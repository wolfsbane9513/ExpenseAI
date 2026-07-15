package com.expenseai.security;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0007\u0018\u0000 \u001a2\u00020\u0001:\u0002\u0019\u001aB\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\n0\u0010J\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u0015J\u0006\u0010\u0018\u001a\u00020\u0015R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/expenseai/security/BiometricHelper;", "", "context", "Landroid/content/Context;", "encryptedPreferences", "Lcom/expenseai/security/EncryptedPreferences;", "(Landroid/content/Context;Lcom/expenseai/security/EncryptedPreferences;)V", "biometricManager", "Landroidx/biometric/BiometricManager;", "authenticate", "", "activity", "Landroidx/fragment/app/FragmentActivity;", "onSuccess", "Lkotlin/Function0;", "onError", "Lkotlin/Function1;", "", "checkBiometricStatus", "Lcom/expenseai/security/BiometricHelper$BiometricStatus;", "isAppLockEnabled", "", "setAppLockEnabled", "enabled", "shouldRequireAuth", "BiometricStatus", "Companion", "app_debug"})
public final class BiometricHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expenseai.security.EncryptedPreferences encryptedPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.biometric.BiometricManager biometricManager = null;
    private static final long AUTH_TIMEOUT_MS = 300000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.security.BiometricHelper.Companion Companion = null;
    
    @javax.inject.Inject()
    public BiometricHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.expenseai.security.EncryptedPreferences encryptedPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expenseai.security.BiometricHelper.BiometricStatus checkBiometricStatus() {
        return null;
    }
    
    public final boolean isAppLockEnabled() {
        return false;
    }
    
    public final void setAppLockEnabled(boolean enabled) {
    }
    
    public final boolean shouldRequireAuth() {
        return false;
    }
    
    public final void authenticate(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/expenseai/security/BiometricHelper$BiometricStatus;", "", "(Ljava/lang/String;I)V", "AVAILABLE", "NO_HARDWARE", "HARDWARE_UNAVAILABLE", "NOT_ENROLLED", "app_debug"})
    public static enum BiometricStatus {
        /*public static final*/ AVAILABLE /* = new AVAILABLE() */,
        /*public static final*/ NO_HARDWARE /* = new NO_HARDWARE() */,
        /*public static final*/ HARDWARE_UNAVAILABLE /* = new HARDWARE_UNAVAILABLE() */,
        /*public static final*/ NOT_ENROLLED /* = new NOT_ENROLLED() */;
        
        BiometricStatus() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.expenseai.security.BiometricHelper.BiometricStatus> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/expenseai/security/BiometricHelper$Companion;", "", "()V", "AUTH_TIMEOUT_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}