# Spec 1.5: Scenario Toggles + "Should I Buy This?" Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let the user toggle what-if scenarios (RSU lumpsum, business income) on their FIRE projection, and answer "should I buy this?" for any purchase in FIRE-days terms.

**Architecture:** Scenarios are a new persisted list on `FireModel`; `FireEngine.project()` resolves enabled scenarios into extra income streams / one-time events before simulating, so every consumer (dashboard chips, projections, purchase decisions) respects toggles automatically. A new `purchaseDecision()` engine helper powers a new Should-I-Buy screen. The Projections tab (InsightsScreen) — which today never renders `FireResult` — gains a Projection card (FIRE date, progress, trajectory sparkline, scenario chips).

**Tech Stack:** Kotlin, Jetpack Compose (Material 3), Hilt, Room+GSON (existing `FireModelEntity` JSON blob), JUnit4 unit tests.

## Global Constraints

- Package `com.expenseai` unchanged (soft rebrand rule).
- Everything on-device; no new dependencies.
- Dark-only theme; use `MaterialTheme.colorScheme` tokens, never raw hex in screens.
- Currency display: `NumberFormat.getCurrencyInstance(Locale("en", "IN"))` with `maximumFractionDigits = 0` (₹, Indian grouping) — the established pattern.
- Amount inputs: `KeyboardOptions(keyboardType = KeyboardType.Number)` or `Decimal`.
- Tests: run `./gradlew testDebugUnitTest`. From WSL there is no JVM — write a `.bat` that sets `JAVA_HOME=C:\Program Files\Android\Android Studio\jbr` and runs `java -cp gradle\wrapper\gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain testDebugUnitTest --console=plain`, then invoke it via `cmd.exe /c` (see memory note; there is no `gradlew.bat` in the repo).
- Commit after each task; do not push until the whole plan is done and green.

## Design decisions locked in

1. **Scenario shape** — one type covers both spec'd cases (RSU = one-time inflow, business = monthly income) with a single editable amount:
   `Scenario(id, label, kind: ONE_TIME_INFLOW | MONTHLY_INCOME, amount, startDate?, enabled)`.
2. **Resolution inside the engine** — `project()` merges enabled scenarios first. `fireDaysImpact()` and `purchaseDecision()` call `project()`, so they inherit toggles for free. No UI recomputation logic.
3. **GSON backward compatibility** — stored `FireModel` JSON predates `scenarios`; GSON reflection leaves the non-null Kotlin field `null`. A `withSafeCollections()` normalizer runs on every read. This is load-bearing — skipping it means NPE on first launch after upgrade.
4. **Seeding** — two disabled scenarios ("RSU Vesting" one-time, "Business Income" monthly, amount 0) are seeded in the default model AND surfaced by the settings ViewModel when a saved model has none, so existing installs see them without a migration.
5. **No new bottom-nav tab** — Should I Buy is a pushed route reachable from the Dashboard quick actions; bottom nav stays at 5 items.

---

### Task 1: Scenario domain type + engine resolution

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireModel.kt`
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (top of `project()`, currently line 38)
- Create: `app/src/test/java/com/expenseai/FireScenarioTest.kt`

**Interfaces:**
- Consumes: existing `FireModel`, `IncomeStream`, `OneTimeEvent`, `IncomeType`, `EventDirection`, `FireEngine.project(model: FireModel): FireResult`.
- Produces: `enum class ScenarioKind { ONE_TIME_INFLOW, MONTHLY_INCOME }`; `data class Scenario(id: String, label: String, kind: ScenarioKind, amount: Double = 0.0, startDate: LocalDate? = null, enabled: Boolean = false)`; `FireModel.scenarios: List<Scenario>` (default empty); `fun FireModel.withSafeCollections(): FireModel`; `fun defaultScenarios(): List<Scenario>`; `FireEngine.resolveScenarios(model): FireModel` (internal).

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/FireScenarioTest.kt`:

```kotlin
package com.expenseai

import com.expenseai.domain.fire.*
import com.google.gson.GsonBuilder
import com.expenseai.data.local.LocalDateAdapter
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class FireScenarioTest {

    private val engine = FireEngine()
    private val start = LocalDate.of(2026, 1, 1)

    private fun baseModel() = FireModel(
        profile = FireProfile(
            targetMode = TargetMode.EXPLICIT_TARGET,
            explicitTargetCorpus = 1_00_00_000.0, // 1 Cr
            targetDate = start.plusYears(15),
            equityCagrPct = 12.0,
            startCorpus = 10_00_000.0,
            simStartDate = start
        ),
        incomes = listOf(
            IncomeStream(
                owner = "Self", label = "Salary", monthlyNet = 1_00_000.0,
                effectiveFrom = start
            )
        )
    )

    @Test
    fun `disabled scenario does not change fire date`() {
        val base = engine.project(baseModel()).fireDate
        val model = baseModel().copy(
            scenarios = listOf(
                Scenario("biz", "Business Income", ScenarioKind.MONTHLY_INCOME,
                    amount = 50_000.0, enabled = false)
            )
        )
        assertEquals(base, engine.project(model).fireDate)
    }

    @Test
    fun `enabled monthly income scenario pulls fire date earlier`() {
        val base = engine.project(baseModel()).fireDate!!
        val model = baseModel().copy(
            scenarios = listOf(
                Scenario("biz", "Business Income", ScenarioKind.MONTHLY_INCOME,
                    amount = 50_000.0, enabled = true)
            )
        )
        val toggled = engine.project(model).fireDate!!
        assertTrue("expected $toggled before $base", toggled.isBefore(base))
    }

    @Test
    fun `enabled one-time inflow scenario pulls fire date earlier`() {
        val base = engine.project(baseModel()).fireDate!!
        val model = baseModel().copy(
            scenarios = listOf(
                Scenario("rsu", "RSU Vesting", ScenarioKind.ONE_TIME_INFLOW,
                    amount = 20_00_000.0, startDate = start.plusYears(1), enabled = true)
            )
        )
        val toggled = engine.project(model).fireDate!!
        assertTrue(toggled.isBefore(base))
    }

    @Test
    fun `enabled scenario with zero amount is ignored`() {
        val base = engine.project(baseModel()).fireDate
        val model = baseModel().copy(
            scenarios = listOf(
                Scenario("rsu", "RSU Vesting", ScenarioKind.ONE_TIME_INFLOW,
                    amount = 0.0, enabled = true)
            )
        )
        assertEquals(base, engine.project(model).fireDate)
    }

    @Test
    fun `withSafeCollections repairs null scenarios from legacy json`() {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()
        // Legacy JSON written before the scenarios field existed
        val legacyJson = gson.toJson(baseModel())
            .replace(Regex(""","?"scenarios":\[[^\]]*\]"""), "")
        val parsed = gson.fromJson(legacyJson, FireModel::class.java).withSafeCollections()
        assertNotNull(parsed.scenarios)
        assertTrue(parsed.scenarios.isEmpty())
    }

    @Test
    fun `defaultScenarios has rsu and business entries, disabled`() {
        val defaults = defaultScenarios()
        assertEquals(setOf("rsu", "business"), defaults.map { it.id }.toSet())
        assertTrue(defaults.none { it.enabled })
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.FireScenarioTest`
Expected: compilation FAILURE — `Scenario`, `ScenarioKind`, `withSafeCollections`, `defaultScenarios` unresolved.

- [ ] **Step 3: Add the domain types to `FireModel.kt`**

Append to `app/src/main/java/com/expenseai/domain/fire/FireModel.kt` (before `data class FireModel`):

```kotlin
enum class ScenarioKind { ONE_TIME_INFLOW, MONTHLY_INCOME }

/** A what-if overlay the user can toggle on the projection (Spec 1.5). */
data class Scenario(
    val id: String,
    val label: String,
    val kind: ScenarioKind,
    val amount: Double = 0.0,
    val startDate: LocalDate? = null,
    val enabled: Boolean = false
)
```

Add the field to `FireModel` (last position):

```kotlin
data class FireModel(
    val profile: FireProfile,
    val incomes: List<IncomeStream> = emptyList(),
    val liabilities: List<Liability> = emptyList(),
    val recurringOutflows: List<RecurringOutflow> = emptyList(),
    val oneTimeEvents: List<OneTimeEvent> = emptyList(),
    val nonCorpusAssets: List<NonCorpusAsset> = emptyList(),
    val properties: List<PropertyHolding> = emptyList(),
    val scenarios: List<Scenario> = emptyList()
)
```

Append after `FireModel` (file bottom):

```kotlin
/**
 * Gson bypasses Kotlin default values, so a FireModel stored before a
 * collection field existed deserializes with that field null. Always call
 * this after fromJson.
 */
@Suppress("USELESS_ELVIS", "SENSELESS_COMPARISON")
fun FireModel.withSafeCollections(): FireModel = copy(
    incomes = incomes ?: emptyList(),
    liabilities = liabilities ?: emptyList(),
    recurringOutflows = recurringOutflows ?: emptyList(),
    oneTimeEvents = oneTimeEvents ?: emptyList(),
    nonCorpusAssets = nonCorpusAssets ?: emptyList(),
    properties = properties ?: emptyList(),
    scenarios = scenarios ?: emptyList()
)

/** Seed scenarios shown when the user has none saved. Amounts are entered in Settings. */
fun defaultScenarios(): List<Scenario> = listOf(
    Scenario("rsu", "RSU Vesting", ScenarioKind.ONE_TIME_INFLOW),
    Scenario("business", "Business Income", ScenarioKind.MONTHLY_INCOME)
)
```

- [ ] **Step 4: Resolve scenarios inside `FireEngine.project()`**

In `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt`, rename the `project` parameter and resolve first. The existing body stays untouched apart from the first line:

```kotlin
fun project(inputModel: FireModel): FireResult {
    val model = resolveScenarios(inputModel)
    val target = FireMath.targetCorpus(model.profile)
    // ... existing body unchanged, it already refers to `model` ...
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
```

- [ ] **Step 5: Run the new tests and the existing engine tests**

Run: `./gradlew testDebugUnitTest --tests "com.expenseai.Fire*"`
Expected: PASS — `FireScenarioTest`, `FireEngineTest`, `FireEngineWarRoomTest`, `FireMathTest` all green (regressions here mean `project()`'s body was altered beyond the first line).

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/ app/src/test/java/com/expenseai/FireScenarioTest.kt
git commit -m "feat(fire): scenario overlays resolved inside the engine"
```

---

### Task 2: Persist scenarios safely in FireRepository

**Files:**
- Modify: `app/src/main/java/com/expenseai/data/repository/FireRepository.kt`

**Interfaces:**
- Consumes: `FireModel.withSafeCollections()`, `defaultScenarios()` (Task 1).
- Produces: `getFireModel(): Flow<FireModel>` now always returns non-null collections and a default model seeded with `defaultScenarios()`.

- [ ] **Step 1: Apply the normalizer on read and seed defaults**

In `getFireModel()` change the deserialization line:

```kotlin
fun getFireModel(): Flow<FireModel> {
    return fireModelDao.getFireModel().map { entity ->
        if (entity != null) {
            gson.fromJson(entity.jsonContent, FireModel::class.java).withSafeCollections()
        } else {
            getDefaultFireModel()
        }
    }
}
```

In `getDefaultFireModel()` add the seed as the last constructor argument:

```kotlin
        recurringOutflows = listOf(
            RecurringOutflow(
                label = "Rent & Lifestyle",
                monthlyAmount = 80_000.0,
                startDate = LocalDate.now()
            )
        ),
        scenarios = defaultScenarios()
```

- [ ] **Step 2: Run the full unit suite (this class has no direct test; the round-trip is covered by `FireScenarioTest.withSafeCollections`)**

Run: `./gradlew testDebugUnitTest`
Expected: PASS (76 existing + 6 new).

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/expenseai/data/repository/FireRepository.kt
git commit -m "feat(fire): persist scenarios with legacy-json null safety"
```

---

### Task 3: `purchaseDecision()` engine helper

**Files:**
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireResult.kt` (add `PurchaseDecision`)
- Modify: `app/src/main/java/com/expenseai/domain/fire/FireEngine.kt` (add helper; delegate `fireDaysImpact`)
- Create: `app/src/test/java/com/expenseai/PurchaseDecisionTest.kt`

**Interfaces:**
- Consumes: `FireEngine.project`, `FireMath.monthlyRate(annualCagrPct: Double): Double`, `OneTimeEvent`, `EventDirection.OUTFLOW`.
- Produces: `data class PurchaseDecision(daysDelta: Long, baseFireDate: LocalDate?, newFireDate: LocalDate?, futureValueAtFireDate: Double)`; `FireEngine.purchaseDecision(model: FireModel, amount: Double, atDate: LocalDate): PurchaseDecision`.

- [ ] **Step 1: Write the failing tests**

Create `app/src/test/java/com/expenseai/PurchaseDecisionTest.kt`:

```kotlin
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
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew testDebugUnitTest --tests com.expenseai.PurchaseDecisionTest`
Expected: compilation FAILURE — `purchaseDecision` unresolved.

- [ ] **Step 3: Implement**

Append to `app/src/main/java/com/expenseai/domain/fire/FireResult.kt`:

```kotlin
data class PurchaseDecision(
    val daysDelta: Long,
    val baseFireDate: LocalDate?,
    val newFireDate: LocalDate?,
    /** What the amount would grow to by the base FIRE date if invested instead. */
    val futureValueAtFireDate: Double
)
```

In `FireEngine.kt`, replace the body of `fireDaysImpact` and add `purchaseDecision` next to it (imports `java.time.temporal.ChronoUnit` already present):

```kotlin
/** FIRE days delayed by spending [amount] (as a one-time outflow at [atDate]). */
fun fireDaysImpact(model: FireModel, amount: Double, atDate: LocalDate): Long =
    purchaseDecision(model, amount, atDate).daysDelta

/** Full purchase-decision readout for the Should-I-Buy screen (Spec 1.5). */
fun purchaseDecision(model: FireModel, amount: Double, atDate: LocalDate): PurchaseDecision {
    if (amount <= 0.0) return PurchaseDecision(0L, project(model).fireDate, project(model).fireDate, 0.0)
    val base = project(model)
    val withSpend = model.copy(
        oneTimeEvents = model.oneTimeEvents + OneTimeEvent(
            "purchase", amount, atDate, EventDirection.OUTFLOW
        )
    )
    val spent = project(withSpend)
    val daysDelta = if (base.fireDate != null && spent.fireDate != null) {
        ChronoUnit.DAYS.between(base.fireDate, spent.fireDate)
    } else 0L
    val monthsToFire = base.fireDate
        ?.let { ChronoUnit.MONTHS.between(atDate, it).coerceAtLeast(0) } ?: 0L
    val fv = amount * Math.pow(
        1.0 + FireMath.monthlyRate(model.profile.equityCagrPct),
        monthsToFire.toDouble()
    )
    return PurchaseDecision(daysDelta, base.fireDate, spent.fireDate, fv)
}
```

- [ ] **Step 4: Run tests**

Run: `./gradlew testDebugUnitTest --tests "com.expenseai.*"`
Expected: PASS, including the existing `FireEngineTest` impact tests (the delegation must not change `fireDaysImpact` results).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/expenseai/domain/fire/ app/src/test/java/com/expenseai/PurchaseDecisionTest.kt
git commit -m "feat(fire): purchaseDecision helper with future-value readout"
```

---

### Task 4: Scenario editing in FIRE Settings

**Files:**
- Modify: `app/src/main/java/com/expenseai/ui/screens/firemodel/FireModelViewModel.kt`
- Modify: `app/src/main/java/com/expenseai/ui/screens/firemodel/FireModelScreen.kt`

**Interfaces:**
- Consumes: `Scenario`, `ScenarioKind`, `defaultScenarios()` (Task 1); `FireRepository.saveFireModel`.
- Produces: `data class ScenarioEdit(id: String, label: String, kind: ScenarioKind, amountText: String, enabled: Boolean)`; `FireModelUiState.scenarios: List<ScenarioEdit>`; `FireModelViewModel.toggleScenario(id: String)`, `updateScenarioAmount(id: String, value: String)`. Task 5's chips reuse the persisted `Scenario.enabled`, not these.

- [ ] **Step 1: Extend the ViewModel**

In `FireModelViewModel.kt` add to the bottom of the file:

```kotlin
data class ScenarioEdit(
    val id: String,
    val label: String,
    val kind: ScenarioKind,
    val amountText: String,
    val enabled: Boolean
)
```

Add `import com.expenseai.domain.fire.Scenario`, `import com.expenseai.domain.fire.ScenarioKind`, `import com.expenseai.domain.fire.defaultScenarios`.

Add the field to `FireModelUiState`:

```kotlin
    val scenarios: List<ScenarioEdit> = emptyList(),
```

In the `init` collector, map stored scenarios (seeding defaults when empty) — extend the existing `it.copy(...)`:

```kotlin
                _uiState.update { it.copy(
                    targetMode = model.profile.targetMode,
                    annualExpenses = model.profile.annualRetirementExpenses.toString(),
                    targetDate = model.profile.targetDate,
                    equityCagr = model.profile.equityCagrPct.toString(),
                    startCorpus = model.profile.startCorpus.toString(),
                    scenarios = model.scenarios.ifEmpty { defaultScenarios() }.map { s ->
                        ScenarioEdit(s.id, s.label, s.kind, s.amount.toLong().toString(), s.enabled)
                    },
                    originalModel = model
                ) }
```

Add the two mutators:

```kotlin
    fun toggleScenario(id: String) {
        _uiState.update { state ->
            state.copy(scenarios = state.scenarios.map {
                if (it.id == id) it.copy(enabled = !it.enabled) else it
            })
        }
    }

    fun updateScenarioAmount(id: String, value: String) {
        _uiState.update { state ->
            state.copy(scenarios = state.scenarios.map {
                if (it.id == id) it.copy(amountText = value.filter { c -> c.isDigit() }) else it
            })
        }
    }
```

In `save()`, persist the edits — the original `startDate` is preserved by id-lookup:

```kotlin
        val originalScenarios = original.scenarios.associateBy { it.id }
        val updatedScenarios = currentState.scenarios.map { edit ->
            Scenario(
                id = edit.id,
                label = edit.label,
                kind = edit.kind,
                amount = edit.amountText.toDoubleOrNull() ?: 0.0,
                startDate = originalScenarios[edit.id]?.startDate,
                enabled = edit.enabled
            )
        }

        viewModelScope.launch {
            repository.saveFireModel(
                original.copy(profile = updatedProfile, scenarios = updatedScenarios)
            )
            _uiState.update { it.copy(isSaved = true) }
        }
```

- [ ] **Step 2: Add the Scenarios section to the screen**

In `FireModelScreen.kt`, after the "Investment Assumptions" fields (after the `startCorpus` `OutlinedTextField`, before the final `Spacer`), insert:

```kotlin
            HorizontalDivider()

            Text(
                text = "Scenarios",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "What-if overlays you can toggle on the Projections tab.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            uiState.scenarios.forEach { scenario ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(scenario.label, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = if (scenario.kind == ScenarioKind.ONE_TIME_INFLOW) {
                                "One-time inflow"
                            } else {
                                "Monthly income"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = scenario.enabled,
                        onCheckedChange = { viewModel.toggleScenario(scenario.id) }
                    )
                }
                OutlinedTextField(
                    value = scenario.amountText,
                    onValueChange = { viewModel.updateScenarioAmount(scenario.id, it) },
                    label = { Text("${scenario.label} amount") },
                    prefix = { Text("₹ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
```

Add imports: `androidx.compose.ui.Alignment`, `com.expenseai.domain.fire.ScenarioKind`.

- [ ] **Step 3: Compile + full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all tests pass (this task is UI/VM plumbing; the engine tests guard behavior).

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/expenseai/ui/screens/firemodel/
git commit -m "feat(ui): scenario toggles and amounts in FIRE settings"
```

---

### Task 5: Projection card with scenario chips on the Projections tab

**Files:**
- Modify: `app/src/main/java/com/expenseai/ui/screens/insights/InsightsViewModel.kt`
- Modify: `app/src/main/java/com/expenseai/ui/screens/insights/InsightsScreen.kt`

**Interfaces:**
- Consumes: `FireRepository.getFireModel()/saveFireModel()`, `FireEngine.project()`, `FireResult` (fields: `fireDate: LocalDate?`, `targetCorpus: Double`, `progressPercent: Double`, `trajectory: List<TrajectoryPoint>` with `corpus: Double`), `Scenario`.
- Produces: `InsightsViewModel.projection: StateFlow<ProjectionUiState>` where `data class ProjectionUiState(result: FireResult? = null, scenarios: List<Scenario> = emptyList())`; `InsightsViewModel.toggleScenario(id: String)`; `onShouldIBuyClick: () -> Unit` parameter on `InsightsScreen` (wired in Task 6).

- [ ] **Step 1: Extend the ViewModel**

In `InsightsViewModel.kt` add imports:

```kotlin
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.fire.FireResult
import com.expenseai.domain.fire.Scenario
import kotlinx.coroutines.Dispatchers
```

Add to the constructor:

```kotlin
    private val fireRepository: FireRepository,
    private val fireEngine: FireEngine
```

Add below `uiState`:

```kotlin
    data class ProjectionUiState(
        val result: FireResult? = null,
        val scenarios: List<Scenario> = emptyList()
    )

    val projection: StateFlow<ProjectionUiState> = fireRepository.getFireModel()
        .map { model -> ProjectionUiState(fireEngine.project(model), model.scenarios) }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProjectionUiState()
        )

    fun toggleScenario(id: String) {
        viewModelScope.launch {
            val model = fireRepository.getFireModel().first()
            fireRepository.saveFireModel(
                model.copy(scenarios = model.scenarios.map {
                    if (it.id == id) it.copy(enabled = !it.enabled) else it
                })
            )
        }
    }
```

(Persisting the toggle makes the dashboard FIRE-day chips and Should-I-Buy respect it too; the flow re-emits and the projection recomputes automatically.)

- [ ] **Step 2: Add the Projection card to the screen**

In `InsightsScreen.kt` add imports:

```kotlin
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.expenseai.domain.fire.FireResult
import com.expenseai.domain.fire.Scenario
import java.time.format.DateTimeFormatter
```

Change the signature:

```kotlin
fun InsightsScreen(
    onShouldIBuyClick: () -> Unit = {},
    viewModel: InsightsViewModel = hiltViewModel()
)
```

Collect the state next to `uiState`:

```kotlin
    val projection by viewModel.projection.collectAsStateWithLifecycle()
```

Insert as the FIRST child of the scrolling `Column` (before `MonthSelector`):

```kotlin
            projection.result?.let { result ->
                ProjectionCard(
                    result = result,
                    scenarios = projection.scenarios,
                    formatter = formatter,
                    onToggleScenario = viewModel::toggleScenario,
                    onShouldIBuyClick = onShouldIBuyClick
                )
            }
```

Append the composables at file bottom:

```kotlin
@Composable
private fun ProjectionCard(
    result: FireResult,
    scenarios: List<Scenario>,
    formatter: java.text.NumberFormat,
    onToggleScenario: (String) -> Unit,
    onShouldIBuyClick: () -> Unit
) {
    val dateFormat = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "FIRE Projection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "FIRE date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        result.fireDate?.format(dateFormat) ?: "Beyond horizon",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Target corpus",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        formatter.format(result.targetCorpus),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            LinearProgressIndicator(
                progress = { (result.progressPercent / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            TrajectorySparkline(result)
            if (scenarios.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(scenarios, key = { it.id }) { scenario ->
                        FilterChip(
                            selected = scenario.enabled,
                            onClick = { onToggleScenario(scenario.id) },
                            label = { Text(scenario.label) }
                        )
                    }
                }
            }
            TextButton(onClick = onShouldIBuyClick) {
                Text("Should I buy something? →")
            }
        }
    }
}

@Composable
private fun TrajectorySparkline(result: FireResult) {
    val color = MaterialTheme.colorScheme.primary
    val points = result.trajectory
    if (points.size < 2) return
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        val maxCorpus = points.maxOf { it.corpus }.coerceAtLeast(1.0)
        val minCorpus = points.minOf { it.corpus }
        val range = (maxCorpus - minCorpus).coerceAtLeast(1.0)
        val stepX = size.width / (points.size - 1)
        val path = Path()
        points.forEachIndexed { i, p ->
            val x = i * stepX
            val y = size.height - ((p.corpus - minCorpus) / range * size.height).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = color, style = Stroke(width = 4f))
    }
}
```

Note: `TrajectoryPoint.corpus` is the field name (see `FireResult.kt:5`). `formatter` in this screen currently has decimals; set `formatter.maximumFractionDigits = 0` where it is created at the top of `InsightsScreen`.

- [ ] **Step 3: Compile + full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/expenseai/ui/screens/insights/
git commit -m "feat(ui): FIRE projection card with scenario toggles on Projections tab"
```

---

### Task 6: "Should I Buy This?" screen + navigation

**Files:**
- Create: `app/src/main/java/com/expenseai/ui/screens/shouldibuy/ShouldIBuyViewModel.kt`
- Create: `app/src/main/java/com/expenseai/ui/screens/shouldibuy/ShouldIBuyScreen.kt`
- Modify: `app/src/main/java/com/expenseai/ui/navigation/NavGraph.kt`
- Modify: `app/src/main/java/com/expenseai/ui/screens/dashboard/DashboardScreen.kt` (entry button)

**Interfaces:**
- Consumes: `FireEngine.purchaseDecision(model, amount, atDate): PurchaseDecision` (Task 3), `FireRepository.getFireModel()`.
- Produces: route `"should_i_buy"`; `DashboardScreen`/`DashboardContent` gain `onShouldIBuyClick: () -> Unit = {}`; `InsightsScreen`'s `onShouldIBuyClick` (Task 5) is wired here.

- [ ] **Step 1: ViewModel**

Create `ShouldIBuyViewModel.kt`:

```kotlin
package com.expenseai.ui.screens.shouldibuy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireEngine
import com.expenseai.domain.fire.PurchaseDecision
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

data class ShouldIBuyUiState(
    val amountText: String = "",
    val decision: PurchaseDecision? = null
)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ShouldIBuyViewModel @Inject constructor(
    fireRepository: FireRepository,
    private val fireEngine: FireEngine
) : ViewModel() {

    private val _amountText = MutableStateFlow("")

    val uiState: StateFlow<ShouldIBuyUiState> = combine(
        _amountText,
        fireRepository.getFireModel()
    ) { text, model ->
        val amount = text.toDoubleOrNull() ?: 0.0
        ShouldIBuyUiState(
            amountText = text,
            decision = if (amount > 0.0) {
                fireEngine.purchaseDecision(model, amount, LocalDate.now())
            } else null
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShouldIBuyUiState()
        )

    fun updateAmount(value: String) {
        _amountText.value = value.filter { it.isDigit() }
    }
}
```

- [ ] **Step 2: Screen**

Create `ShouldIBuyScreen.kt`:

```kotlin
package com.expenseai.ui.screens.shouldibuy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.domain.fire.PurchaseDecision
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShouldIBuyScreen(
    onBack: () -> Unit,
    viewModel: ShouldIBuyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Should I Buy This?", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Enter a purchase amount to see what it costs you in FIRE days.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = viewModel::updateAmount,
                label = { Text("Purchase amount") },
                prefix = { Text("₹ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.decision?.let { decision ->
                DecisionCard(decision, formatter)
            }
        }
    }
}

@Composable
private fun DecisionCard(decision: PurchaseDecision, formatter: NumberFormat) {
    val dateFormat = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
    val verdict = when {
        decision.daysDelta <= 7 -> "Barely a blip."
        decision.daysDelta <= 30 -> "Noticeable, but fine if it matters to you."
        decision.daysDelta <= 90 -> "Significant. Sleep on it."
        else -> "Major setback. Are you sure?"
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "+${decision.daysDelta} FIRE days",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (decision.daysDelta > 90) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            Text(verdict, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            decision.baseFireDate?.let { base ->
                Text(
                    "FIRE date: ${base.format(dateFormat)} → " +
                        (decision.newFireDate?.format(dateFormat) ?: "beyond horizon"),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                "Invested instead, this becomes " +
                    "${formatter.format(decision.futureValueAtFireDate)} by your FIRE date.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

- [ ] **Step 3: Wire navigation**

In `NavGraph.kt`:

Add to the `Screen` sealed class (icon is required by the class but unused for non-bottom-nav routes; `ShoppingBag` is verified present in the icon pack):

```kotlin
    data object ShouldIBuy : Screen("should_i_buy", "Should I Buy?", Icons.Default.ShoppingBag)
```

Add import `androidx.compose.material.icons.filled.ShoppingBag` and `com.expenseai.ui.screens.shouldibuy.ShouldIBuyScreen`.

Add the composable route inside `NavHost` (after the `FireModel` route):

```kotlin
            composable(Screen.ShouldIBuy.route) {
                ShouldIBuyScreen(onBack = { navController.popBackStack() })
            }
```

Pass the callback to both entry points:

```kotlin
                    com.expenseai.ui.screens.dashboard.DashboardScreen(
                        onScanClick = { navController.navigate(Screen.Scan.route) },
                        onSettingsClick = { navController.navigate(Screen.FireModel.route) },
                        onShouldIBuyClick = { navController.navigate(Screen.ShouldIBuy.route) }
                    )
```

```kotlin
            composable(Screen.Insights.route) {
                InsightsScreen(
                    onShouldIBuyClick = { navController.navigate(Screen.ShouldIBuy.route) }
                )
            }
```

- [ ] **Step 4: Dashboard entry button**

In `DashboardScreen.kt` add `onShouldIBuyClick: () -> Unit = {}` to both `DashboardScreen` and `DashboardContent` signatures (pass it through), and to `QuickActionsCard`:

```kotlin
@Composable
private fun QuickActionsCard(
    onScanClick: () -> Unit,
    onInstallModel: () -> Unit,
    onShouldIBuyClick: () -> Unit,
    hasInstalledModel: Boolean,
    isModelBusy: Boolean
) {
```

Inside `QuickActionsCard`, wrap the existing `Row` in a `Column` and add the button below it:

```kotlin
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ...existing two buttons unchanged (remove their old padding modifier)...
            }
            OutlinedButton(
                onClick = onShouldIBuyClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Should I Buy This?")
            }
        }
```

(The existing `Row` currently owns the `.padding(18.dp)` — move that padding to the new `Column` as shown, and pass `onShouldIBuyClick = onShouldIBuyClick` at the `QuickActionsCard` call site.)

- [ ] **Step 5: Compile + full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all tests green.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/expenseai/ui/
git commit -m "feat(ui): Should I Buy This? screen with FIRE-days verdict"
```

---

### Task 7: Final verification

- [ ] **Step 1: Full suite**

Run: `./gradlew testDebugUnitTest`
Expected: BUILD SUCCESSFUL; previous count was 76 tests — expect ~86 with the new `FireScenarioTest` (6) and `PurchaseDecisionTest` (4).

- [ ] **Step 2: Manual device checklist** (requires emulator/device — cannot be automated here)

1. Settings → Scenarios: enter an RSU amount, toggle ON, Save → snackbar.
2. Projections tab: Projection card shows FIRE date; toggling the RSU chip moves the date earlier; sparkline redraws.
3. Dashboard → "Should I Buy This?" → enter 100000 → days delta, date shift, and future-value line render; verdict copy changes at 7/30/90-day thresholds.
4. Kill + relaunch: toggles persisted; upgrading over an existing install does not crash on the Projections tab (legacy-JSON null safety).

- [ ] **Step 3: Push (only after user confirmation or per session authorization)**

```bash
git push origin fire-os
```

---

## Self-review notes

- **Spec coverage:** spec line 34 (scenario overlays) → Tasks 1, 2, 4, 5; spec line 35 + §4.7 (purchase-decision screen on `fireDaysImpact`) → Tasks 3, 6. Spec 1 §5.5's deferred "scenario toggles on trajectory" → Task 5.
- **Known simplification:** scenarios support exactly two kinds with one amount each — no editing of `startDate` in UI (defaults to sim start; RSU date editable later if needed). YAGNI until real usage demands it.
- **Type consistency check:** `Scenario`/`ScenarioKind`/`defaultScenarios`/`withSafeCollections` (Task 1) are consumed by Tasks 2/4/5; `PurchaseDecision`/`purchaseDecision` (Task 3) by Task 6; `onShouldIBuyClick` appears in Tasks 5 and 6 with the same shape.
- **Risk:** `InsightsViewModel` gains constructor params — Hilt provides both (`FireRepository` is `@Singleton @Inject`, `FireEngine` provided in `AppModule`); no test constructs `InsightsViewModel` today (verified: no `InsightsViewModelTest`).
