package com.expenseai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingExpenseDao {

    @Query("SELECT * FROM pending_expenses ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PendingExpenseEntity>>

    @Query("SELECT COUNT(*) FROM pending_expenses")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM pending_expenses WHERE id = :id")
    suspend fun getById(id: Long): PendingExpenseEntity?

    @Query("SELECT COUNT(*) FROM pending_expenses WHERE dedupKey = :key")
    suspend fun countByDedupKey(key: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PendingExpenseEntity): Long

    @Delete
    suspend fun delete(entity: PendingExpenseEntity)

    @Query("DELETE FROM pending_expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_expenses")
    suspend fun deleteAll()
}
