package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\bH\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0006H\u00c6\u0003JQ\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\'"}, d2 = {"Lcom/expenseai/domain/fire/IncomeStream;", "", "owner", "", "label", "monthlyNet", "", "effectiveFrom", "Ljava/time/LocalDate;", "effectiveTo", "type", "Lcom/expenseai/domain/fire/IncomeType;", "annualGrowthPct", "(Ljava/lang/String;Ljava/lang/String;DLjava/time/LocalDate;Ljava/time/LocalDate;Lcom/expenseai/domain/fire/IncomeType;D)V", "getAnnualGrowthPct", "()D", "getEffectiveFrom", "()Ljava/time/LocalDate;", "getEffectiveTo", "getLabel", "()Ljava/lang/String;", "getMonthlyNet", "getOwner", "getType", "()Lcom/expenseai/domain/fire/IncomeType;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class IncomeStream {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String owner = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    private final double monthlyNet = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate effectiveFrom = null;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDate effectiveTo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.IncomeType type = null;
    private final double annualGrowthPct = 0.0;
    
    public IncomeStream(@org.jetbrains.annotations.NotNull()
    java.lang.String owner, @org.jetbrains.annotations.NotNull()
    java.lang.String label, double monthlyNet, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate effectiveFrom, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate effectiveTo, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.IncomeType type, double annualGrowthPct) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOwner() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    public final double getMonthlyNet() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate getEffectiveFrom() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getEffectiveTo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.IncomeType getType() {
        return null;
    }
    
    public final double getAnnualGrowthPct() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDate component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.IncomeType component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.IncomeStream copy(@org.jetbrains.annotations.NotNull()
    java.lang.String owner, @org.jetbrains.annotations.NotNull()
    java.lang.String label, double monthlyNet, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate effectiveFrom, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate effectiveTo, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.IncomeType type, double annualGrowthPct) {
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