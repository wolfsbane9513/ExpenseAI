package com.expenseai.domain.usecase

import com.expenseai.ai.GemmaService
import com.expenseai.ai.SmsParser
import com.expenseai.ai.SmsReader
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.security.InputSanitizer
import javax.inject.Inject

class ProcessSmsUseCase @Inject constructor(
    private val smsReader: SmsReader,
    private val smsParser: SmsParser,
    private val gemmaService: GemmaService,
    private val pendingDao: PendingExpenseDao
) {
    /**
     * Reads SMS inbox, parses transactions, stages new ones as pending.
     * Returns the count of newly staged expenses.
     */
    suspend fun execute(): Int {
        val messages = smsReader.readInbox()
        var staged = 0
        for (raw in messages) {
            val sanitized = InputSanitizer.sanitizeSmsText(raw.body)
            val key = SmsParser.dedupKey(sanitized)
            if (pendingDao.countByDedupKey(key) > 0) continue

            // Use Gemma if available, otherwise fallback to Regex parser
            val ai = gemmaService.parseSms(sanitized)
            val fallback = smsParser.parse(sanitized)
            val amount = if (ai.amount > 0) ai.amount else fallback?.amount ?: 0.0
            if (amount <= 0.0) continue // neither parser found a transaction

            val vendor = ai.vendor.ifBlank { fallback?.vendor ?: "Unknown" }
            val date = ai.date.ifBlank { fallback?.date ?: "" }
            val category = if (ai.amount > 0) ai.category else fallback?.category ?: ai.category

            pendingDao.insert(
                PendingExpenseEntity(
                    vendor = InputSanitizer.sanitizeVendorName(vendor),
                    amount = amount,
                    category = InputSanitizer.validateCategory(category),
                    date = date,
                    source = "sms",
                    dedupKey = key,
                    rawText = sanitized
                )
            )
            staged++
        }
        return staged
    }
}
