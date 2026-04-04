package com.expenseai.data.repository

import com.expenseai.data.local.CategoryTotal
import com.expenseai.data.local.ExpenseDao
import com.expenseai.data.local.ExpenseEntity
import com.expenseai.domain.model.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    private val gson = Gson()

    fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses().map { entities -> entities.map { it.toDomain() } }

    fun getExpensesByMonth(year: Int, month: Int): Flow<List<Expense>> {
        val startDate = String.format("%04d-%02d-01", year, month)
        val endDate = String.format("%04d-%02d-31", year, month)
        return expenseDao.getExpensesByDateRange(startDate, endDate)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getMonthlyTotal(year: Int, month: Int): Flow<Double> {
        val startDate = String.format("%04d-%02d-01", year, month)
        val endDate = String.format("%04d-%02d-31", year, month)
        return expenseDao.getTotalByDateRange(startDate, endDate)
            .map { it ?: 0.0 }
    }

    fun getCategoryTotals(year: Int, month: Int): Flow<List<CategoryTotal>> {
        val startDate = String.format("%04d-%02d-01", year, month)
        val endDate = String.format("%04d-%02d-31", year, month)
        return expenseDao.getCategoryTotals(startDate, endDate)
    }

    fun getRecentExpenses(limit: Int = 5): Flow<List<Expense>> =
        expenseDao.getRecentExpenses(limit).map { entities -> entities.map { it.toDomain() } }

    fun searchExpenses(query: String): Flow<List<Expense>> =
        expenseDao.searchExpenses(query).map { entities -> entities.map { it.toDomain() } }

    suspend fun getExpenseById(id: Long): Expense? =
        expenseDao.getExpenseById(id)?.toDomain()

    suspend fun addExpense(expense: Expense): Long =
        expenseDao.insertExpense(expense.toEntity())

    suspend fun updateExpense(expense: Expense) =
        expenseDao.updateExpense(expense.toEntity())

    suspend fun deleteExpense(id: Long) =
        expenseDao.deleteExpenseById(id)

    private fun ExpenseEntity.toDomain(): Expense = Expense(
        id = id,
        vendor = vendor,
        amount = amount,
        category = category,
        date = date,
        notes = notes,
        items = if (items.isBlank()) emptyList()
                else gson.fromJson(items, object : TypeToken<List<String>>() {}.type),
        imageUri = imageUri,
        source = source,
        createdAt = createdAt
    )

    private fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
        id = id,
        vendor = vendor,
        amount = amount,
        category = category,
        date = date,
        notes = notes,
        items = gson.toJson(items),
        imageUri = imageUri,
        source = source,
        createdAt = createdAt
    )
}
