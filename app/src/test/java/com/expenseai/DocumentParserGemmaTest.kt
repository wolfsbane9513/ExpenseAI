package com.expenseai

import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentParser
import com.expenseai.ai.GemmaService
import com.expenseai.ai.ParsedDocument
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DocumentParserGemmaTest {

    private lateinit var gemmaService: GemmaService
    private lateinit var parser: DocumentParser

    private val loanText = """
        HDFC Bank Loan Statement
        EMI Amount 25,000.00
        Interest Rate 9.1%
        Outstanding Balance: 12,40,000.00
        Remaining Tenure: 62 months
    """.trimIndent()

    @Before
    fun setup() {
        gemmaService = mockk(relaxed = true)
        parser = DocumentParser(gemmaService)
    }

    @Test
    fun `gemma fields win over regex when present`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument(
            docType = "loan", lender = "HDFC Home Loans", emi = 25500.0
        )
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals("HDFC Home Loans", result.loan!!.lender)
        assertEquals(25500.0, result.loan!!.emi!!, 0.01)
        // regex fills what gemma left null
        assertEquals(9.1, result.loan!!.annualRatePct!!, 0.001)
        assertEquals(62, result.loan!!.remainingTenureMonths)
    }

    @Test
    fun `regex fallback used when gemma returns empty`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument()
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals(1240000.0, result.loan!!.outstandingPrincipal!!, 0.01)
        assertEquals(25000.0, result.loan!!.emi!!, 0.01)
    }

    @Test
    fun `unknown type still returns text preview and no fields`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument()
        val result = parser.parse("random text with nothing useful", DocType.UNKNOWN)
        assertEquals(DocType.UNKNOWN, result.type)
        assertNull(result.salary)
        assertNull(result.loan)
        assertTrue(result.textPreview.isNotBlank())
    }

    @Test
    fun `gemma emiStartDate string parses to LocalDate`() = runTest {
        coEvery { gemmaService.parseDocument(any()) } returns ParsedDocument(
            docType = "loan", sanctionedPrincipal = 8500000.0,
            emiStartDate = "2024-09-05", originalTenureMonths = 300
        )
        val result = parser.parse(loanText, DocType.LOAN_STATEMENT)
        assertEquals(java.time.LocalDate.of(2024, 9, 5), result.loan!!.emiStartDate)
    }
}
