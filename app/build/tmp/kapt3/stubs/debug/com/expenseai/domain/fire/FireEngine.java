package com.expenseai.domain.fire;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\rH\u0002J&\u0010\u000e\u001a\u00020\r2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\rH\u0002J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\n2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u0018\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0010H\u0002J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0010H\u0002J\u001e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0010J\u0018\u0010\u001b\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0010H\u0002J\u0018\u0010\u001c\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0010H\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\rH\u0002J%\u0010 \u001a\u00020!2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\rH\u0000\u00a2\u0006\u0002\b#J\u000e\u0010$\u001a\u00020%2\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010&\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/expenseai/domain/fire/FireEngine;", "", "horizonMonths", "", "(I)V", "buildReframe", "Lcom/expenseai/domain/fire/Reframe;", "model", "Lcom/expenseai/domain/fire/FireModel;", "trajectory", "", "Lcom/expenseai/domain/fire/TrajectoryPoint;", "target", "", "corpusAt", "date", "Ljava/time/LocalDate;", "fallback", "derivePhases", "Lcom/expenseai/domain/fire/PhaseSummary;", "emiForMonth", "month", "eventsForMonth", "fireDaysImpact", "", "amount", "atDate", "incomeForMonth", "investableForMonth", "money", "", "v", "netWorthAt", "Lcom/expenseai/domain/fire/NetWorthBreakdown;", "corpus", "netWorthAt$app_debug", "project", "Lcom/expenseai/domain/fire/FireResult;", "recurringForMonth", "app_debug"})
public final class FireEngine {
    private final int horizonMonths = 0;
    
    public FireEngine(int horizonMonths) {
        super();
    }
    
    /**
     * Income active in a given month, with annual growth applied from effectiveFrom.
     */
    private final double incomeForMonth(com.expenseai.domain.fire.FireModel model, java.time.LocalDate month) {
        return 0.0;
    }
    
    private final double recurringForMonth(com.expenseai.domain.fire.FireModel model, java.time.LocalDate month) {
        return 0.0;
    }
    
    private final double emiForMonth(com.expenseai.domain.fire.FireModel model, java.time.LocalDate month) {
        return 0.0;
    }
    
    private final double investableForMonth(com.expenseai.domain.fire.FireModel model, java.time.LocalDate month) {
        return 0.0;
    }
    
    private final double eventsForMonth(com.expenseai.domain.fire.FireModel model, java.time.LocalDate month) {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireResult project(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireModel model) {
        return null;
    }
    
    private final double corpusAt(java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory, java.time.LocalDate date, double fallback) {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.NetWorthBreakdown netWorthAt$app_debug(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireModel model, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate date, double corpus) {
        return null;
    }
    
    private final java.util.List<com.expenseai.domain.fire.PhaseSummary> derivePhases(java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory) {
        return null;
    }
    
    private final com.expenseai.domain.fire.Reframe buildReframe(com.expenseai.domain.fire.FireModel model, java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory, double target) {
        return null;
    }
    
    /**
     * FIRE days delayed by spending [amount] (as a one-time outflow at [atDate]).
     */
    public final long fireDaysImpact(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireModel model, double amount, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate atDate) {
        return 0L;
    }
    
    private final java.lang.String money(double v) {
        return null;
    }
    
    public FireEngine() {
        super();
    }
}