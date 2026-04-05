package com.expenseai.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized security manager handling device integrity checks,
 * root detection, debugger detection, and tamper verification.
 */
@Singleton
class SecurityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class SecurityCheckResult(
        val isSecure: Boolean,
        val warnings: List<String>
    )

    fun performSecurityChecks(): SecurityCheckResult {
        val warnings = mutableListOf<String>()

        if (isDeviceRooted()) warnings.add("Device appears to be rooted")
        if (isDebuggerAttached()) warnings.add("Debugger is attached")
        if (isRunningOnEmulator()) warnings.add("Running on emulator")
        if (isAppDebuggable()) warnings.add("App is in debug mode")
        if (isInstalledFromUnknownSource()) warnings.add("Installed from unknown source")

        return SecurityCheckResult(
            isSecure = warnings.isEmpty(),
            warnings = warnings
        )
    }

    fun isDeviceRooted(): Boolean {
        val rootIndicators = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/su/bin",
            "/system/xbin/daemonsu"
        )

        // Check for root binaries
        if (rootIndicators.any { File(it).exists() }) return true

        // Check for root management apps
        val rootPackages = listOf(
            "com.topjohnwu.magisk",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.noshufou.android.su",
            "com.thirdparty.superuser"
        )

        val pm = context.packageManager
        rootPackages.forEach { pkg ->
            try {
                pm.getPackageInfo(pkg, 0)
                return true
            } catch (_: Exception) { }
        }

        // Check if su is executable
        try {
            Runtime.getRuntime().exec("su")
            return true
        } catch (_: Exception) { }

        return false
    }

    fun isDebuggerAttached(): Boolean =
        android.os.Debug.isDebuggerConnected() || android.os.Debug.waitingForDebugger()

    fun isRunningOnEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.BOARD == "QC_Reference_Phone"
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST.startsWith("Build")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    fun isAppDebuggable(): Boolean =
        context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun isInstalledFromUnknownSource(): Boolean {
        val installer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getInstallerPackageName(context.packageName)
        }

        val trustedInstallers = listOf(
            "com.android.vending",      // Google Play
            "com.google.android.feedback", // Google Play alt
            "com.amazon.venezia"         // Amazon App Store
        )

        return installer == null || installer !in trustedInstallers
    }
}
