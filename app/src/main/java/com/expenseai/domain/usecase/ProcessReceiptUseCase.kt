package com.expenseai.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.expenseai.ai.GemmaService
import com.expenseai.ai.OCRService
import com.expenseai.ai.ParsedReceipt
import javax.inject.Inject

class ProcessReceiptUseCase @Inject constructor(
    private val ocrService: OCRService,
    private val gemmaService: GemmaService
) {
    suspend fun fromBitmap(bitmap: Bitmap): ParsedReceipt {
        val ocrText = ocrService.extractText(bitmap)
        return gemmaService.parseReceipt(ocrText)
    }

    suspend fun fromUri(uri: Uri): ParsedReceipt {
        val ocrText = ocrService.extractText(uri)
        return gemmaService.parseReceipt(ocrText)
    }
}
