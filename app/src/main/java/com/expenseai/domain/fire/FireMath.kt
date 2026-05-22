package com.expenseai.domain.fire

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.pow

/** Pure financial helpers. All amounts in rupees. */
object FireMath {

    /** Monthly rate using the SIP convention (annual / 12), matching the source War Room. */
    fun monthlyRate(annualCagrPct: Double): Double = annualCagrPct / 100.0 / 12.0

    /** The corpus the user is aiming for. */
    fun targetCorpus(profile: FireProfile): Double = when (profile.targetMode) {
        TargetMode.ANNUAL_EXPENSES_25X -> profile.annualRetirementExpenses * 25.0
        TargetMode.EXPLICIT_TARGET -> profile.explicitTargetCorpus
    }

    /** Standard EMI for a fully amortizing loan. Returns 0 for non-positive inputs. */
    fun emi(principal: Double, annualRatePct: Double, tenureMonths: Int): Double {
        if (principal <= 0.0 || tenureMonths <= 0) return 0.0
        val r = annualRatePct / 100.0 / 12.0
        if (r == 0.0) return principal / tenureMonths
        val f = (1.0 + r).pow(tenureMonths)
        return principal * r * f / (f - 1.0)
    }

    /** Months elapsed from [from] to [to], floored at 0. */
    fun monthsBetween(from: LocalDate, to: LocalDate): Int {
        val m = ChronoUnit.MONTHS.between(from.withDayOfMonth(1), to.withDayOfMonth(1))
        return if (m < 0) 0 else m.toInt()
    }

    /** Declining-balance outstanding principal of [liability] at [date]. */
    fun outstandingBalance(liability: Liability, date: LocalDate): Double {
        if (date.isBefore(liability.emiStartDate)) return liability.principalAtEmiStart
        val paid = monthsBetween(liability.emiStartDate, date).coerceAtMost(liability.tenureMonths)
        val r = liability.annualRatePct / 100.0 / 12.0
        val e = emi(liability.principalAtEmiStart, liability.annualRatePct, liability.tenureMonths)
        var bal = liability.principalAtEmiStart
        repeat(paid) {
            val interest = bal * r
            bal = (bal + interest - e).coerceAtLeast(0.0)
        }
        return bal
    }
}
