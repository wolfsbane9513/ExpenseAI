package com.expenseai.domain.usecase

import com.expenseai.ai.EmailParser
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedReceipt
import com.expenseai.ai.SmsParser
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.data.local.PendingExpenseEntity
import com.expenseai.security.InputSanitizer
import javax.inject.Inject

class ProcessSharedTextUseCase @Inject constructor(
    private val emailParser: EmailParser,
    private val smsParser: SmsParser,
    private val gemmaService: GemmaService,
    private val pendingDao: PendingExpenseDao
) {

    suspend fun execute(body: String, subject: String = ""): Boolean {
        val sanitizedSubject = InputSanitizer.sanitizeTextInput(subject)
        val sanitizedEmailBody = InputSanitizer.sanitizeEmailText(body)
        val sanitizedSmsBody = InputSanitizer.sanitizeSmsText(body)

        val emailParsed = emailParser.parse(sanitizedEmailBody, sanitizedSubject)
        val smsParsed = smsParser.parse(sanitizedSmsBody)
        val candidate = chooseCandidate(
            rawBody = body,
            subject = sanitizedSubject,
            emailBody = sanitizedEmailBody,
            smsBody = sanitizedSmsBody,
            emailParsed = emailParsed,
            smsParsed = smsParsed
        ) ?: return false

        if (pendingDao.countByDedupKey(candidate.dedupKey) > 0) return false

        val category = gemmaService.categorizeExpense(candidate.parsed.vendor)

        pendingDao.insert(
            PendingExpenseEntity(
                vendor = InputSanitizer.sanitizeVendorName(candidate.parsed.vendor),
                amount = candidate.parsed.amount,
                category = InputSanitizer.validateCategory(category),
                date = candidate.parsed.date,
                source = candidate.source,
                dedupKey = candidate.dedupKey,
                rawText = candidate.rawText
            )
        )
        return true
    }

    private fun chooseCandidate(
        rawBody: String,
        subject: String,
        emailBody: String,
        smsBody: String,
        emailParsed: ParsedReceipt?,
        smsParsed: ParsedReceipt?
    ): SharedCandidate? {
        val smsCandidate = smsParsed?.let {
            SharedCandidate(
                parsed = it,
                source = "sms",
                dedupKey = SmsParser.dedupKey(smsBody),
                rawText = smsBody
            )
        }
        val emailCandidate = emailParsed?.let {
            SharedCandidate(
                parsed = it,
                source = "email",
                dedupKey = EmailParser.dedupKey(emailBody, subject),
                rawText = emailBody
            )
        }

        return when {
            looksLikeSms(rawBody, subject) && smsCandidate != null -> smsCandidate
            emailCandidate != null -> emailCandidate
            smsCandidate != null -> smsCandidate
            else -> null
        }
    }

    private fun looksLikeSms(body: String, subject: String): Boolean {
        if (subject.isNotBlank()) return false
        val compactBody = body.replace("\n", " ")
        return smsMarkers.any { marker -> compactBody.contains(marker, ignoreCase = true) }
    }

    private data class SharedCandidate(
        val parsed: ParsedReceipt,
        val source: String,
        val dedupKey: String,
        val rawText: String
    )

    companion object {
        private val smsMarkers = listOf(
            "debited",
            "credited",
            "spent",
            "upi",
            "a/c",
            "acct",
            "avl bal",
            "available balance",
            "card ending",
            "vpa"
        )
    }
}
