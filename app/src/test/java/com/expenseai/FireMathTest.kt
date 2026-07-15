package com.expenseai

import com.expenseai.domain.fire.FireMath
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.Liability
import com.expenseai.domain.fire.TargetMode
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class FireMathTest {

    @Test fun `FireModel can be constructed with a minimal profile`() {
        val model = FireModel(
            profile = FireProfile(
                targetMode = TargetMode.EXPLICIT_TARGET,
                explicitTargetCorpus = 300_000_000.0,
                targetDate = LocalDate.of(2031, 12, 31),
                equityCagrPct = 13.0,
                startCorpus = 9_000_000.0,
                simStartDate = LocalDate.of(2026, 5, 1)
            )
        )
        assertEquals(13.0, model.profile.equityCagrPct, 0.0)
        assertEquals(0, model.incomes.size)
    }

    @Test fun `monthlyRate divides annual cagr by 12`() {
        // 13% annual -> 0.13/12 monthly (SIP convention used by the War Room)
        assertEquals(0.0108333, FireMath.monthlyRate(13.0), 1e-6)
    }

    @Test fun `targetCorpus uses 25x for ANNUAL_EXPENSES_25X mode`() {
        val profile = FireProfile(
            targetMode = TargetMode.ANNUAL_EXPENSES_25X,
            annualRetirementExpenses = 2_400_000.0, // 24L/yr
            targetDate = LocalDate.of(2031, 12, 31),
            equityCagrPct = 13.0,
            startCorpus = 0.0,
            simStartDate = LocalDate.of(2026, 5, 1)
        )
        assertEquals(60_000_000.0, FireMath.targetCorpus(profile), 0.01) // 6 Cr
    }

    @Test fun `targetCorpus uses explicit value for EXPLICIT_TARGET mode`() {
        val profile = FireProfile(
            targetMode = TargetMode.EXPLICIT_TARGET,
            explicitTargetCorpus = 300_000_000.0,
            targetDate = LocalDate.of(2031, 12, 31),
            equityCagrPct = 13.0,
            startCorpus = 0.0,
            simStartDate = LocalDate.of(2026, 5, 1)
        )
        assertEquals(300_000_000.0, FireMath.targetCorpus(profile), 0.01)
    }

    @Test fun `emi matches the standard amortization formula`() {
        // 10,00,000 @ 12% over 120 months -> well-known ~14347.09
        assertEquals(14_347.09, FireMath.emi(1_000_000.0, 12.0, 120), 0.5)
    }

    @Test fun `emi is zero for zero or negative principal`() {
        assertEquals(0.0, FireMath.emi(0.0, 12.0, 120), 0.0)
    }

    private fun loan() = Liability(
        name = "Test loan",
        principalAtEmiStart = 1_000_000.0,
        annualRatePct = 12.0,
        tenureMonths = 120,
        emiStartDate = LocalDate.of(2028, 7, 1)
    )

    @Test fun `outstanding equals full principal before emi starts`() {
        val bal = FireMath.outstandingBalance(loan(), LocalDate.of(2027, 1, 1))
        assertEquals(1_000_000.0, bal, 0.01)
    }

    @Test fun `outstanding equals full principal at the emi start month`() {
        val bal = FireMath.outstandingBalance(loan(), LocalDate.of(2028, 7, 1))
        assertEquals(1_000_000.0, bal, 0.01)
    }

    @Test fun `outstanding decreases after some emis are paid`() {
        // After 12 EMIs the balance should be below principal but still substantial.
        val bal = FireMath.outstandingBalance(loan(), LocalDate.of(2029, 7, 1))
        assertEquals(true, bal in 880_000.0..960_000.0)
    }

    @Test fun `outstanding is zero at or after tenure end`() {
        val bal = FireMath.outstandingBalance(loan(), LocalDate.of(2038, 7, 1))
        assertEquals(0.0, bal, 1.0)
    }
}
