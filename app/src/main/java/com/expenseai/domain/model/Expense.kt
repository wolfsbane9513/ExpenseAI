package com.expenseai.domain.model

data class Expense(
    val id: Long = 0,
    val vendor: String,
    val amount: Double,
    val category: String,
    val date: String,
    val notes: String = "",
    val items: List<String> = emptyList(),
    val imageUri: String? = null,
    val source: String = "manual",
    val createdAt: Long = System.currentTimeMillis()
)
