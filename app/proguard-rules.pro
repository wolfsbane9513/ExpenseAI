# ========================
# ExpenseAI ProGuard Rules
# ========================

# --- Obfuscation Hardening ---
-repackageclasses 'o'
-allowaccessmodification
-overloadaggressively
-flattenpackagehierarchy 'o'
-adaptclassstrings
-adaptresourcefilenames
-adaptresourcefilecontents

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
}

# Remove debug-only code
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkParameterIsNotNull(...);
    public static void checkNotNullParameter(...);
    public static void checkExpressionValueIsNotNull(...);
    public static void checkNotNullExpressionValue(...);
}

# --- Keep Rules ---

# Keep Gemma/MediaPipe classes
-keep class com.google.mediapipe.** { *; }
-dontwarn com.google.mediapipe.**

# Keep Room entities and DAOs
-keep class com.expenseai.data.local.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.expenseai.ai.ParsedReceipt { *; }

# Keep security classes
-keep class com.expenseai.security.** { *; }

# Keep SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# Keep Biometric classes
-keep class androidx.biometric.** { *; }

# Keep Encrypted SharedPreferences
-keep class androidx.security.crypto.** { *; }

# Keep ML Kit
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Keep CameraX
-keep class androidx.camera.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin Coroutines
-keepnames class kotlinx.coroutines.** { *; }

# --- Security: prevent reflection-based attacks ---
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}

# --- Strip debug info from release ---
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
