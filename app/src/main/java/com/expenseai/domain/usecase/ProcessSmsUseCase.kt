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
            val parsedResult = gemmaService.parseSms(sanitized)
            val vendor = parsedResult.vendor.ifBlank { smsParser.parse(sanitized)?.vendor ?: "Unknown" }
            val amount = if (parsedResult.amount > 0) parsedResult.amount else (smsParser.parse(sanitized)?.amount ?: 0.0)
            val date = parsedResult.date.ifBlank { smsParser.parse(sanitized)?.date ?: "" }
            val category = parsedResult.category

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
