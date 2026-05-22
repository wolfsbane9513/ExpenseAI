package com.expenseai.domain.fire

import java.time.LocalDate

data class TrajectoryPoint(
    val date: LocalDate,
    val monthIndex: Int,
    val corpus: Double,
    val monthlyInvestable: Double,
    val netWorth: Double
)

data class PhaseSummary(
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val months: Int,
    val representativeMonthlyInvestable: Double,
    val corpusAtEnd: Double
)

data class NetWorthBreakdown(
    val date: LocalDate,
    val liquidCorpus: Double,
    val nonCorpusAssets: Double,
    val propertyValue: Double,
    val loanOutstanding: Double,
    val netWorth: Double
)

enum class ReframeOptionType { EXTEND_DATE, LOWER_TARGET, ADD_INCOME }

data class ReframeOption(
    val type: ReframeOptionType,
    val headline: String,
    val detail: String
)

data class Reframe(
    val targetReachableByTargetDate: Boolean,
    val corpusAtTargetDate: Double,
    val gapToTarget: Double,
    val percentOfTarget: Double,
    val requiredExtraMonthlyInvestment: Double,
    val achievableTargetByTargetDate: Double,
    val dateTargetIsReachable: LocalDate?,
    val options: List<ReframeOption>
)

data class FireResult(
    val fireDate: LocalDate?,
    val targetCorpus: Double,
    val trajectory: List<TrajectoryPoint>,
    val phases: List<PhaseSummary>,
    val netWorthAtTarget: NetWorthBreakdown,
    val reframe: Reframe,
    val progressPercent: Double
)
