package com.expenseai;

import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.data.local.PendingExpenseEntity;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.domain.usecase.ProcessSharedTextUseCase;
import com.expenseai.ui.screens.review.ReviewViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0011\u001a\u00060\u0012j\u0002`\u0013H\u0007J\f\u0010\u0014\u001a\u00060\u0012j\u0002`\u0013H\u0007J\f\u0010\u0015\u001a\u00060\u0012j\u0002`\u0013H\u0007J\b\u0010\u0016\u001a\u00020\u0012H\u0007J\f\u0010\u0017\u001a\u00060\u0012j\u0002`\u0013H\u0007J\b\u0010\u0018\u001a\u00020\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/expenseai/ReviewViewModelTest;", "", "()V", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "pendingFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/expenseai/data/local/PendingExpenseEntity;", "processSharedTextUseCase", "Lcom/expenseai/domain/usecase/ProcessSharedTextUseCase;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "testDispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "viewModel", "Lcom/expenseai/ui/screens/review/ReviewViewModel;", "confirm moves pending to expenses", "", "Lkotlinx/coroutines/test/TestResult;", "pending list emits from dao", "reject deletes from pending dao", "setup", "shared text failure updates error state", "teardown", "app_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class ReviewViewModelTest {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher testDispatcher = null;
    private com.expenseai.data.local.PendingExpenseDao pendingDao;
    private com.expenseai.data.repository.ExpenseRepository repository;
    private com.expenseai.domain.usecase.ProcessSharedTextUseCase processSharedTextUseCase;
    private com.expenseai.ui.screens.review.ReviewViewModel viewModel;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.expenseai.data.local.PendingExpenseEntity>> pendingFlow = null;
    
    public ReviewViewModelTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void teardown() {
    }
}