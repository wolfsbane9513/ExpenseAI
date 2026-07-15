package com.expenseai.ai

import android.content.Context
import com.expenseai.security.InputSanitizer
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class ParsedReceipt(
    val vendor: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val category: String = "other",
    val items: List<String> = emptyList()
)

data class ParsedTransaction(
    val vendor: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val category: String = "other",
    val items: List<String> = emptyList()
)

@Singleton
class GemmaService @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val modelManager: GemmaModelManager
) {
    private val gson = Gson()
    private var llmInference: LlmInference? = null
    @Volatile private var isInitialized = false

    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            try {
                modelManager.updateStatus(ModelStatus.LOADING)
                val modelPath = modelManager.getModelPath()
                if (modelPath == null) {
                    modelManager.updateStatus(
                        ModelStatus.NOT_DOWNLOADED,
                        "No AI model is installed yet. Install a MediaPipe-compatible Gemma bundle (.litertlm, .task, .bin, or .tflite) to enable on-device intelligence."
                    )
                    return@withContext
                }
                val options = LlmInference.LlmInferenceOptions.builder()
                    .setModelPath(modelPath)
                    .setMaxTokens(1024)
                    .setTemperature(0.1f)
                    .setTopK(1)
                    .build()
                llmInference?.close()
                llmInference = LlmInference.createFromOptions(context, options)
                isInitialized = true
                modelManager.updateStatus(ModelStatus.READY)
            } catch (_: Exception) {
                modelManager.updateStatus(ModelStatus.ERROR, "Initialization failed")
            }
        }
    }

    suspend fun parseReceipt(ocrText: String): ParsedReceipt {
        val sanitizedText = InputSanitizer.sanitizeOcrText(ocrText)
        if (!isInitialized) return fallbackParseReceipt(sanitizedText)

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.receiptParsingPrompt(sanitizedText)
                val response = runInference(prompt)
                val parsed = parseJsonResponse(response)
                parsed.copy(
                    vendor = InputSanitizer.sanitizeVendorName(parsed.vendor),
                    category = InputSanitizer.validateCategory(parsed.category)
                )
            } catch (_: Exception) {
                fallbackParseReceipt(sanitizedText)
            }
        }
    }

    suspend fun categorizeExpense(description: String): String {
        val sanitized = InputSanitizer.sanitizeTextInput(description)
        if (!isInitialized) return fallbackCategorize(sanitized)

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.categorizationPrompt(sanitized)
                val response = runInference(prompt).trim().lowercase()
                val validCategories = listOf("food", "transport", "utilities", "shopping",
                    "entertainment", "health", "travel", "other")
                if (response in validCategories) response else fallbackCategorize(description)
            } catch (_: Exception) {
                fallbackCategorize(description)
            }
        }
    }

    suspend fun generateInsights(total: Double, breakdown: Map<String, Double>): String {
        if (!isInitialized) return fallbackInsights(total, breakdown)

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.spendingInsightsPrompt(total, breakdown)
                runInference(prompt)
            } catch (_: Exception) {
                fallbackInsights(total, breakdown)
            }
        }
    }

    suspend fun parseSms(smsText: String): ParsedTransaction {
        val sanitized = InputSanitizer.sanitizeTextInput(smsText)
        if (!isInitialized) return fallbackParseTransaction(sanitized)

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.smsParsingPrompt(sanitized)
                val response = runInference(prompt)
                parseTransactionResponse(response)
            } catch (_: Exception) {
                fallbackParseTransaction(sanitized)
            }
        }
    }

    suspend fun parseEmail(emailText: String): ParsedTransaction {
        val sanitized = InputSanitizer.sanitizeTextInput(emailText)
        if (!isInitialized) return fallbackParseTransaction(sanitized)

        return withContext(Dispatchers.IO) {
            try {
                val prompt = PromptTemplates.emailParsingPrompt(sanitized)
                val response = runInference(prompt)
                parseTransactionResponse(response)
            } catch (_: Exception) {
                fallbackParseTransaction(sanitized)
            }
        }
    }

    private fun runInference(prompt: String): String =
        llmInference?.generateResponse(prompt) ?: ""

    private fun parseJsonResponse(json: String): ParsedReceipt {
        return try {
            val cleaned = json.trim()
                .removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            gson.fromJson(cleaned, ParsedReceipt::class.java) ?: ParsedReceipt()
        } catch (_: JsonSyntaxException) {
            ParsedReceipt()
        }
    }

    private fun parseTransactionResponse(json: String): ParsedTransaction {
        return try {
            val cleaned = json.trim()
                .removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            gson.fromJson(cleaned, ParsedTransaction::class.java) ?: ParsedTransaction()
        } catch (_: JsonSyntaxException) {
            ParsedTransaction()
        }
    }

    private fun fallbackParseReceipt(ocrText: String): ParsedReceipt {
        val lines = ocrText.lines().map { it.trim() }.filter { it.isNotBlank() }
        val vendor = lines.firstOrNull() ?: "Unknown"
        val amountRegex = Regex("""(?:total|amount|grand\s*total|net|due)[:\s]*(?:Rs\.?|INR|[₹$])?\s*(\d+(?:[.,]\d{0,2})?)""", RegexOption.IGNORE_CASE)
        val currencyRegex = Regex("""(?:Rs\.?|INR|[₹$])\s*(\d+(?:[.,]\d{0,2})?)""")
        val amount = amountRegex.find(ocrText)?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()
            ?: currencyRegex.findAll(ocrText).lastOrNull()?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()
            ?: 0.0
        val dateRegex = Regex("""(\d{1,2}[/\-]\d{1,2}[/\-]\d{2,4})""")
        val dateMatch = dateRegex.find(ocrText)?.value ?: ""
        return ParsedReceipt(
            vendor = vendor, amount = amount, date = dateMatch,
            category = fallbackCategorize(vendor),
            items = lines.filter { it.contains(Regex("""\d+[.,]\d{2}""")) }.take(10)
        )
    }

    private fun fallbackCategorize(description: String): String {
        val lower = description.lowercase()
        return when {
            lower.containsAny("restaurant","cafe","food","pizza","burger","coffee","tea","bakery","swiggy","zomato","dining") -> "food"
            lower.containsAny("uber","ola","taxi","fuel","petrol","diesel","metro","bus","train","parking") -> "transport"
            lower.containsAny("electricity","water","gas","internet","phone","mobile","recharge","bill") -> "utilities"
            lower.containsAny("amazon","flipkart","myntra","mall","store","shop","market") -> "shopping"
            lower.containsAny("movie","netflix","spotify","game","concert","show","theatre") -> "entertainment"
            lower.containsAny("hospital","doctor","medicine","pharmacy","medical","clinic","health") -> "health"
            lower.containsAny("hotel","flight","booking","travel","trip","airport") -> "travel"
            else -> "other"
        }
    }

    private fun fallbackInsights(total: Double, breakdown: Map<String, Double>): String {
        val topCategory = breakdown.maxByOrNull { it.value }
        val percentage = if (total > 0 && topCategory != null) (topCategory.value / total * 100).toInt() else 0
        return buildString {
            append("This month you spent ₹%.0f in total. ".format(total))
            if (topCategory != null) append("Your biggest category was ${topCategory.key} at $percentage% of total spending. ")
            append("Consider reviewing your top spending categories for potential savings.")
        }
    }

    private fun fallbackParseTransaction(text: String): ParsedTransaction {
        val amountRegex = Regex("""(?:Rs\.?|INR|[₹$])\s*(\d+(?:[.,]\d{0,2})?)""", RegexOption.IGNORE_CASE)
        val amount = amountRegex.find(text)?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull() ?: 0.0
        val vendorRegex = Regex("""(?:at|to|from)\s+([A-Z0-9\s*&]+?)(?:\s+on|\s+for|\s+ref|$)""", RegexOption.IGNORE_CASE)
        val vendor = vendorRegex.find(text)?.groupValues?.get(1)?.trim() ?: "Unknown"
        
        return ParsedTransaction(
            vendor = vendor,
            amount = amount,
            date = java.time.LocalDate.now().toString(),
            category = fallbackCategorize(vendor)
        )
    }

    private fun String.containsAny(vararg keywords: String): Boolean = keywords.any { this.contains(it) }
}
