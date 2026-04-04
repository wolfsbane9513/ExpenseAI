package com.expenseai.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.GemmaService
import com.expenseai.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class InsightsUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val totalSpending: Double = 0.0,
    val categoryBreakdown: Map<String, Double> = emptyMap(),
    val aiInsights: String = "",
    val isLoadingInsights: Boolean = false
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val gemmaService: GemmaService
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _aiInsights = MutableStateFlow("")
    private val _isLoadingInsights = MutableStateFlow(false)

    val uiState: StateFlow<InsightsUiState> = combine(
        _currentMonth,
        _aiInsights,
        _isLoadingInsights
    ) { month, insights, loading ->
        Triple(month, insights, loading)
    }.flatMapLatest { (month, insights, loading) ->
        combine(
            repository.getMonthlyTotal(month.year, month.monthValue),
            repository.getCategoryTotals(month.year, month.monthValue)
        ) { total, categories ->
            InsightsUiState(
                currentMonth = month,
                totalSpending = total,
                categoryBreakdown = categories.associate { it.category to it.total },
                aiInsights = insights,
                isLoadingInsights = loading
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InsightsUiState()
    )

    fun generateInsights() {
        viewModelScope.launch {
            _isLoadingInsights.value = true
            try {
                val state = uiState.value
                val insights = gemmaService.generateInsights(
                    state.totalSpending,
                    state.categoryBreakdown
                )
                _aiInsights.value = insights
            } catch (e: Exception) {
                _aiInsights.value = "Unable to generate insights at this time."
            } finally {
                _isLoadingInsights.value = false
            }
        }
    }

    fun previousMonth() {
        _currentMonth.update { it.minusMonths(1) }
        _aiInsights.value = ""
    }

    fun nextMonth() {
        val next = _currentMonth.value.plusMonths(1)
        if (next <= YearMonth.now()) {
            _currentMonth.update { next }
            _aiInsights.value = ""
        }
    }
}
