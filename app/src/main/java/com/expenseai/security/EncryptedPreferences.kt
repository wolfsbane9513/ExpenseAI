package com.expenseai.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AES-256 encrypted SharedPreferences for storing sensitive app data
 * like user settings, model checksums, and session tokens.
 */
@Singleton
class EncryptedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "expense_ai_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun putString(key: String, value: String) =
        prefs.edit().putString(key, value).apply()

    fun getString(key: String, default: String? = null): String? =
        prefs.getString(key, default)

    fun putBoolean(key: String, value: Boolean) =
        prefs.edit().putBoolean(key, value).apply()

    fun getBoolean(key: String, default: Boolean = false): Boolean =
        prefs.getBoolean(key, default)

    fun putLong(key: String, value: Long) =
        prefs.edit().putLong(key, value).apply()

    fun getLong(key: String, default: Long = 0L): Long =
        prefs.getLong(key, default)

    fun remove(key: String) =
        prefs.edit().remove(key).apply()

    fun clear() = prefs.edit().clear().apply()

    // Model integrity verification
    fun storeModelChecksum(checksum: String) =
        putString(KEY_MODEL_CHECKSUM, checksum)

    fun getModelChecksum(): String? =
        getString(KEY_MODEL_CHECKSUM)

    fun setAppLockEnabled(enabled: Boolean) =
        putBoolean(KEY_APP_LOCK_ENABLED, enabled)

    fun isAppLockEnabled(): Boolean =
        getBoolean(KEY_APP_LOCK_ENABLED, false)

    fun setLastAuthTimestamp(timestamp: Long) =
        putLong(KEY_LAST_AUTH_TIMESTAMP, timestamp)

    fun getLastAuthTimestamp(): Long =
        getLong(KEY_LAST_AUTH_TIMESTAMP, 0L)

    companion object {
        private const val KEY_MODEL_CHECKSUM = "gemma_model_checksum"
        private const val KEY_APP_LOCK_ENABLED = "app_lock_enabled"
        private const val KEY_LAST_AUTH_TIMESTAMP = "last_auth_timestamp"
    }
}
