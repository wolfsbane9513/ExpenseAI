package com.expenseai.ui.screens.firemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.Scenario
import com.expenseai.domain.fire.ScenarioKind
import com.expenseai.domain.fire.TargetMode
import com.expenseai.domain.fire.defaultScenarios
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FireModelViewModel @Inject constructor(
    private val repository: FireRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FireModelUiState())
    val uiState: StateFlow<FireModelUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFireModel().collect { model ->
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
            }
        }
    }

    fun updateTargetMode(mode: TargetMode) {
        _uiState.update { it.copy(targetMode = mode) }
    }

    fun updateAnnualExpenses(value: String) {
        _uiState.update { it.copy(annualExpenses = value) }
    }

    fun updateEquityCagr(value: String) {
        _uiState.update { it.copy(equityCagr = value) }
    }

    fun updateStartCorpus(value: String) {
        _uiState.update { it.copy(startCorpus = value) }
    }

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

    fun save() {
        val currentState = _uiState.value
        val original = currentState.originalModel ?: return

        val updatedProfile = original.profile.copy(
            targetMode = currentState.targetMode,
            annualRetirementExpenses = currentState.annualExpenses.toDoubleOrNull() ?: 0.0,
            equityCagrPct = currentState.equityCagr.toDoubleOrNull() ?: 12.0,
            startCorpus = currentState.startCorpus.toDoubleOrNull() ?: 0.0
        )

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
    }
    
    fun resetSavedStatus() {
        _uiState.update { it.copy(isSaved = false) }
    }
}

data class FireModelUiState(
    val targetMode: TargetMode = TargetMode.ANNUAL_EXPENSES_25X,
    val annualExpenses: String = "",
    val targetDate: LocalDate = LocalDate.now().plusYears(10),
    val equityCagr: String = "12.0",
    val startCorpus: String = "0",
    val scenarios: List<ScenarioEdit> = emptyList(),
    val originalModel: FireModel? = null,
    val isSaved: Boolean = false
)

data class ScenarioEdit(
    val id: String,
    val label: String,
    val kind: ScenarioKind,
    val amountText: String,
    val enabled: Boolean
)
