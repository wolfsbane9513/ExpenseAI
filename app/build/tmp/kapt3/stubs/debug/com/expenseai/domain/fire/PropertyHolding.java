package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003JF\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u001bJ\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\""}, d2 = {"Lcom/expenseai/domain/fire/PropertyHolding;", "", "name", "", "currentValue", "", "appreciatedValueAtTarget", "possessionDate", "Ljava/time/LocalDate;", "linkedLiabilityName", "(Ljava/lang/String;DLjava/lang/Double;Ljava/time/LocalDate;Ljava/lang/String;)V", "getAppreciatedValueAtTarget", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getCurrentValue", "()D", "getLinkedLiabilityName", "()Ljava/lang/String;", "getName", "getPossessionDate", "()Ljava/time/LocalDate;", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;DLjava/lang/Double;Ljava/time/LocalDate;Ljava/lang/String;)Lcom/expenseai/domain/fire/PropertyHolding;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class PropertyHolding {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    private final double currentValue = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double appreciatedValueAtTarget = null;
    @org.jetbrains.annotations.Nullable()
    private final java.time.LocalDate possessionDate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String linkedLiabilityName = null;
    
    public PropertyHolding(@org.jetbrains.annotations.NotNull()
    java.lang.String name, double currentValue, @org.jetbrains.annotations.Nullable()
    java.lang.Double appreciatedValueAtTarget, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate possessionDate, @org.jetbrains.annotations.Nullable()
    java.lang.String linkedLiabilityName) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    public final double getCurrentValue() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getAppreciatedValueAtTarget() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getPossessionDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLinkedLiabilityName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.PropertyHolding copy(@org.jetbrains.annotations.NotNull()
    java.lang.String name, double currentValue, @org.jetbrains.annotations.Nullable()
    java.lang.Double appreciatedValueAtTarget, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate possessionDate, @org.jetbrains.annotations.Nullable()
    java.lang.String linkedLiabilityName) {
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