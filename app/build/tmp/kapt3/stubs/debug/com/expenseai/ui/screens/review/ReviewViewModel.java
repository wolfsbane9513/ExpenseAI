package com.expenseai.ui.screens.review;

import androidx.lifecycle.ViewModel;
import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.local.PendingExpenseEntity;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.domain.model.Expense;
import com.expenseai.domain.usecase.ProcessSharedTextUseCase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0011J\u0016\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ\u000e\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0011J\f\u0010\u001e\u001a\u00020\u001f*\u00020\u0011H\u0002R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013\u00a8\u0006 "}, d2 = {"Lcom/expenseai/ui/screens/review/ReviewViewModel;", "Landroidx/lifecycle/ViewModel;", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "processSharedTextUseCase", "Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase;", "(Lcom/expenseai/data/local/PendingExpenseDao;Lcom/expenseai/data/repository/ExpenseRepository;Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase;)V", "_shareStagingState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/expenseai/ui/screens/review/ShareStagingState;", "gson", "Lcom/google/gson/Gson;", "pendingExpenses", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/expenseai/data/local/PendingExpenseEntity;", "getPendingExpenses", "()Lkotlinx/coroutines/flow/StateFlow;", "shareStagingState", "getShareStagingState", "confirm", "", "item", "processSharedText", "body", "", "subject", "reject", "toExpense", "Lcom/expenseai/domain/model/Expense;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReviewViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.local.PendingExpenseDao pendingDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.ExpenseRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.usecase.ProcessSharedTextUseCase processSharedTextUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.expenseai.ui.screens.review.ShareStagingState> _shareStagingState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.review.ShareStagingState> shareStagingState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.expenseai.data.local.PendingExpenseEntity>> pendingExpenses = null;
    
    @javax.inject.Inject()
    public ReviewViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseDao pendingDao, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.ExpenseRepository repository, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.usecase.ProcessSharedTextUseCase processSharedTextUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.review.ShareStagingState> getShareStagingState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.expenseai.data.local.PendingExpenseEntity>> getPendingExpenses() {
        return null;
    }
    
    public final void confirm(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseEntity item) {
    }
    
    public final void reject(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.local.PendingExpenseEntity item) {
    }
    
    public final void processSharedText(@org.jetbrains.annotations.NotNull()
    java.lang.String body, @org.jetbrains.annotations.NotNull()
    java.lang.String subject) {
    }
    
    private final com.expenseai.domain.model.Expense toExpense(com.expenseai.data.local.PendingExpenseEntity $this$toExpense) {
        return null;
    }
}