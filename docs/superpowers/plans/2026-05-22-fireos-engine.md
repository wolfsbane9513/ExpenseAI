# FIRE OS — FIRE Engine Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the pure-Kotlin FIRE projection engine and its domain model — a fully unit-tested library that turns a `FireModel` into a `FireResult` (FIRE date, corpus trajectory, net worth, and the honest reframe), reproducing the War Room's math.

**Architecture:** A dependency-free domain package `com.expenseai.domain.fire`. Plain Kotlin data classes describe the model and result; `FireEngine` runs a month-by-month simulation. No Android, Room, or Compose dependencies — so it runs as a plain JVM unit test. Persistence (Room) and UI (Compose War Room) are separate follow-on plans that consume this engine.

**Tech Stack:** Kotlin, `java.time.LocalDate`, JUnit 4 (`org.junit`), Gradle unit tests (`testDebugUnitTest`).

---

## ⚠ Environment note (read once)

Java/Gradle are **not available in WSL**. Every `./gradlew ...` command in this plan must
be run from **Android Studio's terminal on Windows** (or via the Android Studio test
runner). When executing this plan from WSL, the code can be written here, but the
"run the test" verification steps must be executed on Windows — surface the command to
the user and wait for the pass/fail result before proceeding.

Money is represented as `Double` in **rupees** (matching the existing codebase, e.g.
`Expense.amount`). The engine uses **monthly rate = annualCagrPct / 100 / 12** (the SIP
convention used by the source War Room), not a compounded 12th-root rate.

---

## File Structure

- Create: `app/src/main/java/com/expenseai/domain/fire/FireModel.kt` — input data classes + enums.
- Create: `app/src/main/java/com/expenseai/domain/fire/FireResult.kt` — output data classes + enums.
- Create: `app/src/main/java/com/expenseai/domain/fire/FireMath.kt` — pure helper functions (rate, target, EMI, outstanding balance).
- Create: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` — the simulation engine.
- Test: `app/src/test/java/com/expenseai/FireMathTest.kt`
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt`
- Test: `app/src/test/java/com/expenseai/FireEngineWarRoomTest.kt`

---

## Task 1: Domain model types

**Files:**
- Create: `app/src/main/java/com/expenseai/domain/fire/FireModel.kt`
- Create: `app/src/main/java/com/expenseai/domain/fire/FireResult.kt`
- Test: `app/src/test/java/com/expenseai/FireMathTest.kt` (model-construction smoke test only in this task)

- [ ] **Step 1: Write the failing test**

Create `app/src/test/java/com/expenseai/FireMathTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
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
}
```

- [ ] **Step 2: Run test to verify it fails**

Run (Windows / Android Studio terminal): `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: FAIL — `FireModel` / `FireProfile` unresolved (compile error).

- [ ] **Step 3: Write the model types**

Create `app/src/main/java/com/expenseai/domain/fire/FireModel.kt`:

```kotlin
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
```

Create `app/src/main/java/com/expenseai/domain/fire/FireResult.kt`:

```kotlin
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireModel.kt \
        app/src/main/java/com/expenseai/domain/fire/FireResult.kt \
        app/src/test/java/com/expenseai/FireMathTest.kt
git commit -m "feat(fire): add FIRE engine domain model + result types"
```

---

## Task 2: Financial math helpers — monthly rate, target corpus, EMI

**Files:**
- Create: `app/src/main/java/com/expenseai/domain/fire/FireMath.kt`
- Test: `app/src/test/java/com/expenseai/FireMathTest.kt` (add tests)

- [ ] **Step 1: Write the failing tests**

Add to `app/src/test/java/com/expenseai/FireMathTest.kt` (add imports at top:
`import com.expenseai.domain.fire.FireMath` and the model imports already present):

```kotlin
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
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: FAIL — `FireMath` unresolved.

- [ ] **Step 3: Write the helpers**

Create `app/src/main/java/com/expenseai/domain/fire/FireMath.kt`:

```kotlin
package com.expenseai.domain.fire

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
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: PASS (all 6 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireMath.kt \
        app/src/test/java/com/expenseai/FireMathTest.kt
git commit -m "feat(fire): add monthly-rate, target-corpus, and EMI helpers"
```

---

## Task 3: Loan outstanding balance at a date

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireMath.kt`
- Test: `app/src/test/java/com/expenseai/FireMathTest.kt` (add tests)

- [ ] **Step 1: Write the failing tests**

Add to `FireMathTest.kt` (add import `import com.expenseai.domain.fire.Liability`):

```kotlin
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
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: FAIL — `outstandingBalance` unresolved.

- [ ] **Step 3: Implement `outstandingBalance`**

Add to `FireMath` in `FireMath.kt` (add `import java.time.LocalDate` and
`import java.time.temporal.ChronoUnit` at top of the file):

```kotlin
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
```

Add the imports at the top of `FireMath.kt`:

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireMathTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireMath.kt \
        app/src/test/java/com/expenseai/FireMathTest.kt
git commit -m "feat(fire): add loan outstanding-balance amortization helper"
```

---

## Task 4: Core projection — trajectory and FIRE date

**Files:**
- Create: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt`
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt`

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/FireEngineTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.IncomeStream
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
                com.expenseai.domain.fire.OneTimeEvent(
                    "possession", 9_000_000.0,
                    LocalDate.of(2027, 1, 1),
                    com.expenseai.domain.fire.EventDirection.OUTFLOW
                )
            )
        )
        val baseDate = engine.project(base).fireDate!!
        val hitDate = engine.project(withHit).fireDate!!
        assertTrue("hit=$hitDate base=$baseDate", hitDate.isAfter(baseDate))
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: FAIL — `FireEngine` unresolved.

- [ ] **Step 3: Implement the core projection**

Create `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt`:

```kotlin
package com.expenseai.domain.fire

import java.time.LocalDate
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

    fun project(model: FireModel): FireResult {
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

        val netWorthAtTarget = netWorthAt(model, model.profile.targetDate, corpusAt(trajectory, model.profile.targetDate, corpus))
        val phases = derivePhases(model, trajectory)
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

    private fun corpusAt(trajectory: List<TrajectoryPoint>, date: LocalDate, fallback: Double): Double =
        trajectory.lastOrNull { !it.date.isAfter(date) }?.corpus ?: fallback

    // netWorthAt, derivePhases, buildReframe are added in Tasks 5-7.
    internal fun netWorthAt(model: FireModel, date: LocalDate, corpus: Double): NetWorthBreakdown =
        NetWorthBreakdown(date, corpus, 0.0, 0.0, 0.0, corpus)

    private fun derivePhases(model: FireModel, trajectory: List<TrajectoryPoint>): List<PhaseSummary> =
        emptyList()

    private fun buildReframe(model: FireModel, trajectory: List<TrajectoryPoint>, target: Double): Reframe =
        Reframe(false, 0.0, 0.0, 0.0, 0.0, 0.0, null, emptyList())
}
```

> Note: `netWorthAt`, `derivePhases`, and `buildReframe` are stubbed here so the core
> loop compiles and its tests pass. Tasks 5–7 replace each stub and add their tests.

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: PASS (all 5 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireEngine.kt \
        app/src/test/java/com/expenseai/FireEngineTest.kt
git commit -m "feat(fire): add core month-by-month projection and FIRE date"
```

---

## Task 5: Net worth at a date

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (replace `netWorthAt` stub)
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt` (add tests)

- [ ] **Step 1: Write the failing test**

Add to `FireEngineTest.kt` (add imports `com.expenseai.domain.fire.Liability`,
`com.expenseai.domain.fire.NonCorpusAsset`, `com.expenseai.domain.fire.PropertyHolding`):

```kotlin
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: FAIL — assertion (stub returns corpus-only net worth).

- [ ] **Step 3: Replace the `netWorthAt` stub**

In `FireEngine.kt`, replace the stubbed `netWorthAt` with:

```kotlin
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireEngine.kt \
        app/src/test/java/com/expenseai/FireEngineTest.kt
git commit -m "feat(fire): compute net worth (corpus + assets + property - loans)"
```

---

## Task 6: Phase derivation

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (replace `derivePhases` stub)
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt` (add test)

- [ ] **Step 1: Write the failing test**

Add to `FireEngineTest.kt`:

```kotlin
    @Test fun `phases split where the monthly investable changes`() {
        val engine = FireEngine(horizonMonths = 36)
        // Investable jumps when an EMI begins at month 13.
        val model = constantModel(target = 1_000_000_000.0).copy(
            liabilities = listOf(
                com.expenseai.domain.fire.Liability(
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: FAIL — stub returns empty list.

- [ ] **Step 3: Replace the `derivePhases` stub**

In `FireEngine.kt`, replace the stubbed `derivePhases` with:

```kotlin
    private fun derivePhases(model: FireModel, trajectory: List<TrajectoryPoint>): List<PhaseSummary> {
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireEngine.kt \
        app/src/test/java/com/expenseai/FireEngineTest.kt
git commit -m "feat(fire): derive cashflow phases from investable changes"
```

---

## Task 7: The honest reframe

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (replace `buildReframe` stub)
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt` (add tests)

- [ ] **Step 1: Write the failing tests**

Add to `FireEngineTest.kt`:

```kotlin
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
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: FAIL — stub returns an all-zero reframe.

- [ ] **Step 3: Replace the `buildReframe` stub**

In `FireEngine.kt`, replace the stubbed `buildReframe` with:

```kotlin
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

    private fun money(v: Double): String = "₹${Math.round(v)}"
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireEngine.kt \
        app/src/test/java/com/expenseai/FireEngineTest.kt
git commit -m "feat(fire): add honest reframe (gap, required extra, options A/B/C)"
```

---

## Task 8: Purchase / expense FIRE-day impact

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (add `fireDaysImpact`)
- Test: `app/src/test/java/com/expenseai/FireEngineTest.kt` (add tests)

- [ ] **Step 1: Write the failing tests**

Add to `FireEngineTest.kt`:

```kotlin
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
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: FAIL — `fireDaysImpact` unresolved.

- [ ] **Step 3: Implement `fireDaysImpact`**

Add to `FireEngine` in `FireEngine.kt` (add `import java.time.temporal.ChronoUnit`):

```kotlin
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
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/FireEngine.kt \
        app/src/test/java/com/expenseai/FireEngineTest.kt
git commit -m "feat(fire): add purchase FIRE-day impact helper"
```

---

## Task 9: War Room regression fixture

**Files:**
- Test: `app/src/test/java/com/expenseai/FireEngineWarRoomTest.kt`

This locks the engine to the source War Room's headline figures so future changes can't
silently drift. The model encodes the same inputs; numbers are **test fixtures, never
shipped as app defaults**.

- [ ] **Step 1: Write the failing test**

Create `app/src/test/java/com/expenseai/FireEngineWarRoomTest.kt`:

```kotlin
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
```

- [ ] **Step 2: Run test to verify it fails or passes**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngineWarRoomTest"`
Expected: PASS (the engine is already implemented). If a band assertion fails, do **not**
loosen it blindly — first confirm the model inputs above match the War Room; the bands are
intentionally generous to absorb phase-boundary rounding. Adjust only the model inputs to
match the source, not the engine, unless a genuine engine bug is found.

- [ ] **Step 3: (only if a real bug surfaces) fix the engine**

If Step 2 reveals an engine error (not a fixture mismatch), fix it in `FireEngine.kt`
and re-run all engine tests:

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.FireEngine*"`
Expected: PASS.

- [ ] **Step 4: Run the full engine suite**

Run: `./gradlew :app:testDebugUnitTest --tests "com.expenseai.Fire*"`
Expected: PASS (FireMathTest, FireEngineTest, FireEngineWarRoomTest).

- [ ] **Step 5: Commit**

```bash
git add app/src/test/java/com/expenseai/FireEngineWarRoomTest.kt
git commit -m "test(fire): lock engine to War Room headline figures"
```

---

## Self-Review (completed)

- **Spec coverage:** Implements spec §3 model (as pure-Kotlin types; Room persistence is the
  next plan), §4.1 core loop, §4.2 FIRE date, §4.3 net worth, §4.4 loan amortization (EMI from
  start), §4.5 phases, §4.6 reframe, §4.7 `fireDaysImpact`, §4.8 result shape, §7 engine tests
  + War Room regression. UI (spec §5), branding (§6), and Room persistence are deliberately
  separate follow-on plans.
- **Placeholder scan:** No TBD/TODO; every code step shows complete code; stubs in Task 4 are
  explicitly replaced in Tasks 5–7.
- **Type consistency:** `FireModel`, `FireResult`, `FireMath`, `FireEngine`, and all field names
  (`monthlyNet`, `principalAtEmiStart`, `emiStartDate`, `appreciatedValueAtTarget`,
  `requiredExtraMonthlyInvestment`, etc.) are used identically across tasks.

## Follow-on plans (not in this plan)

1. **Persistence:** Room entities + DAOs + DB migration + `FireRepository` that assembles a
   `FireModel` and exposes `FireResult` as a `StateFlow`.
2. **War Room UI:** Compose screens for the 8 sections + empty state.
3. **Edit Model forms:** structured entry/edit (also the Spec-2 extraction confirm surface).
4. **Branding:** display name → "FIRE OS".
