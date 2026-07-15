package com.expenseai.ui.screens.dashboard

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.GemmaModelManager
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ModelStatus
import com.expenseai.data.local.CategoryTotal
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeParseException
import javax.inject.Inject

data class DashboardUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val totalSpending: Double = 0.0,
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val recentExpenses: List<ExpenseImpact> = emptyList(),
    val modelStatus: ModelStatus = ModelStatus.NOT_DOWNLOADED,
    val modelMessage: String? = null,
    val installedModelName: String? = null,
    val showAddDialog: Boolean = false
)

data class ExpenseImpact(
    val expense: Expense,
    val fireImpactDays: Long = 0
)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val gemmaService: GemmaService,
    private val modelManager: GemmaModelManager,
    private val fireRepository: FireRepository,
    private val fireEngine: FireEngine
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<DashboardUiState> = combine(
        _currentMonth,
        modelManager.status,
        modelManager.errorMessage,
        fireRepository.getFireModel()
    ) { month, modelStatus, modelMessage, fireModel ->
        Quadruple(month, modelStatus, modelMessage, fireModel)
    }.flatMapLatest { (month, modelStatus, modelMessage, fireModel) ->
        combine(
            repository.getMonthlyTotal(month.year, month.monthValue),
            repository.getCategoryTotals(month.year, month.monthValue),
            repository.getRecentExpenses(5)
        ) { total, categories, recent ->
            val recentWithImpact = recent.map { expense ->
                val impact = try {
                    val date = LocalDate.parse(expense.date)
                    fireEngine.fireDaysImpact(fireModel, expense.amount, date)
                } catch (e: Exception) {
                    0L
                }
                ExpenseImpact(expense, impact)
            }
            DashboardUiState(
                currentMonth = month,
                totalSpending = total,
                categoryTotals = categories,
                recentExpenses = recentWithImpact,
                modelStatus = modelStatus,
                modelMessage = modelMessage,
                installedModelName = modelManager.getModelFileName()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    init {
        viewModelScope.launch {
            gemmaService.initialize()
        }
    }

    fun importModel(uri: Uri) {
        viewModelScope.launch {
            try {
                modelManager.importModel(uri)
                gemmaService.initialize()
            } catch (e: Exception) {
                modelManager.updateStatus(ModelStatus.ERROR, e.message ?: "Failed to import model.")
            }
        }
    }

    fun removeModel() {
        modelManager.clearModel()
    }

    fun getModelImportSummary(): String = modelManager.getImportSummary()

    fun previousMonth() {
        _currentMonth.update { it.minusMonths(1) }
    }

    fun nextMonth() {
        val next = _currentMonth.value.plusMonths(1)
        if (next <= YearMonth.now()) {
            _currentMonth.update { next }
        }
    }

    fun showAddDialog() {
        // Handled in UI state
    }

    fun addExpense(vendor: String, amount: Double, category: String, date: String) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    vendor = vendor,
                    amount = amount,
                    category = category,
                    date = date
                )
            )
        }
    }
}
