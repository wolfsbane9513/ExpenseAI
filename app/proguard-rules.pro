# Keep Gemma/MediaPipe classes
-keep class com.google.mediapipe.** { *; }

# Keep Room entities
-keep class com.expenseai.data.local.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
