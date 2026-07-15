package com.expenseai.security;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * AES-256 encrypted SharedPreferences for storing sensitive app data
 * like user settings, model checksums, and session tokens.
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0011\b\u0007\u0018\u0000 !2\u00020\u0001:\u0001!B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\fJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0018\u0010\u0012\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0011J\b\u0010\u0013\u001a\u0004\u0018\u00010\u000eJ\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000eJ\u0006\u0010\u0015\u001a\u00020\fJ\u0016\u0010\u0016\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\fJ\u0016\u0010\u0018\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0011J\u0016\u0010\u0019\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000eJ\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\fJ\u000e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u0011J\u000e\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/expenseai/security/EncryptedPreferences;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "masterKey", "Landroidx/security/crypto/MasterKey;", "prefs", "Landroid/content/SharedPreferences;", "clear", "", "getBoolean", "", "key", "", "default", "getLastAuthTimestamp", "", "getLong", "getModelChecksum", "getString", "isAppLockEnabled", "putBoolean", "value", "putLong", "putString", "remove", "setAppLockEnabled", "enabled", "setLastAuthTimestamp", "timestamp", "storeModelChecksum", "checksum", "Companion", "app_debug"})
public final class EncryptedPreferences {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.security.crypto.MasterKey masterKey = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MODEL_CHECKSUM = "gemma_model_checksum";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_APP_LOCK_ENABLED = "app_lock_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_AUTH_TIMESTAMP = "last_auth_timestamp";
    @org.jetbrains.annotations.NotNull()
    public static final com.expenseai.security.EncryptedPreferences.Companion Companion = null;
    
    @javax.inject.Inject()
    public EncryptedPreferences(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void putString(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getString(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.Nullable()
    java.lang.String p1_772401952) {
        return null;
    }
    
    public final void putBoolean(@org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean value) {
    }
    
    public final boolean getBoolean(@org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean p1_772401952) {
        return false;
    }
    
    public final void putLong(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long value) {
    }
    
    public final long getLong(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long p1_772401952) {
        return 0L;
    }
    
    public final void remove(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void clear() {
    }
    
    public final void storeModelChecksum(@org.jetbrains.annotations.NotNull()
    java.lang.String checksum) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getModelChecksum() {
        return null;
    }
    
    public final void setAppLockEnabled(boolean enabled) {
    }
    
    public final boolean isAppLockEnabled() {
        return false;
    }
    
    public final void setLastAuthTimestamp(long timestamp) {
    }
    
    public final long getLastAuthTimestamp() {
        return 0L;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/expenseai/security/EncryptedPreferences$Companion;", "", "()V", "KEY_APP_LOCK_ENABLED", "", "KEY_LAST_AUTH_TIMESTAMP", "KEY_MODEL_CHECKSUM", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}