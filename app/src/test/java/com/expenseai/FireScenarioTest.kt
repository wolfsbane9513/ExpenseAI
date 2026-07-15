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
