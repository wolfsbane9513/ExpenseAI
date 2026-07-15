package com.expenseai.ui.screens.firemodel;

import androidx.lifecycle.ViewModel;
import com.expenseai.data.repository.FireRepository;
import com.expenseai.domain.fire.FireModel;
import com.expenseai.domain.fire.FireProfile;
import com.expenseai.domain.fire.TargetMode;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDate;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0017\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010 \u001a\u00020\rH\u00c6\u0003JQ\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010\"\u001a\u00020\r2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0012R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\'"}, d2 = {"Lcom/expenseai/ui/screens/firemodel/FireModelUiState;", "", "targetMode", "Lcom/expenseai/domain/fire/TargetMode;", "annualExpenses", "", "targetDate", "Ljava/time/LocalDate;", "equityCagr", "startCorpus", "originalModel", "Lcom/expenseai/domain/fire/FireModel;", "isSaved", "", "(Lcom/expenseai/domain/fire/TargetMode;Ljava/lang/String;Ljava/time/LocalDate;Ljava/lang/String;Ljava/lang/String;Lcom/expenseai/domain/fire/FireModel;Z)V", "getAnnualExpenses", "()Ljava/lang/String;", "getEquityCagr", "()Z", "getOriginalModel", "()Lcom/expenseai/domain/fire/FireModel;", "getStartCorpus", "getTargetDate", "()Ljava/time/LocalDate;", "getTargetMode", "()Lcom/expenseai/domain/fire/TargetMode;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class FireModelUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.TargetMode targetMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String annualExpenses = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate targetDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String equityCagr = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String startCorpus = null;
    @org.jetbrains.annotations.Nullable()
    private final com.expenseai.domain.fire.FireModel originalModel = null;
    private final boolean isSaved = false;
    
    public FireModelUiState(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.TargetMode targetMode, @org.jetbrains.annotations.NotNull()
    java.lang.String annualExpenses, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate targetDate, @org.jetbrains.annotations.NotNull()
    java.lang.String equityCagr, @org.jetbrains.annotations.NotNull()
    java.lang.String startCorpus, @org.jetbrains.annotations.Nullable()
    com.expenseai.domain.fire.FireModel originalModel, boolean isSaved) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.TargetMode getTargetMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAnnualExpenses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate getTargetDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEquityCagr() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStartCorpus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.expenseai.domain.fire.FireModel getOriginalModel() {
        return null;
    }
    
    public final boolean isSaved() {
        return false;
    }
    
    public FireModelUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.TargetMode component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.expenseai.domain.fire.FireModel component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.ui.screens.firemodel.FireModelUiState copy(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.TargetMode targetMode, @org.jetbrains.annotations.NotNull()
    java.lang.String annualExpenses, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate targetDate, @org.jetbrains.annotations.NotNull()
    java.lang.String equityCagr, @org.jetbrains.annotations.NotNull()
    java.lang.String startCorpus, @org.jetbrains.annotations.Nullable()
    com.expenseai.domain.fire.FireModel originalModel, boolean isSaved) {
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