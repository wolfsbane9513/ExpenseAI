package com.expenseai.domain.fire

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class FireEngine(private val horizonMonths: Int = 360) {

    /** Income active in a given month, with annual growth applied from effectiveFrom. */
    private fun incomeForMonth(model: FireModel, month: LocalDate): Double =
        model.incomes.filter { s ->
            !month.isBefore(s.effectiveFrom) && (s.effectiveTo == null || !month.isAfter(s.effectiveTo))
        }.sumOf { s ->
            val years = FireMath.monthsBetween(s.effectiveFrom, month) / 12
            s.monthlyNet * (1.0 + s.annualGrowthPct / 100.0).pow(years)
        }

    private fun recurringForMonth(model: FireModel, month: LocalDate): Double =
        model.recurringOutflows.filter { o ->
            (o.startDate == null || !month.isBefore(o.startDate)) &&
                (o.endDate == null || !month.isAfter(o.endDate))
        }.sumOf { it.monthlyAmount }

    private fun emiForMonth(model: FireModel, month: LocalDate): Double =
        model.liabilities.filter { l ->
            !month.isBefore(l.emiStartDate) &&
                FireMath.monthsBetween(l.emiStartDate, month) < l.tenureMonths
        }.sumOf { FireMath.emi(it.principalAtEmiStart, it.annualRatePct, it.tenureMonths) }

    private fun investableForMonth(model: FireModel, month: LocalDate): Double =
        incomeForMonth(model, month) - recurringForMonth(model, month) - emiForMonth(model, month)

    private fun eventsForMonth(model: FireModel, month: LocalDate): Double =
        model.oneTimeEvents.filter {
            it.date.year == month.year && it.date.monthValue == month.monthValue
        }.sumOf { if (it.direction == EventDirection.OUTFLOW) -it.amount else it.amount }

    fun project(inputModel: FireModel): FireResult {
        val model = resolveScenarios(inputModel)
        val target = FireMath.targetCorpus(model.profile)
        val r = FireMath.monthlyRate(model.profile.equityCagrPct)
        val startDate = model.profile.simStartDate

        var corpus = model.profile.startCorpus
        var fireDate: LocalDate? = null
        val trajectory = ArrayList<TrajectoryPoint>(horizonMonths)

        for (i in 0 until horizonMonths) {
            val month = startDate.plusMonths(i.toLong())
            val investable = investableForMonth(model, month)
            corpus = corpus * (1.0 + r) + investable
            corpus += eventsForMonth(model, month)
            val nw = netWorthAt(model, month, corpus)
            trajectory.add(TrajectoryPoint(month, i, corpus, investable, nw.netWorth))
            if (fireDate == null && corpus >= target) fireDate = month
        }

        val targetDate = model.profile.targetDate
        val netWorthAtTarget = netWorthAt(model, targetDate, corpusAt(trajectory, targetDate, corpus))
        val phases = derivePhases(trajectory)
        val reframe = buildReframe(model, trajectory, target)
        val progress = if (target > 0) (model.profile.startCorpus / target) * 100.0 else 0.0

        return FireResult(
            fireDate = fireDate,
            targetCorpus = target,
            trajectory = trajectory,
            phases = phases,
            netWorthAtTarget = netWorthAtTarget,
            reframe = reframe,
            progressPercent = progress
        )
    }

    /** Merge enabled scenarios into the model as plain incomes/events. */
    internal fun resolveScenarios(model: FireModel): FireModel {
        val active = model.scenarios.filter { it.enabled && it.amount > 0.0 }
        if (active.isEmpty()) return model
        val start = model.profile.simStartDate
        val extraIncomes = active
            .filter { it.kind == ScenarioKind.MONTHLY_INCOME }
            .map {
                IncomeStream(
                    owner = "Scenario", label = it.label, monthlyNet = it.amount,
                    effectiveFrom = it.startDate ?: start, type = IncomeType.BUSINESS
                )
            }
        val extraEvents = active
            .filter { it.kind == ScenarioKind.ONE_TIME_INFLOW }
            .map { OneTimeEvent(it.label, it.amount, it.startDate ?: start, EventDirection.INFLOW) }
        return model.copy(
            incomes = model.incomes + extraIncomes,
            oneTimeEvents = model.oneTimeEvents + extraEvents
        )
    }

    private fun corpusAt(trajectory: List<TrajectoryPoint>, date: LocalDate, fallback: Double): Double =
        trajectory.lastOrNull { !it.date.isAfter(date) }?.corpus ?: fallback

    internal fun netWorthAt(model: FireModel, date: LocalDate, corpus: Double): NetWorthBreakdown {
        val assets = model.nonCorpusAssets.sumOf { a ->
            val years = FireMath.monthsBetween(model.profile.simStartDate, date) / 12
            a.value * (1.0 + a.annualGrowthPct / 100.0).pow(years)
        }
        val property = model.properties.sumOf { it.appreciatedValueAtTarget ?: it.currentValue }
        val loans = model.liabilities.sumOf { FireMath.outstandingBalance(it, date) }
        val nw = corpus + assets + property - loans
        return NetWorthBreakdown(date, corpus, assets, property, loans, nw)
    }

    private fun derivePhases(trajectory: List<TrajectoryPoint>): List<PhaseSummary> {
        if (trajectory.isEmpty()) return emptyList()
        val phases = ArrayList<PhaseSummary>()
        var phaseStartIdx = 0
        // Round investable to the nearest 1000 to ignore tiny growth drift.
        fun bucket(v: Double) = Math.round(v / 1000.0)

        for (i in 1..trajectory.size) {
            val boundary = i == trajectory.size ||
                bucket(trajectory[i].monthlyInvestable) != bucket(trajectory[phaseStartIdx].monthlyInvestable)
            if (boundary) {
                val startP = trajectory[phaseStartIdx]
                val endP = trajectory[i - 1]
                phases.add(
                    PhaseSummary(
                        label = "Phase ${phases.size + 1}",
                        startDate = startP.date,
                        endDate = endP.date,
                        months = i - phaseStartIdx,
                        representativeMonthlyInvestable = startP.monthlyInvestable,
                        corpusAtEnd = endP.corpus
                    )
                )
                phaseStartIdx = i
            }
        }
        return phases
    }

    private fun buildReframe(model: FireModel, trajectory: List<TrajectoryPoint>, target: Double): Reframe {
        val targetDate = model.profile.targetDate
        val r = FireMath.monthlyRate(model.profile.equityCagrPct)
        val atTarget = trajectory.lastOrNull { !it.date.isAfter(targetDate) }
        val corpusAtTargetDate = atTarget?.corpus ?: model.profile.startCorpus
        val monthsToTarget = FireMath.monthsBetween(model.profile.simStartDate, targetDate).coerceAtLeast(1)
        val reachable = corpusAtTargetDate >= target

        val gap = (target - corpusAtTargetDate).coerceAtLeast(0.0)
        // Future-value-of-annuity factor for the extra monthly investment needed.
        val annuityFactor = if (r == 0.0) monthsToTarget.toDouble()
            else (((1.0 + r).pow(monthsToTarget) - 1.0) / r)
        val requiredExtra = if (reachable) 0.0 else gap / annuityFactor
        val percentOfTarget = if (target > 0) corpusAtTargetDate / target * 100.0 else 0.0
        val dateReachable = trajectory.firstOrNull { it.corpus >= target }?.date

        val options = listOf(
            ReframeOption(
                ReframeOptionType.EXTEND_DATE,
                "Extend the date",
                dateReachable?.let { "Your current trajectory reaches the target around $it." }
                    ?: "The target is not reached within the simulated horizon."
            ),
            ReframeOption(
                ReframeOptionType.LOWER_TARGET,
                "Lower the target",
                "By $targetDate you can reach about ${money(corpusAtTargetDate)}; " +
                    "at 4% SWR that is ${money(corpusAtTargetDate * 0.04 / 12.0)}/mo perpetual."
            ),
            ReframeOption(
                ReframeOptionType.ADD_INCOME,
                "Add an income stream",
                "Investing an extra ${money(requiredExtra)}/mo from now closes the gap by $targetDate."
            )
        )

        return Reframe(
            targetReachableByTargetDate = reachable,
            corpusAtTargetDate = corpusAtTargetDate,
            gapToTarget = gap,
            percentOfTarget = percentOfTarget,
            requiredExtraMonthlyInvestment = requiredExtra,
            achievableTargetByTargetDate = corpusAtTargetDate,
            dateTargetIsReachable = dateReachable,
            options = options
        )
    }

    /** FIRE days delayed by spending [amount] (as a one-time outflow at [atDate]). */
    fun fireDaysImpact(model: FireModel, amount: Double, atDate: LocalDate): Long {
        if (amount <= 0.0) return 0L
        val baseDate = project(model).fireDate
        val withSpend = model.copy(
            oneTimeEvents = model.oneTimeEvents + OneTimeEvent(
                "purchase", amount, atDate, EventDirection.OUTFLOW
            )
        )
        val spendDate = project(withSpend).fireDate
        if (baseDate == null || spendDate == null) return 0L
        return ChronoUnit.DAYS.between(baseDate, spendDate)
    }

    private fun money(v: Double): String = "₹${Math.round(v)}"
}
