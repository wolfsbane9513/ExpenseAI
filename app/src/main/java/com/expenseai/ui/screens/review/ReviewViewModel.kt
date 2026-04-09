package com.expenseai.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import com.expenseai.domain.usecase.ProcessSharedTextUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val pendingDao: PendingExpenseDao,
    private val repository: ExpenseRepository,
    private val processSharedTextUseCase: ProcessSharedTextUseCase
) : ViewModel() {

    private val gson = Gson()

    val pendingExpenses: StateFlow<List<PendingExpenseEntity>> = pendingDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun confirm(item: PendingExpenseEntity) {
        viewModelScope.launch {
            repository.addExpense(item.toExpense())
            pendingDao.deleteById(item.id)
        }
    }

    fun reject(item: PendingExpenseEntity) {
        viewModelScope.launch {
            pendingDao.deleteById(item.id)
        }
    }

    fun processSharedText(body: String, subject: String) {
        viewModelScope.launch {
            processSharedTextUseCase.execute(body = body, subject = subject)
        }
    }

    private fun PendingExpenseEntity.toExpense(): Expense {
        val itemsList: List<String> = if (items.isBlank()) emptyList()
        else gson.fromJson(items, object : TypeToken<List<String>>() {}.type)
        return Expense(
            vendor = vendor, amount = amount, category = category,
            date = date, notes = notes, items = itemsList,
            source = source, createdAt = System.currentTimeMillis()
        )
    }
}
