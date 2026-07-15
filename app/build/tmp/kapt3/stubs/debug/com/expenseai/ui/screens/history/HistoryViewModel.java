package com.expenseai.ui.screens.history;

import androidx.lifecycle.ViewModel;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.domain.fire.FireEngine;
import com.expenseai.domain.model.Expense;
import com.expenseai.ui.screens.dashboard.ExpenseImpact;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDate;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u000bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0017"}, d2 = {"Lcom/expenseai/ui/screens/history/HistoryViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "fireRepository", "Lcom/expenseai/data/repository/FireRepository;", "fireEngine", "Lcom/expenseai/domain/fire/FireEngine;", "(Lcom/expenseai/data/repository/ExpenseRepository;Lcom/expenseai/data/repository/FireRepository;Lcom/expenseai/domain/fire/FireEngine;)V", "_searchQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/expenseai/ui/screens/history/HistoryUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteExpense", "", "id", "", "updateSearchQuery", "query", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HistoryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.ExpenseRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.FireRepository fireRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.FireEngine fireEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.history.HistoryUiState> uiState = null;
    
    @javax.inject.Inject()
    public HistoryViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.ExpenseRepository repository, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.FireRepository fireRepository, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireEngine fireEngine) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.history.HistoryUiState> getUiState() {
        return null;
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void deleteExpense(long id) {
    }
}