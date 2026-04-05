package com.expenseai.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptedPreferences: EncryptedPreferences
) {
    private val biometricManager = BiometricManager.from(context)

    enum class BiometricStatus {
        AVAILABLE,
        NO_HARDWARE,
        HARDWARE_UNAVAILABLE,
        NOT_ENROLLED
    }

    fun checkBiometricStatus(): BiometricStatus {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.HARDWARE_UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NOT_ENROLLED
            else -> BiometricStatus.HARDWARE_UNAVAILABLE
        }
    }

    fun isAppLockEnabled(): Boolean = encryptedPreferences.isAppLockEnabled()

    fun setAppLockEnabled(enabled: Boolean) = encryptedPreferences.setAppLockEnabled(enabled)

    fun shouldRequireAuth(): Boolean {
        if (!isAppLockEnabled()) return false
        val lastAuth = encryptedPreferences.getLastAuthTimestamp()
        val elapsed = System.currentTimeMillis() - lastAuth
        return elapsed > AUTH_TIMEOUT_MS
    }

    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                encryptedPreferences.setLastAuthTimestamp(System.currentTimeMillis())
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                // Individual attempt failed, biometric prompt handles retry
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("ExpenseAI")
            .setSubtitle("Authenticate to access your expenses")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        BiometricPrompt(activity, executor, callback).authenticate(promptInfo)
    }

    companion object {
        private const val AUTH_TIMEOUT_MS = 5 * 60 * 1000L // 5 minutes
    }
}
