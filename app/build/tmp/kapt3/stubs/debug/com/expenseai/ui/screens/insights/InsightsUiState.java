package com.expenseai.ui.screens.insights;

import androidx.lifecycle.ViewModel;
import com.expenseai.ai.GemmaService;
import com.expenseai.data.repository.ExpenseRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.YearMonth;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\u0015\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u000bH\u00c6\u0003JG\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u00072\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u000b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\bH\u00d6\u0001R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/expenseai/ui/screens/insights/InsightsUiState;", "", "currentMonth", "Ljava/time/YearMonth;", "totalSpending", "", "categoryBreakdown", "", "", "aiInsights", "isLoadingInsights", "", "(Ljava/time/YearMonth;DLjava/util/Map;Ljava/lang/String;Z)V", "getAiInsights", "()Ljava/lang/String;", "getCategoryBreakdown", "()Ljava/util/Map;", "getCurrentMonth", "()Ljava/time/YearMonth;", "()Z", "getTotalSpending", "()D", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class InsightsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.time.YearMonth currentMonth = null;
    private final double totalSpending = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Double> categoryBreakdown = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String aiInsights = null;
    private final boolean isLoadingInsights = false;
    
    public InsightsUiState(@org.jetbrains.annotations.NotNull()
    java.time.YearMonth currentMonth, double totalSpending, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Double> categoryBreakdown, @org.jetbrains.annotations.NotNull()
    java.lang.String aiInsights, boolean isLoadingInsights) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.YearMonth getCurrentMonth() {
        return null;
    }
    
    public final double getTotalSpending() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Double> getCategoryBreakdown() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAiInsights() {
        return null;
    }
    
    public final boolean isLoadingInsights() {
        return false;
    }
    
    public InsightsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.YearMonth component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Double> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.insights.InsightsUiState copy(@org.jetbrains.annotations.NotNull()
    java.time.YearMonth currentMonth, double totalSpending, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Double> categoryBreakdown, @org.jetbrains.annotations.NotNull()
    java.lang.String aiInsights, boolean isLoadingInsights) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}