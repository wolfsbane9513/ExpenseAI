package com.expenseai.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.model.Expense
import com.expenseai.ui.screens.dashboard.ExpenseImpact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HistoryUiState(
    val expenses: List<ExpenseImpact> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val fireRepository: FireRepository,
    private val fireEngine: FireEngine
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HistoryUiState> = combine(
        _searchQuery.debounce(300),
        fireRepository.getFireModel()
    ) { query, fireModel ->
        query to fireModel
    }.flatMapLatest { (query, fireModel) ->
        val flow = if (query.isBlank()) {
            repository.getAllExpenses()
        } else {
            repository.searchExpenses(query)
        }
        
        flow.map { expenses ->
            val expensesWithImpact = expenses.map { expense ->
                val impact = try {
                    val date = LocalDate.parse(expense.date)
                    fireEngine.fireDaysImpact(fireModel, expense.amount, date)
                } catch (e: Exception) {
                    0L
                }
                ExpenseImpact(expense, impact)
            }
            HistoryUiState(
                expenses = expensesWithImpact,
                searchQuery = query,
                isSearching = query.isNotBlank()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteExpense(id)
        }
    }
}
