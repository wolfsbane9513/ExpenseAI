package com.expenseai.domain.usecase

import android.net.Uri
import com.expenseai.ai.DocumentExtraction
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.PdfTextExtractor
import com.expenseai.security.InputSanitizer
import javax.inject.Inject

class EmptyDocumentException : Exception("No readable text found in document")

class ExtractDocumentUseCase @Inject constructor(
    private val pdfTextExtractor: PdfTextExtractor,
    private val documentParser: DocumentParser
) {
    /** Extracts, sanitizes, classifies and parses a PDF. Throws on unreadable/empty docs. */
    suspend fun execute(uri: Uri): DocumentExtraction {
        val pages = pdfTextExtractor.extract(uri)
        val fullText = InputSanitizer.sanitizeDocumentText(pages.joinToString("\n\n"))
        if (fullText.isBlank()) throw EmptyDocumentException()
        val type = documentParser.classify(fullText)
        return documentParser.parse(fullText, type)
    }
}
