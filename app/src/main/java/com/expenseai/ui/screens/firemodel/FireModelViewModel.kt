package com.expenseai.ui.screens.firemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.repository.FireRepository
import com.expenseai.domain.fire.FireModel
import com.expenseai.domain.fire.FireProfile
import com.expenseai.domain.fire.TargetMode
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

    fun save() {
        val currentState = _uiState.value
        val original = currentState.originalModel ?: return
        
        val updatedProfile = original.profile.copy(
            targetMode = currentState.targetMode,
            annualRetirementExpenses = currentState.annualExpenses.toDoubleOrNull() ?: 0.0,
            equityCagrPct = currentState.equityCagr.toDoubleOrNull() ?: 12.0,
            startCorpus = currentState.startCorpus.toDoubleOrNull() ?: 0.0
        )
        
        viewModelScope.launch {
            repository.saveFireModel(original.copy(profile = updatedProfile))
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
    val originalModel: FireModel? = null,
    val isSaved: Boolean = false
)
