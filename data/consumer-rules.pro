# Data Module ProGuard Rules

# Keep data models for serialization
-keep class com.cesar.pokedexclaude.data.remote.dto.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.cesar.pokedexclaude.data.**$$serializer { *; }
-keepclassmembers class com.cesar.pokedexclaude.data.** {
    *** Companion;
}
-keepclasseswithmembers class com.cesar.pokedexclaude.data.** {
    kotlinx.serialization.KSerializer serializer(...);
}
