package com.expenseai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FireModelDao {
    @Query("SELECT * FROM fire_model WHERE id = 0")
    fun getFireModel(): Flow<FireModelEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFireModel(entity: FireModelEntity)
}
