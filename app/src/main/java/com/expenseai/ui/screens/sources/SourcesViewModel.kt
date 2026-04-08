package com.expenseai.ui.screens.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expenseai.data.local.PendingExpenseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val pendingDao: PendingExpenseDao
) : ViewModel() {

    val pendingCount: StateFlow<Int> = pendingDao.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
