############################################
# ProGuard / R8 rules for WeightObserver
# Focused on:
# - Jetpack Compose
# - Koin (DI)
# - kotlinx.serialization
# - SQLDelight
# - Logging (Napier / android.util.Log)
############################################

##########
# Compose
##########

# Keep Compose runtime and UI classes
-keep class androidx.compose.** { *; }
-keep class androidx.activity.compose.** { *; }
-keep class androidx.lifecycle.** { *; }

# Do not strip @Composable methods
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Suppress warnings from Compose internals
-dontwarn androidx.compose.**
-dontwarn kotlin.jvm.internal.DefaultConstructorMarker

############
# Koin (DI)
############

-keep class org.koin.** { *; }
-dontwarn org.koin.**

################################
# kotlinx.serialization (JSON)
################################

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    static ** Companion;
}
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}
-dontwarn kotlinx.serialization.**

#############
# SQLDelight
#############

-keep class app.cash.sqldelight.** { *; }
-dontwarn app.cash.sqldelight.**

#########
# Napier
#########

# Strip Napier logging calls in release to reduce size and overhead
-assumenosideeffects class io.github.aakira.napier.Napier {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

#######################
# android.util.Log API
#######################

-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

#########################
# Annotations used by Tink
#########################

# Google Error Prone annotations are compileOnly, safe to ignore for R8
-dontwarn com.google.errorprone.annotations.**

# javax.annotation annotations are also provided only at compile time
-dontwarn javax.annotation.**
-dontwarn javax.annotation.concurrent.**


