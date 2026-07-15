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
        val parsedResult = gemmaService.parseEmail(sanitizedBody)
        val vendor = parsedResult.vendor.ifBlank { emailParser.parse(sanitizedBody, sanitizedSubject)?.vendor ?: "Unknown" }
        val amount = if (parsedResult.amount > 0) parsedResult.amount else (emailParser.parse(sanitizedBody, sanitizedSubject)?.amount ?: 0.0)
        val date = parsedResult.date.ifBlank { emailParser.parse(sanitizedBody, sanitizedSubject)?.date ?: "" }
        val category = parsedResult.category
        val items = if (parsedResult.items.isNotEmpty()) {
            com.google.gson.Gson().toJson(parsedResult.items)
        } else {
            com.google.gson.Gson().toJson(emailParser.parse(sanitizedBody, sanitizedSubject)?.items ?: emptyList<String>())
        }

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
