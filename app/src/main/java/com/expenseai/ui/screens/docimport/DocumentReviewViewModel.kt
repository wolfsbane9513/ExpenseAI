package com.expenseai.ui.screens.docimport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.DocType
import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.usecase.toIncomeStream
import com.expenseai.domain.usecase.toLiability
import com.expenseai.domain.usecase.usesSanctionDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DocReviewUiState(
    val type: DocType = DocType.UNKNOWN,
    val textPreview: String = "",
    // salary fields (editable text)
    val employer: String = "",
    val monthlyNet: String = "",
    // loan fields (editable text)
    val lender: String = "",
    val emi: String = "",
    val annualRatePct: String = "",
    val principal: String = "",
    val tenureMonths: String = "",
    val emiStartDate: LocalDate? = null,
    val estimatedFromCurrentBalance: Boolean = false,
    val isSaved: Boolean = false,
    val hasExtraction: Boolean = false
)

@HiltViewModel
class DocumentReviewViewModel @Inject constructor(
    private val holder: ExtractionResultHolder,
    private val fireRepository: FireRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DocReviewUiState())
    val uiState: StateFlow<DocReviewUiState> = _uiState.asStateFlow()

    init {
        holder.extraction.value?.let { extraction ->
            val loan = extraction.loan
            val sanction = loan?.usesSanctionDetails == true
            _uiState.value = DocReviewUiState(
                type = extraction.type,
                textPreview = extraction.textPreview,
                employer = extraction.salary?.employer.orEmpty(),
                monthlyNet = extraction.salary?.monthlyNet?.toLong()?.toString().orEmpty(),
                lender = loan?.lender.orEmpty(),
                emi = loan?.emi?.toLong()?.toString().orEmpty(),
                annualRatePct = loan?.annualRatePct?.toString().orEmpty(),
                principal = (if (sanction) loan?.sanctionedPrincipal else loan?.outstandingPrincipal)
                    ?.toLong()?.toString().orEmpty(),
                tenureMonths = (if (sanction) loan?.originalTenureMonths else loan?.remainingTenureMonths)
                    ?.toString().orEmpty(),
                emiStartDate = if (sanction) loan?.emiStartDate else null,
                estimatedFromCurrentBalance = extraction.type == DocType.LOAN_STATEMENT && !sanction,
                hasExtraction = true
            )
        }
    }

    fun setType(type: DocType) = _uiState.update { it.copy(type = type) }
    fun updateEmployer(v: String) = _uiState.update { it.copy(employer = v) }
    fun updateMonthlyNet(v: String) = _uiState.update { it.copy(monthlyNet = v.filter(Char::isDigit)) }
    fun updateLender(v: String) = _uiState.update { it.copy(lender = v) }
    fun updateEmi(v: String) = _uiState.update { it.copy(emi = v.filter(Char::isDigit)) }
    fun updateRate(v: String) = _uiState.update { it.copy(annualRatePct = v.filter { c -> c.isDigit() || c == '.' }) }
    fun updatePrincipal(v: String) = _uiState.update { it.copy(principal = v.filter(Char::isDigit)) }
    fun updateTenure(v: String) = _uiState.update { it.copy(tenureMonths = v.filter(Char::isDigit)) }

    fun confirm() {
        val s = _uiState.value
        viewModelScope.launch {
            val model = fireRepository.getFireModel().first()
            val updated = when (s.type) {
                DocType.SALARY_SLIP -> model.copy(
                    incomes = model.incomes + SalaryFields(
                        employer = s.employer,
                        monthlyNet = s.monthlyNet.toDoubleOrNull()
                    ).toIncomeStream()
                )
                DocType.LOAN_STATEMENT -> {
                    val sanction = s.emiStartDate != null
                    model.copy(
                        liabilities = model.liabilities + LoanFields(
                            lender = s.lender,
                            emi = s.emi.toDoubleOrNull(),
                            annualRatePct = s.annualRatePct.toDoubleOrNull(),
                            sanctionedPrincipal = if (sanction) s.principal.toDoubleOrNull() else null,
                            emiStartDate = s.emiStartDate,
                            originalTenureMonths = if (sanction) s.tenureMonths.toIntOrNull() else null,
                            outstandingPrincipal = if (!sanction) s.principal.toDoubleOrNull() else null,
                            remainingTenureMonths = if (!sanction) s.tenureMonths.toIntOrNull() else null
                        ).toLiability()
                    )
                }
                DocType.UNKNOWN -> model
            }
            if (updated !== model) {
                fireRepository.saveFireModel(updated)
            }
            holder.clear()
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun cancel() = holder.clear()

    val canConfirm: Boolean
        get() = when (_uiState.value.type) {
            DocType.SALARY_SLIP -> _uiState.value.monthlyNet.toDoubleOrNull()?.let { it > 0 } == true
            DocType.LOAN_STATEMENT -> _uiState.value.principal.toDoubleOrNull()?.let { it > 0 } == true &&
                (_uiState.value.tenureMonths.toIntOrNull() ?: 0) > 0
            DocType.UNKNOWN -> false
        }
}
