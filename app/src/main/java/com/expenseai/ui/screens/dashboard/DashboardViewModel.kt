package com.expenseai.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.GemmaModelManager
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ModelStatus
import com.expenseai.data.local.CategoryTotal
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val showAddDialog: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val gemmaService: GemmaService,
    private val modelManager: GemmaModelManager
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<DashboardUiState> = combine(
        _currentMonth,
        modelManager.status
    ) { month, modelStatus ->
        month to modelStatus
    }.flatMapLatest { (month, modelStatus) ->
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
                modelStatus = modelStatus
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
