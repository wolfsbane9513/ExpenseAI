package com.expenseai.domain.usecase

import com.expenseai.ai.EmailParser
import com.expenseai.ai.GemmaService
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.security.InputSanitizer
import javax.inject.Inject

class ProcessEmailUseCase @Inject constructor(
    private val emailParser: EmailParser,
    private val gemmaService: GemmaService,
    private val pendingDao: PendingExpenseDao
) {
    /**
     * Parses a shared email body. Returns true if a new pending expense was staged.
     */
    suspend fun execute(body: String, subject: String = ""): Boolean {
        val sanitizedBody = InputSanitizer.sanitizeEmailText(body)
        val sanitizedSubject = InputSanitizer.sanitizeTextInput(subject)

        val key = EmailParser.dedupKey(sanitizedBody, sanitizedSubject)
        if (pendingDao.countByDedupKey(key) > 0) return false

        // Use Gemma if available, otherwise fallback to Regex parser
        val ai = gemmaService.parseEmail(sanitizedBody)
        val fallback = emailParser.parse(sanitizedBody, sanitizedSubject)
        val amount = if (ai.amount > 0) ai.amount else fallback?.amount ?: 0.0
        if (amount <= 0.0) return false // neither parser found a transaction

        val vendor = ai.vendor.ifBlank { fallback?.vendor ?: "Unknown" }
        val date = ai.date.ifBlank { fallback?.date ?: "" }
        val category = if (ai.amount > 0) ai.category else fallback?.category ?: ai.category
        val items = com.google.gson.Gson().toJson(ai.items.ifEmpty { fallback?.items ?: emptyList() })

        pendingDao.insert(
            PendingExpenseEntity(
                vendor = InputSanitizer.sanitizeVendorName(vendor),
                amount = amount,
                category = InputSanitizer.validateCategory(category),
                date = date,
                source = "email",
                dedupKey = key,
                rawText = sanitizedBody,
                items = items
            )
        )
        return true
    }
}
