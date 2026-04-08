package com.expenseai.ui.screens.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.local.PendingExpenseDao
import com.expenseai.domain.usecase.ProcessSmsUseCase
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
    private val processSmsUseCase: ProcessSmsUseCase,
    private val pendingDao: PendingExpenseDao
) : ViewModel() {

    val pendingCount: StateFlow<Int> = pendingDao.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _smsEnabled = MutableStateFlow(false)
    val smsEnabled: StateFlow<Boolean> = _smsEnabled.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _lastScanCount = MutableStateFlow(0)
    val lastScanCount: StateFlow<Int> = _lastScanCount.asStateFlow()

    fun setSmsEnabled(enabled: Boolean) {
        _smsEnabled.value = enabled
        if (enabled) scanSms()
    }

    fun scanSms() {
        viewModelScope.launch {
            _isScanning.value = true
            val count = processSmsUseCase.execute()
            _lastScanCount.value = count
            _isScanning.value = false
        }
    }
}
