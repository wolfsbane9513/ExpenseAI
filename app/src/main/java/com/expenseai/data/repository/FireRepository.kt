package com.expenseai.data.repository

import com.expenseai.data.local.FireModelDao
import com.expenseai.data.local.FireModelEntity
import com.expenseai.data.local.LocalDateAdapter
import com.expenseai.domain.fire.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireRepository @Inject constructor(
    private val fireModelDao: FireModelDao
) {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    fun getFireModel(): Flow<FireModel> {
        return fireModelDao.getFireModel().map { entity ->
            if (entity != null) {
                gson.fromJson(entity.jsonContent, FireModel::class.java).withSafeCollections()
            } else {
                getDefaultFireModel()
            }
        }
    }

    suspend fun saveFireModel(fireModel: FireModel) {
        val json = gson.toJson(fireModel)
        fireModelDao.saveFireModel(FireModelEntity(jsonContent = json))
    }

    private fun getDefaultFireModel(): FireModel {
        return FireModel(
            profile = FireProfile(
                targetMode = TargetMode.ANNUAL_EXPENSES_25X,
                annualRetirementExpenses = 12_00_000.0, // 1L/month
                targetDate = LocalDate.now().plusYears(10),
                equityCagrPct = 12.0,
                startCorpus = 50_00_000.0,
                simStartDate = LocalDate.now()
            ),
            incomes = listOf(
                IncomeStream(
                    owner = "Self",
                    label = "Primary Salary",
                    monthlyNet = 2_50_000.0,
                    effectiveFrom = LocalDate.now(),
                    annualGrowthPct = 5.0
                )
            ),
            recurringOutflows = listOf(
                RecurringOutflow(
                    label = "Rent & Lifestyle",
                    monthlyAmount = 80_000.0,
                    startDate = LocalDate.now()
                )
            ),
            scenarios = defaultScenarios()
        )
    }
}
