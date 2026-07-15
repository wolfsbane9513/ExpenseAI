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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001:\u0001&B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ&\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0019J\u0006\u0010\u001e\u001a\u00020\u0019J\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u0017J\u0006\u0010#\u001a\u00020\u0017J\u0006\u0010$\u001a\u00020\u0017J\u0006\u0010%\u001a\u00020\u0017R\u001c\u0010\r\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\'"}, d2 = {"Lcom/expenseai/ui/screens/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/expenseai/data/repository/ExpenseRepository;", "gemmaService", "Lcom/expenseai/ai/GemmaService;", "modelManager", "Lcom/expenseai/ai/GemmaModelManager;", "fireRepository", "Lcom/expenseai/data/repository/FireRepository;", "fireEngine", "Lcom/expenseai/domain/fire/FireEngine;", "(Lcom/expenseai/data/repository/ExpenseRepository;Lcom/expenseai/ai/GemmaService;Lcom/expenseai/ai/GemmaModelManager;Lcom/expenseai/data/repository/FireRepository;Lcom/expenseai/domain/fire/FireEngine;)V", "_currentMonth", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Ljava/time/YearMonth;", "kotlin.jvm.PlatformType", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/expenseai/ui/screens/dashboard/DashboardUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addExpense", "", "vendor", "", "amount", "", "category", "date", "getModelImportSummary", "importModel", "uri", "Landroid/net/Uri;", "nextMonth", "previousMonth", "removeModel", "showAddDialog", "Quadruple", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.ExpenseRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaService gemmaService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.ai.GemmaModelManager modelManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.data.repository.FireRepository fireRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.FireEngine fireEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.time.YearMonth> _currentMonth = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.dashboard.DashboardUiState> uiState = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.ExpenseRepository repository, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaService gemmaService, @org.jetbrains.annotations.NotNull()
    com.expenseai.ai.GemmaModelManager modelManager, @org.jetbrains.annotations.NotNull()
    com.expenseai.data.repository.FireRepository fireRepository, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireEngine fireEngine) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.expenseai.ui.screens.dashboard.DashboardUiState> getUiState() {
        return null;
    }
    
    public final void importModel(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void removeModel() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getModelImportSummary() {
        return null;
    }
    
    public final void previousMonth() {
    }
    
    public final void nextMonth() {
    }
    
    public final void showAddDialog() {
    }
    
    public final void addExpense(@org.jetbrains.annotations.NotNull()
    java.lang.String vendor, double amount, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u0003*\u0004\b\u0003\u0010\u00042\u00020\u0005B%\u0012\u0006\u0010\u0006\u001a\u00028\u0000\u0012\u0006\u0010\u0007\u001a\u00028\u0001\u0012\u0006\u0010\b\u001a\u00028\u0002\u0012\u0006\u0010\t\u001a\u00028\u0003\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u0011\u001a\u00028\u0000H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u0012\u001a\u00028\u0001H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u0013\u001a\u00028\u0002H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u0014\u001a\u00028\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJN\u0010\u0015\u001a\u001a\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u00002\b\b\u0002\u0010\u0007\u001a\u00028\u00012\b\b\u0002\u0010\b\u001a\u00028\u00022\b\b\u0002\u0010\t\u001a\u00028\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u0013\u0010\u0006\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\t\u001a\u00028\u0003\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000e\u0010\fR\u0013\u0010\u0007\u001a\u00028\u0001\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000f\u0010\fR\u0013\u0010\b\u001a\u00028\u0002\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u001e"}, d2 = {"Lcom/expenseai/ui/screens/dashboard/DashboardViewModel$Quadruple;", "A", "B", "C", "D", "", "first", "second", "third", "fourth", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getFirst", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getFourth", "getSecond", "getThird", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/expenseai/ui/screens/dashboard/DashboardViewModel$Quadruple;", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    static final class Quadruple<A extends java.lang.Object, B extends java.lang.Object, C extends java.lang.Object, D extends java.lang.Object> {
        private final A first = null;
        private final B second = null;
        private final C third = null;
        private final D fourth = null;
        
        public Quadruple(A first, B second, C third, D fourth) {
            super();
        }
        
        public final A getFirst() {
            return null;
        }
        
        public final B getSecond() {
            return null;
        }
        
        public final C getThird() {
            return null;
        }
        
        public final D getFourth() {
            return null;
        }
        
        public final A component1() {
            return null;
        }
        
        public final B component2() {
            return null;
        }
        
        public final C component3() {
            return null;
        }
        
        public final D component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.expenseai.ui.screens.dashboard.DashboardViewModel.Quadruple<A, B, C, D> copy(A first, B second, C third, D fourth) {
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
}