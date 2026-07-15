package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0010J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000f\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0\u0007H\u00c6\u0003J\t\u0010!\u001a\u00020\fH\u00c6\u0003J\t\u0010\"\u001a\u00020\u000eH\u00c6\u0003J\t\u0010#\u001a\u00020\u0005H\u00c6\u0003J]\u0010$\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020)H\u00d6\u0001J\t\u0010*\u001a\u00020+H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016\u00a8\u0006,"}, d2 = {"Lcom/expenseai/domain/fire/FireResult;", "", "fireDate", "Ljava/time/LocalDate;", "targetCorpus", "", "trajectory", "", "Lcom/expenseai/domain/fire/TrajectoryPoint;", "phases", "Lcom/expenseai/domain/fire/PhaseSummary;", "netWorthAtTarget", "Lcom/expenseai/domain/fire/NetWorthBreakdown;", "reframe", "Lcom/expenseai/domain/fire/Reframe;", "progressPercent", "(Ljava/time/LocalDate;DLjava/util/List;Ljava/util/List;Lcom/expenseai/domain/fire/NetWorthBreakdown;Lcom/expenseai/domain/fire/Reframe;D)V", "getFireDate", "()Ljava/time/LocalDate;", "getNetWorthAtTarget", "()Lcom/expenseai/domain/fire/NetWorthBreakdown;", "getPhases", "()Ljava/util/List;", "getProgressPercent", "()D", "getReframe", "()Lcom/expenseai/domain/fire/Reframe;", "getTargetCorpus", "getTrajectory", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class FireResult {
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDate fireDate = null;
    private final double targetCorpus = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.PhaseSummary> phases = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.NetWorthBreakdown netWorthAtTarget = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.Reframe reframe = null;
    private final double progressPercent = 0.0;
    
    public FireResult(@org.jetbrains.annotations.Nullable()
    java.time.LocalDate fireDate, double targetCorpus, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.PhaseSummary> phases, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.NetWorthBreakdown netWorthAtTarget, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.Reframe reframe, double progressPercent) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getFireDate() {
        return null;
    }
    
    public final double getTargetCorpus() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.TrajectoryPoint> getTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.PhaseSummary> getPhases() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.NetWorthBreakdown getNetWorthAtTarget() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.Reframe getReframe() {
        return null;
    }
    
    public final double getProgressPercent() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.TrajectoryPoint> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.PhaseSummary> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.NetWorthBreakdown component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.Reframe component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireResult copy(@org.jetbrains.annotations.Nullable()
    java.time.LocalDate fireDate, double targetCorpus, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.TrajectoryPoint> trajectory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.PhaseSummary> phases, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.NetWorthBreakdown netWorthAtTarget, @org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.Reframe reframe, double progressPercent) {
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