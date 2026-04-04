package com.expenseai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vendor: String,
    val amount: Double,
    val category: String,
    val date: String,
    val notes: String = "",
    val items: String = "",
    val imageUri: String? = null,
    val source: String = "manual",
    val createdAt: Long = System.currentTimeMillis()
)
