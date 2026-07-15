package com.expenseai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fire_model")
data class FireModelEntity(
    @PrimaryKey val id: Int = 0, // Single row for user's FIRE model
    val jsonContent: String
)
