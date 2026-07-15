package com.expenseai.domain.fire;

import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001Bm\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005\u0012\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005\u00a2\u0006\u0002\u0010\u0011J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u00c6\u0003J\u000f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\f0\u0005H\u00c6\u0003J\u000f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005H\u00c6\u0003J\u000f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005H\u00c6\u0003Js\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00052\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00052\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00052\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005H\u00c6\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\'H\u00d6\u0001J\t\u0010(\u001a\u00020)H\u00d6\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013\u00a8\u0006*"}, d2 = {"Lcom/expenseai/domain/fire/FireModel;", "", "profile", "Lcom/expenseai/domain/fire/FireProfile;", "incomes", "", "Lcom/expenseai/domain/fire/IncomeStream;", "liabilities", "Lcom/expenseai/domain/fire/Liability;", "recurringOutflows", "Lcom/expenseai/domain/fire/RecurringOutflow;", "oneTimeEvents", "Lcom/expenseai/domain/fire/OneTimeEvent;", "nonCorpusAssets", "Lcom/expenseai/domain/fire/NonCorpusAsset;", "properties", "Lcom/expenseai/domain/fire/PropertyHolding;", "(Lcom/expenseai/domain/fire/FireProfile;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getIncomes", "()Ljava/util/List;", "getLiabilities", "getNonCorpusAssets", "getOneTimeEvents", "getProfile", "()Lcom/expenseai/domain/fire/FireProfile;", "getProperties", "getRecurringOutflows", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class FireModel {
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.domain.fire.FireProfile profile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.IncomeStream> incomes = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.Liability> liabilities = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.RecurringOutflow> recurringOutflows = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.OneTimeEvent> oneTimeEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.NonCorpusAsset> nonCorpusAssets = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expenseai.domain.fire.PropertyHolding> properties = null;
    
    public FireModel(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireProfile profile, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.IncomeStream> incomes, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.Liability> liabilities, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.RecurringOutflow> recurringOutflows, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.OneTimeEvent> oneTimeEvents, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.NonCorpusAsset> nonCorpusAssets, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.PropertyHolding> properties) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireProfile getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.IncomeStream> getIncomes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.Liability> getLiabilities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.RecurringOutflow> getRecurringOutflows() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.OneTimeEvent> getOneTimeEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.NonCorpusAsset> getNonCorpusAssets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.PropertyHolding> getProperties() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireProfile component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.IncomeStream> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.Liability> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.RecurringOutflow> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.OneTimeEvent> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.NonCorpusAsset> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expenseai.domain.fire.PropertyHolding> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.domain.fire.FireModel copy(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireProfile profile, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.IncomeStream> incomes, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.Liability> liabilities, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.RecurringOutflow> recurringOutflows, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.OneTimeEvent> oneTimeEvents, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.NonCorpusAsset> nonCorpusAssets, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expenseai.domain.fire.PropertyHolding> properties) {
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