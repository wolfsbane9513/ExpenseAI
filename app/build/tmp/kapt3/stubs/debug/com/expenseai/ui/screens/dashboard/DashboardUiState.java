package com.expenseai.ui.screens.dashboard;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.expenseai.ai.GemmaModelManager;
import com.expenseai.ai.GemmaService;
import com.expenseai.ai.ModelStatus;
import com.expenseai.data.local.CategoryTotal;
import com.expenseai.data.repository.ExpenseRepository;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.domain.fire.FireEngine;
import com.expenseai.domain.model.Expense;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u001b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Be\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010#\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\n0\u0007H\u00c6\u0003J\t\u0010%\u001a\u00020\fH\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\u000b\u0010\'\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\t\u0010(\u001a\u00020\u0011H\u00c6\u0003Ji\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u00c6\u0001J\u0013\u0010*\u001a\u00020\u00112\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020-H\u00d6\u0001J\t\u0010.\u001a\u00020\u000eH\u00d6\u0001R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u0006/"}, d2 = {"Lcom/expenseai/ui/screens/dashboard/DashboardUiState;", "", "currentMonth", "Ljava/time/YearMonth;", "totalSpending", "", "categoryTotals", "", "Lcom/expenseai/data/local/CategoryTotal;", "recentExpenses", "Lcom/expenseai/ui/screens/dashboard/ExpenseImpact;", "modelStatus", "Lcom/expenseai/ai/ModelStatus;", "modelMessage", "", "installedModelName", "showAddDialog", "", "(Ljava/time/YearMonth;DLjava/util/List;Ljava/util/List;Lcom/expenseai/ai/ModelStatus;Ljava/lang/String;Ljava/lang/String;Z)V", "getCategoryTotals", "()Ljava/util/List;", "getCurrentMonth", "()Ljava/time/YearMonth;", "getInstalledModelName", "()Ljava/lang/String;", "getModelMessage", "getModelStatus", "()Lcom/expenseai/ai/ModelStatus;", "getRecentExpenses", "getShowAddDialog", "()Z", "getTotalSpending", "()D", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class DashboardUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.time.YearMonth currentMonth = null;
    private final double totalSpending = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.data.local.CategoryTotal> categoryTotals = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.ui.screens.dashboard.ExpenseImpact> recentExpenses = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.ModelStatus modelStatus = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String modelMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String installedModelName = null;
    private final boolean showAddDialog = false;
    
    public DashboardUiState(@org.jetbrains.annotations.NotNull()
    java.time.YearMonth currentMonth, double totalSpending, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.data.local.CategoryTotal> categoryTotals, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.ui.screens.dashboard.ExpenseImpact> recentExpenses, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.ModelStatus modelStatus, @org.jetbrains.annotations.Nullable()
    java.lang.String modelMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String installedModelName, boolean showAddDialog) {
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
    public final java.util.List<com.expenseai.data.local.CategoryTotal> getCategoryTotals() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.ui.screens.dashboard.ExpenseImpact> getRecentExpenses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ai.ModelStatus getModelStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getModelMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getInstalledModelName() {
        return null;
    }
    
    public final boolean getShowAddDialog() {
        return false;
    }
    
    public DashboardUiState() {
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
    public final java.util.List<com.expenseai.data.local.CategoryTotal> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.ui.screens.dashboard.ExpenseImpact> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ai.ModelStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.dashboard.DashboardUiState copy(@org.jetbrains.annotations.NotNull()
    java.time.YearMonth currentMonth, double totalSpending, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.data.local.CategoryTotal> categoryTotals, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.ui.screens.dashboard.ExpenseImpact> recentExpenses, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.ModelStatus modelStatus, @org.jetbrains.annotations.Nullable()
    java.lang.String modelMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String installedModelName, boolean showAddDialog) {
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