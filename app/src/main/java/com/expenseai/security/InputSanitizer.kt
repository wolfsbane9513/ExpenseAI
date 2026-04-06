package com.expenseai.security

/**
 * Input sanitization utility to prevent injection attacks
 * in OCR text, user inputs, and LLM prompt construction.
 */
object InputSanitizer {

    // Sanitize user text input for storage
    fun sanitizeTextInput(input: String): String {
        return input
            .trim()
            .replace(Regex("[<>\"';&|`]"), "")  // Remove potential injection chars
            .take(MAX_TEXT_LENGTH)
    }

    // Sanitize amount input - only allow valid numeric format
    fun sanitizeAmountInput(input: String): String {
        return input.filter { it.isDigit() || it == '.' }
            .let { sanitized ->
                // Ensure only one decimal point
                val dotIndex = sanitized.indexOf('.')
                if (dotIndex >= 0) {
                    sanitized.substring(0, dotIndex + 1) +
                            sanitized.substring(dotIndex + 1).replace(".", "")
                } else sanitized
            }
            .take(MAX_AMOUNT_LENGTH)
    }

    // Sanitize OCR text before sending to LLM
    fun sanitizeOcrText(ocrText: String): String {
        return ocrText
            .replace(Regex("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]"), "") // Remove control chars
            .replace(Regex("<start_of_turn>|<end_of_turn>"), "")       // Remove prompt injection markers
            .replace(Regex("<[^>]*>"), "")                             // Strip HTML/XML tags
            .take(MAX_OCR_LENGTH)
    }

    // Sanitize SMS text before sending to LLM
    fun sanitizeSmsText(raw: String): String {
        return raw
            .trim()
            .replace(Regex("<[^>]*>"), "")                              // strip HTML/XML tags
            .replace(Regex("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]"), "") // strip control chars
            .replace(Regex("<start_of_turn>|<end_of_turn>"), "")
            .take(MAX_SMS_LENGTH)
    }

    // HTML is stripped first so any injection markers embedded in tag attributes are
    // removed by the tag-strip pass; the injection-marker step then catches any
    // freestanding markers that survived as plain text.
    // Sanitize email text before sending to LLM
    fun sanitizeEmailText(raw: String): String {
        return raw
            .replace(Regex("<[^>]*>"), "")                              // strip HTML/XML tags (first)
            .replace(Regex("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]"), "") // strip control chars
            .replace(Regex("<start_of_turn>|<end_of_turn>"), "")
            .take(MAX_EMAIL_LENGTH)
    }

    // Sanitize vendor name for display
    fun sanitizeVendorName(vendor: String): String {
        return vendor
            .trim()
            .replace(Regex("[^\\w\\s.,&'-]"), "")
            .take(MAX_VENDOR_LENGTH)
    }

    // Validate category is from allowed list
    fun validateCategory(category: String): String {
        val validCategories = setOf(
            "food", "transport", "utilities", "shopping",
            "entertainment", "health", "travel", "other"
        )
        return if (category.lowercase() in validCategories) category.lowercase() else "other"
    }

    // Validate date format (YYYY-MM-DD)
    fun isValidDate(date: String): Boolean {
        return date.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))
    }

    // Sanitize file path to prevent path traversal
    fun sanitizeFilePath(path: String): String {
        return path
            .replace("..", "")
            .replace("//", "/")
            .replace("\\", "/")
    }

    private const val MAX_TEXT_LENGTH = 500
    private const val MAX_AMOUNT_LENGTH = 15
    private const val MAX_OCR_LENGTH = 5000
    private const val MAX_VENDOR_LENGTH = 100
    private const val MAX_SMS_LENGTH = 500
    private const val MAX_EMAIL_LENGTH = 2000
}
