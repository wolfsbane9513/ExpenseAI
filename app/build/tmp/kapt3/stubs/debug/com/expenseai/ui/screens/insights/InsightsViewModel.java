package com.expenseai.ui.screens.insights;

import androidx.lifecycle.ViewModel;
import com.expenseai.ai.GemmaService;
import com.expenseai.data.repository.ExpenseRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.YearMonth;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0017\u001a\u00020\u0015R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u0018"}, d2 = {"Lcom/expenseai/ui/screens/insights/InsightsViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "(Lcom/expenseai/data/repository/ExpenseRepository;Lcom/expenseai/ai/GemmaService;)V", "_aiInsights", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_currentMonth", "Ljava/time/YearMonth;", "kotlin.jvm.PlatformType", "_isLoadingInsights", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/expenseai/ui/screens/insights/InsightsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "generateInsights", "", "nextMonth", "previousMonth", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class InsightsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.ExpenseRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.time.YearMonth> _currentMonth = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _aiInsights = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoadingInsights = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.insights.InsightsUiState> uiState = null;
    
    @javax.inject.Inject()
    public InsightsViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.ExpenseRepository repository, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.insights.InsightsUiState> getUiState() {
        return null;
    }
    
    public final void generateInsights() {
    }
    
    public final void previousMonth() {
    }
    
    public final void nextMonth() {
    }
}