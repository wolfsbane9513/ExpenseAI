package com.expenseai.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val expenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HistoryUiState> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllExpenses()
            } else {
                repository.searchExpenses(query)
            }.map { expenses ->
                HistoryUiState(
                    expenses = expenses,
                    searchQuery = query,
                    isSearching = query.isNotBlank()
                )
            }
        }
        .stateIn(
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
