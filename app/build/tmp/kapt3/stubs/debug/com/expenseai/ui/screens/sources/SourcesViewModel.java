package com.expenseai.ui.screens.sources;

import androidx.lifecycle.ViewModel;
import com.expenseai.data.local.PendingExpenseDao;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/expenseai/ui/screens/sources/SourcesViewModel;", "Landroidx/lifecycle/ViewModel;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "(Lcom/expenseai/data/local/PendingExpenseDao;)V", "pendingCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "getPendingCount", "()Lkotlinx/coroutines/flow/StateFlow;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SourcesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.PendingExpenseDao pendingDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> pendingCount = null;
    
    @javax.inject.Inject()
    public SourcesViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseDao pendingDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getPendingCount() {
        return null;
    }
}