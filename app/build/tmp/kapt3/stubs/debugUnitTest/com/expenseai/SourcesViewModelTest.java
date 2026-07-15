package com.expenseai;

import com.expenseai.data.local.PendingExpenseDao;
import com.expenseai.ui.screens.sources.SourcesViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\f\u001a\u00060\rj\u0002`\u000eH\u0007J\b\u0010\u000f\u001a\u00020\rH\u0007J\b\u0010\u0010\u001a\u00020\rH\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/expenseai/SourcesViewModelTest;", "", "()V", "countFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "pendingDao", "Lcom/expenseai/data/local/PendingExpenseDao;", "testDispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "viewModel", "Lcom/expenseai/ui/screens/sources/SourcesViewModel;", "pending count reflects dao flow", "", "Lkotlinx/coroutines/test/TestResult;", "setup", "teardown", "app_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class SourcesViewModelTest {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher testDispatcher = null;
    private com.expenseai.data.local.PendingExpenseDao pendingDao;
    private com.expenseai.ui.screens.sources.SourcesViewModel viewModel;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> countFlow = null;
    
    public SourcesViewModelTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void teardown() {
    }
}