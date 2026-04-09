package com.expenseai.ui.screens.dashboard

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.GemmaModelManager
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ModelStatus
import com.expenseai.data.local.CategoryTotal
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class DashboardUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val totalSpending: Double = 0.0,
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val recentExpenses: List<Expense> = emptyList(),
    val modelStatus: ModelStatus = ModelStatus.NOT_DOWNLOADED,
    val modelMessage: String? = null,
    val installedModelName: String? = null,
    val showAddDialog: Boolean = false
)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val gemmaService: GemmaService,
    private val modelManager: GemmaModelManager
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<DashboardUiState> = combine(
        _currentMonth,
        modelManager.status,
        modelManager.errorMessage
    ) { month, modelStatus, modelMessage ->
        Triple(month, modelStatus, modelMessage)
    }.flatMapLatest { (month, modelStatus, modelMessage) ->
        combine(
            repository.getMonthlyTotal(month.year, month.monthValue),
            repository.getCategoryTotals(month.year, month.monthValue),
            repository.getRecentExpenses(5)
        ) { total, categories, recent ->
            DashboardUiState(
                currentMonth = month,
                totalSpending = total,
                categoryTotals = categories,
                recentExpenses = recent,
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
