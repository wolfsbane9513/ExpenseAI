package com.expenseai.security

import android.content.Context
import android.net.Uri
import com.expenseai.data.local.ExpenseDao
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure data export with sanitization.
 * Exports expenses to JSON format for backup/portability.
 */
@Singleton
class DataExporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val expenseDao: ExpenseDao
) {
    private val gson = Gson()

    suspend fun exportToJson(outputUri: Uri): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val expenses = expenseDao.getAllExpenses().first()

            // Sanitize before export - strip image URIs (contain local paths)
            val sanitized = expenses.map { entity ->
                entity.copy(imageUri = null)
            }

            context.contentResolver.openOutputStream(outputUri)?.use { stream ->
                OutputStreamWriter(stream, Charsets.UTF_8).use { writer ->
                    writer.write(gson.toJson(sanitized))
                }
            }

            Result.success(sanitized.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun exportToCsv(outputUri: Uri): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val expenses = expenseDao.getAllExpenses().first()

            context.contentResolver.openOutputStream(outputUri)?.use { stream ->
                OutputStreamWriter(stream, Charsets.UTF_8).use { writer ->
                    writer.write("Date,Vendor,Amount,Category,Notes,Source\n")
                    expenses.forEach { e ->
                        val vendor = e.vendor.replace(",", ";").replace("\"", "'")
                        val notes = e.notes.replace(",", ";").replace("\"", "'")
                        writer.write("${e.date},\"$vendor\",${e.amount},${e.category},\"$notes\",${e.source}\n")
                    }
                }
            }

            Result.success(expenses.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
