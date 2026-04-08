package com.expenseai.ai

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

data class RawSms(val body: String, val timestamp: Long, val address: String)

@Singleton
class SmsReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Returns SMSes from the last [days] days, up to [limit].
     * Caller must verify READ_SMS permission before calling this.
     */
    fun readInbox(days: Int = 30, limit: Int = 500): List<RawSms> {
        val cutoff = Instant.now().minus(days.toLong(), ChronoUnit.DAYS).toEpochMilli()
        val uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("body", "date", "address")
        val selection = "date > ?"
        val selectionArgs = arrayOf(cutoff.toString())
        val sortOrder = "date DESC LIMIT $limit"

        val result = mutableListOf<RawSms>()
        context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                val bodyIdx = cursor.getColumnIndexOrThrow("body")
                val dateIdx = cursor.getColumnIndexOrThrow("date")
                val addrIdx = cursor.getColumnIndexOrThrow("address")
                while (cursor.moveToNext()) {
                    result.add(
                        RawSms(
                            body = cursor.getString(bodyIdx) ?: "",
                            timestamp = cursor.getLong(dateIdx),
                            address = cursor.getString(addrIdx) ?: ""
                        )
                    )
                }
            }
        return result
    }
}
