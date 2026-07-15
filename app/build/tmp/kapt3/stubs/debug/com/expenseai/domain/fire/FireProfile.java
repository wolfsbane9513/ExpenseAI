package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\b\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010 \u001a\u00020\bH\u00c6\u0003JY\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010%\u001a\u00020&H\u00d6\u0001J\t\u0010\'\u001a\u00020(H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006)"}, d2 = {"Lcom/expenseai/domain/fire/FireProfile;", "", "targetMode", "Lcom/expenseai/domain/fire/TargetMode;", "annualRetirementExpenses", "", "explicitTargetCorpus", "targetDate", "Ljava/time/LocalDate;", "equityCagrPct", "inflationPct", "startCorpus", "simStartDate", "(Lcom/expenseai/domain/fire/TargetMode;DDLjava/time/LocalDate;DDDLjava/time/LocalDate;)V", "getAnnualRetirementExpenses", "()D", "getEquityCagrPct", "getExplicitTargetCorpus", "getInflationPct", "getSimStartDate", "()Ljava/time/LocalDate;", "getStartCorpus", "getTargetDate", "getTargetMode", "()Lcom/expenseai/domain/fire/TargetMode;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class FireProfile {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.TargetMode targetMode = null;
    private final double annualRetirementExpenses = 0.0;
    private final double explicitTargetCorpus = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate targetDate = null;
    private final double equityCagrPct = 0.0;
    private final double inflationPct = 0.0;
    private final double startCorpus = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate simStartDate = null;
    
    public FireProfile(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.TargetMode targetMode, double annualRetirementExpenses, double explicitTargetCorpus, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate targetDate, double equityCagrPct, double inflationPct, double startCorpus, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate simStartDate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.TargetMode getTargetMode() {
        return null;
    }
    
    public final double getAnnualRetirementExpenses() {
        return 0.0;
    }
    
    public final double getExplicitTargetCorpus() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate getTargetDate() {
        return null;
    }
    
    public final double getEquityCagrPct() {
        return 0.0;
    }
    
    public final double getInflationPct() {
        return 0.0;
    }
    
    public final double getStartCorpus() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate getSimStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.TargetMode component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate component4() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireProfile copy(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.TargetMode targetMode, double annualRetirementExpenses, double explicitTargetCorpus, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate targetDate, double equityCagrPct, double inflationPct, double startCorpus, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate simStartDate) {
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