package com.expenseai.ai

object PromptTemplates {

    fun receiptParsingPrompt(ocrText: String): String = buildString {
        append("<start_of_turn>system\n")
        append("You are a receipt parser. Extract structured data from receipt text.\n")
        append("Always respond with valid JSON only, no other text.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>user\n")
        append("Parse this receipt and extract: vendor name, total amount, date, category, and line items.\n\n")
        append("Receipt text:\n")
        append(ocrText)
        append("\n\n")
        append("Respond ONLY with JSON in this exact format:\n")
        append("""{"vendor":"string","amount":0.0,"date":"YYYY-MM-DD","category":"food|transport|utilities|shopping|entertainment|health|travel|other","items":["item1","item2"]}""")
        append("\n<end_of_turn>\n")
        append("<start_of_turn>model\n")
    }

    fun categorizationPrompt(description: String): String = buildString {
        append("<start_of_turn>system\n")
        append("You are an expense categorizer. Respond with only the category name.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>user\n")
        append("Categorize this expense: $description\n\n")
        append("Categories: food, transport, utilities, shopping, entertainment, health, travel, other\n\n")
        append("Respond with ONLY the category name.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>model\n")
    }

    fun spendingInsightsPrompt(
        total: Double,
        categoryBreakdown: Map<String, Double>
    ): String = buildString {
        append("<start_of_turn>system\n")
        append("You are a helpful financial advisor. Keep responses concise and actionable.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>user\n")
        append("Analyze my spending for this month and give 2-3 specific tips:\n\n")
        append("Total: ₹%.0f\n".format(total))
        append("Breakdown:\n")
        categoryBreakdown.forEach { (category, amount) ->
            append("- ${category.replaceFirstChar { it.uppercase() }}: ₹%.0f\n".format(amount))
        }
        append("\nKeep response under 100 words. Be specific and actionable.\n")
        append("<end_of_turn>\n")
        append("<start_of_turn>model\n")
    }
}
