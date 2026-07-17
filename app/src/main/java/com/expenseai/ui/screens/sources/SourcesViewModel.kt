package com.expenseai.ui.screens.sources

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ExtractDocumentUseCase
import com.expenseai.domain.usecase.EmptyDocumentException
import com.expenseai.ui.screens.docimport.ExtractionResultHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val pendingDao: PendingExpenseDao,
    private val extractDocumentUseCase: ExtractDocumentUseCase,
    private val extractionResultHolder: ExtractionResultHolder
) : ViewModel() {

    sealed interface ImportState {
        data object Idle : ImportState
        data object Loading : ImportState
        data object Ready : ImportState
        data class Error(val message: String) : ImportState
    }

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()

    val pendingCount: StateFlow<Int> = pendingDao.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun importDocument(uri: Uri) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            _importState.value = try {
                extractionResultHolder.set(extractDocumentUseCase.execute(uri))
                ImportState.Ready
            } catch (e: EmptyDocumentException) {
                ImportState.Error("Couldn't read this document — no text found.")
            } catch (e: SecurityException) {
                ImportState.Error("This PDF is password-protected.")
            } catch (e: Exception) {
                ImportState.Error("Couldn't open this document.")
            }
        }
    }

    fun resetImportState() { _importState.value = ImportState.Idle }
}
