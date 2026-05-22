package com.expenseai.domain.fire

import java.time.LocalDate

enum class TargetMode { ANNUAL_EXPENSES_25X, EXPLICIT_TARGET }
enum class IncomeType { SALARY, BUSINESS, PASSIVE }
enum class EventDirection { INFLOW, OUTFLOW }

data class FireProfile(
    val targetMode: TargetMode,
    val annualRetirementExpenses: Double = 0.0,
    val explicitTargetCorpus: Double = 0.0,
    val targetDate: LocalDate,
    val equityCagrPct: Double,
    val inflationPct: Double = 0.0,
    val startCorpus: Double,
    val simStartDate: LocalDate
)

data class IncomeStream(
    val owner: String,
    val label: String,
    val monthlyNet: Double,
    val effectiveFrom: LocalDate,
    val effectiveTo: LocalDate? = null,
    val type: IncomeType = IncomeType.SALARY,
    val annualGrowthPct: Double = 0.0
)

data class Liability(
    val name: String,
    val principalAtEmiStart: Double,
    val annualRatePct: Double,
    val tenureMonths: Int,
    val emiStartDate: LocalDate
)

data class RecurringOutflow(
    val label: String,
    val monthlyAmount: Double,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val category: String = "general"
)

data class OneTimeEvent(
    val label: String,
    val amount: Double,
    val date: LocalDate,
    val direction: EventDirection
)

data class NonCorpusAsset(
    val label: String,
    val value: Double,
    val annualGrowthPct: Double = 0.0
)

data class PropertyHolding(
    val name: String,
    val currentValue: Double,
    val appreciatedValueAtTarget: Double? = null,
    val possessionDate: LocalDate? = null,
    val linkedLiabilityName: String? = null
)

data class FireModel(
    val profile: FireProfile,
    val incomes: List<IncomeStream> = emptyList(),
    val liabilities: List<Liability> = emptyList(),
    val recurringOutflows: List<RecurringOutflow> = emptyList(),
    val oneTimeEvents: List<OneTimeEvent> = emptyList(),
    val nonCorpusAssets: List<NonCorpusAsset> = emptyList(),
    val properties: List<PropertyHolding> = emptyList()
)
