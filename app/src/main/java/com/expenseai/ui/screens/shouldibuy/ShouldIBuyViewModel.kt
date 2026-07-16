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
