package com.expenseai.domain.fire;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Pure financial helpers. All amounts in rupees.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0004J\u0016\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ\u0016\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rJ\u000e\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/expenseai/domain/fire/FireMath;", "", "()V", "emi", "", "principal", "annualRatePct", "tenureMonths", "", "monthlyRate", "annualCagrPct", "monthsBetween", "from", "Ljava/time/LocalDate;", "to", "outstandingBalance", "liability", "Lcom/expenseai/domain/fire/Liability;", "date", "targetCorpus", "profile", "Lcom/expenseai/domain/fire/FireProfile;", "app_debug"})
public final class FireMath {
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.domain.fire.FireMath INSTANCE = null;
    
    private FireMath() {
        super();
    }
    
    /**
     * Monthly rate using the SIP convention (annual / 12), matching the source War Room.
     */
    public final double monthlyRate(double annualCagrPct) {
        return 0.0;
    }
    
    /**
     * The corpus the user is aiming for.
     */
    public final double targetCorpus(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.FireProfile profile) {
        return 0.0;
    }
    
    /**
     * Standard EMI for a fully amortizing loan. Returns 0 for non-positive inputs.
     */
    public final double emi(double principal, double annualRatePct, int tenureMonths) {
        return 0.0;
    }
    
    /**
     * Months elapsed from [from] to [to], floored at 0.
     */
    public final int monthsBetween(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate from, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate to) {
        return 0;
    }
    
    /**
     * Declining-balance outstanding principal of [liability] at [date].
     */
    public final double outstandingBalance(@org.jetbrains.annotations.NotNull()
    com.expenseai.domain.fire.Liability liability, @org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return 0.0;
    }
}