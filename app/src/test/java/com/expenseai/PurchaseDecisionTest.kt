package com.expenseai

import com.expenseai.domain.fire.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class PurchaseDecisionTest {

    private val engine = FireEngine()
    private val start = LocalDate.of(2026, 1, 1)

    private fun model() = FireModel(
        profile = FireProfile(
            targetMode = TargetMode.EXPLICIT_TARGET,
            explicitTargetCorpus = 1_00_00_000.0,
            targetDate = start.plusYears(15),
            equityCagrPct = 12.0,
            startCorpus = 10_00_000.0,
            simStartDate = start
        ),
        incomes = listOf(
            IncomeStream(owner = "Self", label = "Salary", monthlyNet = 1_00_000.0, effectiveFrom = start)
        )
    )

    @Test
    fun `a large purchase delays the fire date`() {
        val d = engine.purchaseDecision(model(), 10_00_000.0, start)
        assertTrue(d.daysDelta > 0)
        assertNotNull(d.baseFireDate)
        assertNotNull(d.newFireDate)
        assertTrue(d.newFireDate!!.isAfter(d.baseFireDate!!))
    }

    @Test
    fun `future value exceeds the amount when fire date is in the future`() {
        val d = engine.purchaseDecision(model(), 1_00_000.0, start)
        assertTrue(d.futureValueAtFireDate > 1_00_000.0)
    }

    @Test
    fun `zero amount is a zero-day decision`() {
        val d = engine.purchaseDecision(model(), 0.0, start)
        assertEquals(0L, d.daysDelta)
        assertEquals(0.0, d.futureValueAtFireDate, 0.001)
    }

    @Test
    fun `fireDaysImpact matches purchaseDecision daysDelta`() {
        val amount = 5_00_000.0
        assertEquals(
            engine.purchaseDecision(model(), amount, start).daysDelta,
            engine.fireDaysImpact(model(), amount, start)
        )
    }

    @Test
    fun `purchase that pushes fire date beyond horizon yields null newFireDate`() {
        // model()'s base FIRE date is reached well within the 360-month horizon
        // (the "large purchase" test above confirms both base and new dates are
        // non-null for a 10L purchase). A purchase 10x the target corpus drives
        // the corpus deeply negative at the outset, so it never recovers to the
        // target within the simulated horizon.
        val hugeAmount = 10 * 1_00_00_000.0
        val d = engine.purchaseDecision(model(), hugeAmount, start)
        assertNotNull(d.baseFireDate)
        assertNull(d.newFireDate)
    }
}
