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
        val parsed = emailParser.parse(sanitizedBody, sanitizedSubject) ?: return false

        val key = EmailParser.dedupKey(sanitizedBody, sanitizedSubject)
        if (pendingDao.countByDedupKey(key) > 0) return false

        val category = gemmaService.categorizeExpense(parsed.vendor)

        pendingDao.insert(
            PendingExpenseEntity(
                vendor = InputSanitizer.sanitizeVendorName(parsed.vendor),
                amount = parsed.amount,
                category = InputSanitizer.validateCategory(category),
                date = parsed.date,
                source = "email",
                dedupKey = key,
                rawText = sanitizedBody
            )
        )
        return true
    }
}
