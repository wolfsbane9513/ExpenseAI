package com.expenseai.domain.usecase

import com.expenseai.ai.GemmaService
import javax.inject.Inject

class CategorizeExpenseUseCase @Inject constructor(
    private val gemmaService: GemmaService
) {
    suspend operator fun invoke(description: String): String =
        gemmaService.categorizeExpense(description)
}
