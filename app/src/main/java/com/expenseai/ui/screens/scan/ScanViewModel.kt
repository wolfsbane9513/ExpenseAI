package com.expenseai.ui.screens.scan

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.ai.GemmaService
import com.expenseai.ai.OCRService
import com.expenseai.ai.ParsedReceipt
import com.expenseai.data.repository.ExpenseRepository
import com.expenseai.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

enum class ScanState {
    IDLE,
    CAPTURING,
    PROCESSING_OCR,
    PROCESSING_AI,
    REVIEW,
    SAVING,
    SAVED,
    ERROR
}

data class ScanUiState(
    val scanState: ScanState = ScanState.IDLE,
    val ocrText: String = "",
    val parsedReceipt: ParsedReceipt = ParsedReceipt(),
    val errorMessage: String? = null,
    val capturedImageUri: Uri? = null,
    // Editable fields for review
    val editVendor: String = "",
    val editAmount: String = "",
    val editCategory: String = "other",
    val editDate: String = LocalDate.now().toString()
)

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val ocrService: OCRService,
    private val gemmaService: GemmaService,
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun processImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(scanState = ScanState.PROCESSING_OCR)

                val ocrText = ocrService.extractText(bitmap)
                _uiState.value = _uiState.value.copy(
                    ocrText = ocrText,
                    scanState = ScanState.PROCESSING_AI
                )

                val parsed = gemmaService.parseReceipt(ocrText)
                _uiState.value = _uiState.value.copy(
                    parsedReceipt = parsed,
                    scanState = ScanState.REVIEW,
                    editVendor = parsed.vendor,
                    editAmount = if (parsed.amount > 0) parsed.amount.toString() else "",
                    editCategory = parsed.category,
                    editDate = parsed.date.ifBlank { LocalDate.now().toString() }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    scanState = ScanState.ERROR,
                    errorMessage = e.message ?: "Failed to process image"
                )
            }
        }
    }

    fun processImageUri(uri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    scanState = ScanState.PROCESSING_OCR,
                    capturedImageUri = uri
                )

                val ocrText = ocrService.extractText(uri)
                _uiState.value = _uiState.value.copy(
                    ocrText = ocrText,
                    scanState = ScanState.PROCESSING_AI
                )

                val parsed = gemmaService.parseReceipt(ocrText)
                _uiState.value = _uiState.value.copy(
                    parsedReceipt = parsed,
                    scanState = ScanState.REVIEW,
                    editVendor = parsed.vendor,
                    editAmount = if (parsed.amount > 0) parsed.amount.toString() else "",
                    editCategory = parsed.category,
                    editDate = parsed.date.ifBlank { LocalDate.now().toString() }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    scanState = ScanState.ERROR,
                    errorMessage = e.message ?: "Failed to process image"
                )
            }
        }
    }

    fun updateVendor(value: String) {
        _uiState.value = _uiState.value.copy(editVendor = value)
    }

    fun updateAmount(value: String) {
        _uiState.value = _uiState.value.copy(editAmount = value)
    }

    fun updateCategory(value: String) {
        _uiState.value = _uiState.value.copy(editCategory = value)
    }

    fun updateDate(value: String) {
        _uiState.value = _uiState.value.copy(editDate = value)
    }

    fun saveExpense() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(scanState = ScanState.SAVING)
            try {
                val state = _uiState.value
                repository.addExpense(
                    Expense(
                        vendor = state.editVendor,
                        amount = state.editAmount.toDoubleOrNull() ?: 0.0,
                        category = state.editCategory,
                        date = state.editDate,
                        imageUri = state.capturedImageUri?.toString(),
                        source = "ocr",
                        items = state.parsedReceipt.items
                    )
                )
                _uiState.value = _uiState.value.copy(scanState = ScanState.SAVED)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    scanState = ScanState.ERROR,
                    errorMessage = "Failed to save: ${e.message}"
                )
            }
        }
    }

    fun reset() {
        _uiState.value = ScanUiState()
    }
}
