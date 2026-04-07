package com.expenseai.ai

import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Regex-based parser for Indian bank transaction SMS messages.
 * Returns null for non-transaction messages (OTPs, promotions, etc.)
 */
@Singleton
class SmsParser @Inject constructor() {

    fun parse(sms: String): ParsedReceipt? {
        val amount = extractAmount(sms) ?: return null
        val vendor = extractVendor(sms)
        val date = extractDate(sms)
        return ParsedReceipt(
            vendor = vendor,
            amount = amount,
            date = date,
            category = "other",   // caller can call GemmaService.categorizeExpense() to refine
            items = emptyList()
        )
    }

    private fun extractAmount(sms: String): Double? {
        // Collect all currency amounts with their positions
        val amountPattern = Regex(
            """(?:(?:Rs\.?|INR)\s*([\d,]+(?:\.\d{1,2})?))|(?:₹\s*([\d,]+(?:\.\d{1,2})?))""",
            RegexOption.IGNORE_CASE
        )
        val candidates = amountPattern.findAll(sms).mapNotNull { m ->
            val raw = m.groupValues[1].ifBlank { m.groupValues[2] }.replace(",", "")
            val value = raw.toDoubleOrNull() ?: return@mapNotNull null
            Pair(m.range.first, value)
        }.toList()

        if (candidates.isEmpty()) return null

        // Prefer amount closest to a debit/spend keyword to avoid returning balance amounts
        val debitKeywords = Regex("""(?:debited|spent|withdrawn|transferred|charged|paid)""", RegexOption.IGNORE_CASE)
        val keywordPositions = debitKeywords.findAll(sms).map { it.range.first }.toList()

        return if (keywordPositions.isNotEmpty()) {
            candidates.minByOrNull { (pos, _) ->
                keywordPositions.minOf { kp -> Math.abs(pos - kp) }
            }?.second
        } else {
            candidates.first().second
        }
    }

    private fun extractVendor(sms: String): String {
        val patterns = listOf(
            Regex("""(?:to|at)\s+([A-Za-z0-9@._\-]{3,40})""", RegexOption.IGNORE_CASE),
            Regex("""VPA\s+([A-Za-z0-9@._\-]+)""", RegexOption.IGNORE_CASE),
            Regex("""merchant[:\s]+([A-Za-z0-9 &]{3,40})""", RegexOption.IGNORE_CASE),
        )
        for (pattern in patterns) {
            val match = pattern.find(sms) ?: continue
            val candidate = match.groupValues[1].trim()
            if (candidate.length >= 3 && !candidate.matches(Regex("""[\dX/\-]+"""))) {
                return candidate.take(60)
            }
        }
        return sms.split(" ").firstOrNull()?.trimEnd(':') ?: "Unknown"
    }

    private fun extractDate(sms: String): String {
        // dd-MM-yy
        Regex("""(\d{2}-\d{2}-\d{2})(?!\d)""").find(sms)?.let { m ->
            return try {
                val d = LocalDate.parse(m.value, DateTimeFormatter.ofPattern("dd-MM-yy"))
                d.toString()
            } catch (e: DateTimeParseException) { "" }
        }
        // dd-MM-yyyy or dd/MM/yyyy
        Regex("""(\d{2}[-/]\d{2}[-/]\d{4})""").find(sms)?.let { m ->
            return try {
                val fmt = if (m.value.contains('/')) DateTimeFormatter.ofPattern("dd/MM/yyyy")
                          else DateTimeFormatter.ofPattern("dd-MM-yyyy")
                LocalDate.parse(m.value, fmt).toString()
            } catch (e: DateTimeParseException) { "" }
        }
        // dd-Mon-yy  e.g. 06-Apr-26
        Regex("""(\d{2}-[A-Za-z]{3}-\d{2})""").find(sms)?.let { m ->
            return try {
                LocalDate.parse(m.value, DateTimeFormatter.ofPattern("dd-MMM-yy")).toString()
            } catch (e: DateTimeParseException) { "" }
        }
        return LocalDate.now().toString()
    }

    companion object {
        fun dedupKey(sms: String): String {
            val refPattern = Regex("""(?:Ref(?:erence)?|UPI Ref)\s*(?:No\.?)?\s*([A-Z0-9]{9,20})""", RegexOption.IGNORE_CASE)
            val ref = refPattern.find(sms)?.groupValues?.get(1)
            if (!ref.isNullOrBlank()) return ref
            return MessageDigest.getInstance("SHA-256")
                .digest(sms.trim().toByteArray())
                .take(8)
                .joinToString("") { "%02x".format(it) }
        }
    }
}
