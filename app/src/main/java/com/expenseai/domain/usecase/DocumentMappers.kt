package com.expenseai.domain.usecase

import com.expenseai.ai.LoanFields
import com.expenseai.ai.SalaryFields
import com.expenseai.domain.fire.IncomeStream
import com.expenseai.domain.fire.Liability
import java.time.LocalDate

/** True when the exact sanction-time details were extracted (spec §5, mapping rule 1). */
val LoanFields.usesSanctionDetails: Boolean
    get() = sanctionedPrincipal != null && emiStartDate != null && originalTenureMonths != null

fun SalaryFields.toIncomeStream(today: LocalDate = LocalDate.now()): IncomeStream = IncomeStream(
    owner = "Self",
    label = employer?.takeIf { it.isNotBlank() } ?: "Salary",
    monthlyNet = monthlyNet ?: 0.0,
    effectiveFrom = today
)

fun LoanFields.toLiability(today: LocalDate = LocalDate.now()): Liability =
    if (usesSanctionDetails) {
        Liability(
            name = lender?.takeIf { it.isNotBlank() } ?: "Loan",
            principalAtEmiStart = sanctionedPrincipal!!,
            annualRatePct = annualRatePct ?: 0.0,
            tenureMonths = originalTenureMonths!!,
            emiStartDate = emiStartDate!!
        )
    } else {
        Liability(
            name = lender?.takeIf { it.isNotBlank() } ?: "Loan",
            principalAtEmiStart = outstandingPrincipal ?: 0.0,
            annualRatePct = annualRatePct ?: 0.0,
            tenureMonths = remainingTenureMonths ?: 0,
            emiStartDate = today
        )
    }
