package com.expenseai

import com.expenseai.domain.fire.EventDirection
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.IncomeStream
import com.expenseai.domain.fire.OneTimeEvent
import com.expenseai.domain.fire.RecurringOutflow
import com.expenseai.domain.fire.TargetMode
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class FireEngineWarRoomTest {

    private val start = LocalDate.of(2026, 7, 1)

    // Phase 1: Jul 2026 -> Jun 2028, ~5.11L/mo investable (7.11L income - 2.00L outflow).
    private fun warRoomModel() = FireModel(
        profile = FireProfile(
            targetMode = TargetMode.EXPLICIT_TARGET,
            explicitTargetCorpus = 300_000_000.0, // ₹30 Cr
            targetDate = LocalDate.of(2031, 12, 31),
            equityCagrPct = 13.0,
            startCorpus = 9_000_000.0, // ₹90L
            simStartDate = start
        ),
        incomes = listOf(
            IncomeStream("household", "salary", 711_000.0, effectiveFrom = start,
                effectiveTo = LocalDate.of(2028, 6, 1)),
            IncomeStream("household", "salary +10%", 782_000.0,
                effectiveFrom = LocalDate.of(2028, 7, 1),
                effectiveTo = LocalDate.of(2030, 5, 1)),
            IncomeStream("household", "salary +12%", 876_000.0,
                effectiveFrom = LocalDate.of(2030, 6, 1))
        ),
        // One outflow per phase, sized so net investable matches the War Room:
        // P1 711k-200k=511k, P2 782k-374k=408k, P3 876k-324k=552k.
        recurringOutflows = listOf(
            RecurringOutflow("outflow-p1", 200_000.0, startDate = start,
                endDate = LocalDate.of(2028, 6, 1)),
            RecurringOutflow("outflow-p2", 374_000.0,
                startDate = LocalDate.of(2028, 7, 1),
                endDate = LocalDate.of(2030, 5, 1)),
            RecurringOutflow("outflow-p3", 324_000.0,
                startDate = LocalDate.of(2030, 6, 1))
        ),
        oneTimeEvents = listOf(
            OneTimeEvent("possession own funds", 9_000_000.0,
                LocalDate.of(2030, 5, 1), EventDirection.OUTFLOW)
        )
    )

    @Test fun `corpus at end of phase 1 is about 2_57 Cr`() {
        val engine = FireEngine(horizonMonths = 360)
        val traj = engine.project(warRoomModel()).trajectory
        // Jun 2028 == month index 23 (Jul 2026 + 23 months).
        val jun2028 = traj.first { it.date == LocalDate.of(2028, 6, 1) }
        assertTrue("phase1 corpus=${jun2028.corpus}", jun2028.corpus in 24_000_000.0..27_500_000.0)
    }

    @Test fun `base corpus at Dec 2031 is in the 5 to 6_2 Cr band`() {
        val engine = FireEngine(horizonMonths = 360)
        val traj = engine.project(warRoomModel()).trajectory
        val dec2031 = traj.first { it.date == LocalDate.of(2031, 12, 1) }
        assertTrue("dec2031 corpus=${dec2031.corpus}", dec2031.corpus in 50_000_000.0..62_000_000.0)
    }

    @Test fun `30 Cr by 2031 is reported unreachable`() {
        val engine = FireEngine(horizonMonths = 360)
        val rf = engine.project(warRoomModel()).reframe
        assertTrue(!rf.targetReachableByTargetDate)
        // Headline: about a quarter of the target.
        assertTrue("pct=${rf.percentOfTarget}", rf.percentOfTarget in 15.0..35.0)
    }
}
