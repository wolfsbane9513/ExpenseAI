package com.expenseai.domain.usecase

import com.expenseai.ai.GemmaService
import javax.inject.Inject

class GenerateInsightsUseCase @Inject constructor(
    private val gemmaService: GemmaService
) {
    suspend operator fun invoke(total: Double, breakdown: Map<String, Double>): String =
        gemmaService.generateInsights(total, breakdown)
}
