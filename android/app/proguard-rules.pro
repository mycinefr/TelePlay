# Add project specific ProGuard rules here.

# Retrofit
-keep class kotlin.Metadata { *; }
-keep interface com.mycinefr.tv.data.api.** { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Gson / Data Models
-keep class com.mycinefr.tv.data.model.** { *; }
-keep class com.google.gson.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.internal.define.ComponentProcessor

# ExoPlayer / Media3
-keep class androidx.media3.** { *; }

# FFmpeg extension
-keep class com.github.ArmynC.** { *; }

# TV / Leanback (required for TV launcher to find the correct activity)
-keep class androidx.leanback.** { *; }
-keep class androidx.tv.** { *; }
-keep class com.mycinefr.tv.ui.MainActivity { *; }
-keep class com.mycinefr.tv.ui.mobile.MobileMainActivity { *; }
