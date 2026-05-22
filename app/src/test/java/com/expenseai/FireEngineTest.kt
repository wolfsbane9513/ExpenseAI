package com.expenseai

import com.expenseai.domain.fire.EventDirection
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.IncomeStream
import com.expenseai.domain.fire.Liability
import com.expenseai.domain.fire.NonCorpusAsset
import com.expenseai.domain.fire.OneTimeEvent
import com.expenseai.domain.fire.PropertyHolding
import com.expenseai.domain.fire.RecurringOutflow
import com.expenseai.domain.fire.TargetMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class FireEngineTest {

    private val start = LocalDate.of(2026, 5, 1)

    /** Model with a constant ~5.11L/mo investable: 7.11L income - 2.00L outflow. */
    private fun constantModel(target: Double) = FireModel(
        profile = FireProfile(
            targetMode = TargetMode.EXPLICIT_TARGET,
            explicitTargetCorpus = target,
            targetDate = LocalDate.of(2031, 12, 31),
            equityCagrPct = 13.0,
            startCorpus = 9_000_000.0,
            simStartDate = start
        ),
        incomes = listOf(
            IncomeStream("household", "salary", 711_000.0, effectiveFrom = start)
        ),
        recurringOutflows = listOf(
            RecurringOutflow("fixed", 200_000.0, startDate = start)
        )
    )

    @Test fun `trajectory has one point per simulated month`() {
        val engine = FireEngine(horizonMonths = 24)
        val result = engine.project(constantModel(target = 1_000_000_000.0)) // unreachable
        assertEquals(24, result.trajectory.size)
        assertEquals(start, result.trajectory.first().date)
    }

    @Test fun `corpus after 24 months matches closed-form FV plus annuity`() {
        val engine = FireEngine(horizonMonths = 24)
        val result = engine.project(constantModel(target = 1_000_000_000.0))
        // C=90L grows at r=0.13/12; M=5.11L/mo added at month end for 24 months.
        // Expected ~ 2.56 Cr (see plan derivation).
        val corpus24 = result.trajectory.last().corpus
        assertTrue("corpus24=$corpus24", corpus24 in 25_000_000.0..26_500_000.0)
    }

    @Test fun `fireDate is set when target is reached within horizon`() {
        val engine = FireEngine(horizonMonths = 360)
        val result = engine.project(constantModel(target = 25_000_000.0)) // ~reached near 24mo
        assertNotNull(result.fireDate)
        assertTrue(result.fireDate!! >= start)
    }

    @Test fun `fireDate is null when target is not reached within horizon`() {
        val engine = FireEngine(horizonMonths = 24)
        val result = engine.project(constantModel(target = 1_000_000_000.0))
        assertEquals(null, result.fireDate)
    }

    @Test fun `a one-time outflow pushes the fire date later`() {
        val engine = FireEngine(horizonMonths = 360)
        val base = constantModel(target = 50_000_000.0)
        val withHit = base.copy(
            oneTimeEvents = listOf(
                OneTimeEvent(
                    "possession", 9_000_000.0,
                    LocalDate.of(2027, 1, 1),
                    EventDirection.OUTFLOW
                )
            )
        )
        val baseDate = engine.project(base).fireDate!!
        val hitDate = engine.project(withHit).fireDate!!
        assertTrue("hit=$hitDate base=$baseDate", hitDate.isAfter(baseDate))
    }

    @Test fun `net worth adds non-corpus assets and property and subtracts loan`() {
        val engine = FireEngine(horizonMonths = 1)
        val model = constantModel(target = 1_000_000_000.0).copy(
            nonCorpusAssets = listOf(NonCorpusAsset("EPF", 5_500_000.0)),
            properties = listOf(
                PropertyHolding(
                    "Flat", currentValue = 38_000_000.0,
                    appreciatedValueAtTarget = 38_000_000.0,
                    linkedLiabilityName = "SBI"
                )
            ),
            liabilities = listOf(
                Liability("SBI", 20_000_000.0, 7.55, 240, LocalDate.of(2028, 7, 1))
            )
        )
        // Before EMI start the SBI outstanding equals full principal (2 Cr).
        val nw = engine.netWorthAt(model, LocalDate.of(2027, 1, 1), corpus = 10_000_000.0)
        // 1Cr corpus + 55L EPF + 3.8Cr flat - 2Cr loan = 3.35 Cr
        assertEquals(33_500_000.0, nw.netWorth, 1.0)
        assertEquals(20_000_000.0, nw.loanOutstanding, 1.0)
    }

    @Test fun `phases split where the monthly investable changes`() {
        val engine = FireEngine(horizonMonths = 36)
        // Investable jumps when an EMI begins at month 13.
        val model = constantModel(target = 1_000_000_000.0).copy(
            liabilities = listOf(
                Liability(
                    "SBI", 10_000_000.0, 7.55, 240,
                    emiStartDate = start.plusMonths(12)
                )
            )
        )
        val phases = engine.project(model).phases
        assertTrue("expected >=2 phases, got ${phases.size}", phases.size >= 2)
        // The first phase ends the month before the EMI starts.
        assertEquals(start.plusMonths(11), phases.first().endDate)
    }

    @Test fun `reframe reports reachable when target is hit by target date`() {
        val engine = FireEngine(horizonMonths = 360)
        val result = engine.project(constantModel(target = 25_000_000.0))
        assertTrue(result.reframe.targetReachableByTargetDate)
        assertEquals(0.0, result.reframe.gapToTarget, 1.0)
        assertEquals(0.0, result.reframe.requiredExtraMonthlyInvestment, 1.0)
    }

    @Test fun `reframe reports gap and required extra investment when unreachable`() {
        val engine = FireEngine(horizonMonths = 360)
        // 30 Cr by 2031 is unreachable on this cashflow (the War Room's real finding).
        val result = engine.project(constantModel(target = 300_000_000.0))
        val rf = result.reframe
        assertTrue(!rf.targetReachableByTargetDate)
        assertTrue("gap=${rf.gapToTarget}", rf.gapToTarget > 0.0)
        assertTrue("extra=${rf.requiredExtraMonthlyInvestment}", rf.requiredExtraMonthlyInvestment > 0.0)
        assertTrue("achievable=${rf.achievableTargetByTargetDate}", rf.achievableTargetByTargetDate > 0.0)
        assertEquals(3, rf.options.size)
    }

    @Test fun `fireDaysImpact is positive for a spend that delays FIRE`() {
        val engine = FireEngine(horizonMonths = 360)
        val model = constantModel(target = 50_000_000.0)
        val days = engine.fireDaysImpact(model, amount = 850_000.0, atDate = start)
        assertTrue("days=$days", days > 0)
    }

    @Test fun `fireDaysImpact is zero for a zero spend`() {
        val engine = FireEngine(horizonMonths = 360)
        val model = constantModel(target = 50_000_000.0)
        assertEquals(0L, engine.fireDaysImpact(model, amount = 0.0, atDate = start))
    }
}
