package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010 \u001a\u00020\u0005H\u00c6\u0003J\t\u0010!\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0003Ja\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0001J\u0013\u0010%\u001a\u00020\u00032\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020*H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006+"}, d2 = {"Lcom/expenseai/domain/fire/Reframe;", "", "targetReachableByTargetDate", "", "corpusAtTargetDate", "", "gapToTarget", "percentOfTarget", "requiredExtraMonthlyInvestment", "achievableTargetByTargetDate", "dateTargetIsReachable", "Ljava/time/LocalDate;", "options", "", "Lcom/expenseai/domain/fire/ReframeOption;", "(ZDDDDDLjava/time/LocalDate;Ljava/util/List;)V", "getAchievableTargetByTargetDate", "()D", "getCorpusAtTargetDate", "getDateTargetIsReachable", "()Ljava/time/LocalDate;", "getGapToTarget", "getOptions", "()Ljava/util/List;", "getPercentOfTarget", "getRequiredExtraMonthlyInvestment", "getTargetReachableByTargetDate", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class Reframe {
    private final boolean targetReachableByTargetDate = false;
    private final double corpusAtTargetDate = 0.0;
    private final double gapToTarget = 0.0;
    private final double percentOfTarget = 0.0;
    private final double requiredExtraMonthlyInvestment = 0.0;
    private final double achievableTargetByTargetDate = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDate dateTargetIsReachable = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.ReframeOption> options = null;
    
    public Reframe(boolean targetReachableByTargetDate, double corpusAtTargetDate, double gapToTarget, double percentOfTarget, double requiredExtraMonthlyInvestment, double achievableTargetByTargetDate, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate dateTargetIsReachable, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.ReframeOption> options) {
        super();
    }
    
    public final boolean getTargetReachableByTargetDate() {
        return false;
    }
    
    public final double getCorpusAtTargetDate() {
        return 0.0;
    }
    
    public final double getGapToTarget() {
        return 0.0;
    }
    
    public final double getPercentOfTarget() {
        return 0.0;
    }
    
    public final double getRequiredExtraMonthlyInvestment() {
        return 0.0;
    }
    
    public final double getAchievableTargetByTargetDate() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getDateTargetIsReachable() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.ReframeOption> getOptions() {
        return null;
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.ReframeOption> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.Reframe copy(boolean targetReachableByTargetDate, double corpusAtTargetDate, double gapToTarget, double percentOfTarget, double requiredExtraMonthlyInvestment, double achievableTargetByTargetDate, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate dateTargetIsReachable, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.ReframeOption> options) {
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