# Core UI Module ProGuard Rules

# Keep all Composables
-keep @androidx.compose.runtime.Composable class * { *; }

# Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
