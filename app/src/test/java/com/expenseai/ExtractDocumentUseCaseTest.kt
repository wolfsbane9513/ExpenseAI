package com.expenseai

import android.net.Uri
import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentExtraction
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.PdfTextExtractor
import com.expenseai.domain.usecase.EmptyDocumentException
import com.expenseai.domain.usecase.ExtractDocumentUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExtractDocumentUseCaseTest {

    private lateinit var pdfTextExtractor: PdfTextExtractor
    private lateinit var documentParser: DocumentParser
    private lateinit var useCase: ExtractDocumentUseCase
    private val uri = mockk<Uri>()

    @Before
    fun setup() {
        pdfTextExtractor = mockk()
        documentParser = mockk()
        useCase = ExtractDocumentUseCase(pdfTextExtractor, documentParser)
    }

    @Test
    fun `joins pages, classifies and parses`() = runTest {
        coEvery { pdfTextExtractor.extract(uri) } returns listOf("Payslip page 1", "Net Pay 1,50,000")
        every { documentParser.classify(any()) } returns DocType.SALARY_SLIP
        coEvery { documentParser.parse(any(), DocType.SALARY_SLIP) } returns
            DocumentExtraction(DocType.SALARY_SLIP, textPreview = "x")

        val result = useCase.execute(uri)
        assertEquals(DocType.SALARY_SLIP, result.type)
    }

    @Test(expected = EmptyDocumentException::class)
    fun `blank ocr output throws EmptyDocumentException`() = runTest {
        coEvery { pdfTextExtractor.extract(uri) } returns listOf("", "   ")
        useCase.execute(uri)
    }
}
