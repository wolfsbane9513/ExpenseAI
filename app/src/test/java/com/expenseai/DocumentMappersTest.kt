package com.expenseai

import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.domain.usecase.toIncomeStream
import com.expenseai.domain.usecase.toLiability
import com.expenseai.domain.usecase.usesSanctionDetails
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class DocumentMappersTest {

    private val today = LocalDate.of(2026, 7, 16)

    @Test
    fun `salary maps to income stream`() {
        val stream = SalaryFields(employer = "TransmuteLabs", monthlyNet = 150000.0)
            .toIncomeStream(today)
        assertEquals("TransmuteLabs", stream.label)
        assertEquals(150000.0, stream.monthlyNet, 0.01)
        assertEquals(today, stream.effectiveFrom)
        assertEquals("Self", stream.owner)
    }

    @Test
    fun `loan with sanction details maps exactly`() {
        val fields = LoanFields(
            lender = "SBI", emi = 68444.0, annualRatePct = 8.5,
            sanctionedPrincipal = 8500000.0,
            emiStartDate = LocalDate.of(2024, 9, 5),
            originalTenureMonths = 300,
            outstandingPrincipal = 8210550.0, remainingTenureMonths = 278
        )
        assertTrue(fields.usesSanctionDetails)
        val liability = fields.toLiability(today)
        assertEquals(8500000.0, liability.principalAtEmiStart, 0.01)
        assertEquals(300, liability.tenureMonths)
        assertEquals(LocalDate.of(2024, 9, 5), liability.emiStartDate)
        assertEquals(8.5, liability.annualRatePct, 0.001)
    }

    @Test
    fun `loan without sanction details falls back to outstanding from today`() {
        val fields = LoanFields(
            lender = "HDFC", emi = 25000.0, annualRatePct = 9.1,
            outstandingPrincipal = 1240000.0, remainingTenureMonths = 62
        )
        assertFalse(fields.usesSanctionDetails)
        val liability = fields.toLiability(today)
        assertEquals(1240000.0, liability.principalAtEmiStart, 0.01)
        assertEquals(62, liability.tenureMonths)
        assertEquals(today, liability.emiStartDate)
    }

    @Test
    fun `missing lender defaults to Loan`() {
        val liability = LoanFields(outstandingPrincipal = 100000.0, remainingTenureMonths = 12)
            .toLiability(today)
        assertEquals("Loan", liability.name)
    }
}
