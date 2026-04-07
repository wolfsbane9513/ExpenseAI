package com.expenseai.ai

import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailParser @Inject constructor() {

    /**
     * Returns null if no financial amount can be found — indicates non-transactional email.
     */
    fun parse(body: String, subject: String = ""): ParsedReceipt? {
        val amount = extractAmount(body) ?: return null
        val vendor = extractVendor(body, subject)
        val date = extractDate(body)
        return ParsedReceipt(
            vendor = vendor,
            amount = amount,
            date = date,
            category = "other",
            items = extractItems(body)
        )
    }

    private fun extractAmount(text: String): Double? {
        // Priority: Grand Total > Total > Amount > any currency value
        val labelPatterns = listOf(
            Regex("""(?:Grand\s+Total|Order\s+Total|Amount\s+Paid|Total\s+Amount)[:\s]*(?:Rs\.?|INR|₹)?\s*([\d,]+(?:\.\d{1,2})?)""", RegexOption.IGNORE_CASE),
            Regex("""(?:Total|Amount)[:\s]*(?:Rs\.?|INR|₹)?\s*([\d,]+(?:\.\d{1,2})?)""", RegexOption.IGNORE_CASE),
            Regex("""(?:Rs\.?|INR|₹)\s*([\d,]+(?:\.\d{1,2})?)""", RegexOption.IGNORE_CASE),
        )
        for (pattern in labelPatterns) {
            val match = pattern.find(text) ?: continue
            val raw = match.groupValues[1].replace(",", "")
            val value = raw.toDoubleOrNull() ?: continue
            if (value > 0) return value
        }
        return null
    }

    private fun extractVendor(body: String, subject: String): String {
        // Try subject line first — often contains merchant name
        val subjectVendorPatterns = listOf(
            Regex("""Your\s+([A-Za-z0-9 &]+?)\s+(?:order|receipt|booking|invoice)""", RegexOption.IGNORE_CASE),
            Regex("""(?:from|by)\s+([A-Za-z0-9 &]{3,30})""", RegexOption.IGNORE_CASE),
        )
        for (pattern in subjectVendorPatterns) {
            val match = pattern.find(subject) ?: continue
            val v = match.groupValues[1].trim()
            if (v.length >= 3) return v
        }
        // Try body
        Regex("""(?:merchant|store|from|vendor)[:\s]+([A-Za-z0-9 &]{3,40})""", RegexOption.IGNORE_CASE)
            .find(body)?.let { return it.groupValues[1].trim() }
        // Use first meaningful word of subject
        return subject.split(" ").firstOrNull { it.length > 2 } ?: "Unknown"
    }

    private fun extractDate(text: String): String {
        // ISO: 2026-04-06 — fall through on parse failure so subsequent formats are tried
        Regex("""(\d{4}-\d{2}-\d{2})""").find(text)?.let {
            try { return LocalDate.parse(it.value).toString() } catch (_: Exception) { /* fall through */ }
        }
        // dd Mon yyyy  e.g. 06 Apr 2026
        Regex("""(\d{1,2}\s+[A-Za-z]{3}\s+\d{4})""").find(text)?.let { m ->
            try {
                return LocalDate.parse(m.value, DateTimeFormatter.ofPattern("d MMM yyyy")).toString()
            } catch (_: DateTimeParseException) { /* fall through */ }
        }
        // dd-MM-yyyy or dd/MM/yyyy
        Regex("""(\d{2}[-/]\d{2}[-/]\d{4})""").find(text)?.let { m ->
            try {
                val fmt = if (m.value.contains('/')) DateTimeFormatter.ofPattern("dd/MM/yyyy")
                          else DateTimeFormatter.ofPattern("dd-MM-yyyy")
                return LocalDate.parse(m.value, fmt).toString()
            } catch (_: DateTimeParseException) { /* fall through */ }
        }
        return LocalDate.now().toString()
    }

    private fun extractItems(body: String): List<String> {
        // Lines that look like "Item name ... Rs X"
        return body.lines()
            .filter { line ->
                line.contains(Regex("""(?:Rs\.?|INR|₹)\s*[\d,]+""", RegexOption.IGNORE_CASE)) &&
                line.trim().length in 5..80
            }
            .take(10)
            .map { it.trim() }
    }

    companion object {
        fun dedupKey(body: String, subject: String): String {
            // Prefer order/booking ID
            val idPattern = Regex("""(?:Order|Booking|Ref|PNR)[:\s#]*([A-Z0-9\-]{6,20})""", RegexOption.IGNORE_CASE)
            val id = idPattern.find(body)?.groupValues?.get(1)
                ?: idPattern.find(subject)?.groupValues?.get(1)
            if (!id.isNullOrBlank()) return id
            return MessageDigest.getInstance("SHA-256")
                .digest("$subject|$body".trim().toByteArray())
                .take(8)
                .joinToString("") { "%02x".format(it) }
        }
    }
}
