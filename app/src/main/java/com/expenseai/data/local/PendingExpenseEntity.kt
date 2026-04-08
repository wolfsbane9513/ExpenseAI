package com.expenseai.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pending_expenses",
    indices = [Index(value = ["dedupKey"], unique = true)]
)
data class PendingExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vendor: String,
    val amount: Double,
    val category: String,
    val date: String,
    val notes: String = "",
    val items: String = "",          // JSON array, same pattern as ExpenseEntity
    val source: String,              // "sms" or "email"
    val dedupKey: String,            // prevents re-staging the same message
    val rawText: String = "",        // original SMS body / email body for reference
    val createdAt: Long = System.currentTimeMillis()
)
