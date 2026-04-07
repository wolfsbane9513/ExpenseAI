package com.expenseai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ExpenseEntity::class, PendingExpenseEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun pendingExpenseDao(): PendingExpenseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS pending_expenses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        vendor TEXT NOT NULL,
                        amount REAL NOT NULL,
                        category TEXT NOT NULL,
                        date TEXT NOT NULL,
                        notes TEXT NOT NULL DEFAULT '',
                        items TEXT NOT NULL DEFAULT '',
                        source TEXT NOT NULL,
                        dedupKey TEXT NOT NULL,
                        rawText TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_pending_expenses_dedupKey " +
                    "ON pending_expenses(dedupKey)"
                )
            }
        }
    }
}
