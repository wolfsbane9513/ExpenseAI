package com.expenseai

import com.expenseai.ai.DocType
import com.expenseai.ai.DocumentParser
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class DocumentParserTest {

    private val parser = DocumentParser(mockk(relaxed = true))

    private val salarySlipText = """
        TransmuteLabs Private Limited
        Payslip for the month of June 2026
        Employee Name: R Prakash
        Basic Pay 1,20,000.00
        HRA 48,000.00
        Gross Pay 1,95,000.00
        Deductions 45,000.00
        Net Pay 1,50,000.00
    """.trimIndent()

    private val loanSanctionText = """
        State Bank of India
        Home Loan Account Statement
        Loan Account No: 000123456789
        Sanctioned Amount: 85,00,000.00
        Rate of Interest: 8.50 %
        Tenure: 300 months
        First EMI Date: 05/09/2024
        EMI: 68,444.00
        Outstanding Principal: 82,10,550.00
    """.trimIndent()

    private val loanOutstandingOnlyText = """
        HDFC Bank Loan Statement
        EMI Amount 25,000.00
        Interest Rate 9.1%
        Outstanding Balance: 12,40,000.00
        Remaining Tenure: 62 months
    """.trimIndent()

    private val randomText = "Your OTP for login is 482913. Do not share it with anyone."

    @Test
    fun `classifies salary slip`() {
        assertEquals(DocType.SALARY_SLIP, parser.classify(salarySlipText))
    }

    @Test
    fun `classifies loan statement`() {
        assertEquals(DocType.LOAN_STATEMENT, parser.classify(loanSanctionText))
    }

    @Test
    fun `unrelated text classifies as unknown`() {
        assertEquals(DocType.UNKNOWN, parser.classify(randomText))
    }

    @Test
    fun `extracts net pay and employer from salary slip`() {
        val fields = parser.extractSalary(salarySlipText)
        assertEquals(150000.0, fields.monthlyNet!!, 0.01)
        assertEquals("TransmuteLabs Private Limited", fields.employer)
    }

    @Test
    fun `extracts sanction details from loan statement`() {
        val fields = parser.extractLoan(loanSanctionText)
        assertEquals(8500000.0, fields.sanctionedPrincipal!!, 0.01)
        assertEquals(8.5, fields.annualRatePct!!, 0.001)
        assertEquals(300, fields.originalTenureMonths)
        assertEquals(LocalDate.of(2024, 9, 5), fields.emiStartDate)
        assertEquals(68444.0, fields.emi!!, 0.01)
        assertEquals(8210550.0, fields.outstandingPrincipal!!, 0.01)
        assertEquals("State Bank of India", fields.lender)
    }

    @Test
    fun `extracts outstanding fallback fields when no sanction details`() {
        val fields = parser.extractLoan(loanOutstandingOnlyText)
        assertNull(fields.sanctionedPrincipal)
        assertNull(fields.emiStartDate)
        assertEquals(1240000.0, fields.outstandingPrincipal!!, 0.01)
        assertEquals(62, fields.remainingTenureMonths)
        assertEquals(25000.0, fields.emi!!, 0.01)
        assertEquals(9.1, fields.annualRatePct!!, 0.001)
    }

    @Test
    fun `tenure in years converts to months`() {
        val fields = parser.extractLoan("Loan Sanctioned Amount 50,00,000 Tenure: 25 years EMI 40,000")
        assertEquals(300, fields.originalTenureMonths)
    }

    @Test
    fun `sanitizeDocumentText keeps long documents`() {
        val long = "a".repeat(10_000)
        assertEquals(10_000, com.expenseai.security.InputSanitizer.sanitizeDocumentText(long).length)
    }
}
