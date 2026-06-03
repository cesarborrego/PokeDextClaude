package com.cesar.pokedexclaude.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/**
 * Light color scheme with modern, friendly Pokeball-inspired palette.
 * Features warm neutrals and coral-red primary color for instant brand recognition.
 */
private val LightColorScheme = lightColorScheme(
    primary = PokeballRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = PokeballRedDark,

    secondary = PokeballBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD3E4F8),
    onSecondaryContainer = PokeballBlueDark,

    tertiary = CoralAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF4B1C2B),

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorBackground,
    onErrorContainer = Color(0xFF930006),

    background = NeutralWhite,
    onBackground = NeutralGray900,

    surface = SurfaceLight,
    onSurface = NeutralGray900,
    surfaceVariant = NeutralGray50,
    onSurfaceVariant = NeutralGray700,

    outline = NeutralGray100,
    outlineVariant = NeutralGray200,

    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = NeutralGray900,
    inverseOnSurface = NeutralGray50,
    inversePrimary = Color(0xFFFFB4AB)
)

/**
 * Dark color scheme with cool dark grays and vibrant accent colors.
 * Maintains brand consistency while being easy on the eyes in low light.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = PokeballRedDark,
    onPrimaryContainer = Color(0xFFFFDAD6),

    secondary = PokeballBlueLight,
    onSecondary = Color(0xFF003258),
    secondaryContainer = PokeballBlueDark,
    onSecondaryContainer = Color(0xFFD3E4F8),

    tertiary = Color(0xFFFFB1C8),
    onTertiary = Color(0xFF5E1133),
    tertiaryContainer = Color(0xFF7B2952),
    onTertiaryContainer = Color(0xFFFFD8E4),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = DarkGray100,
    onBackground = DarkGray900,

    surface = SurfaceDark,
    onSurface = DarkGray900,
    surfaceVariant = DarkGray50,
    onSurfaceVariant = DarkGray700,

    outline = DarkGray200,
    outlineVariant = Color(0xFF3C3C3E),

    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = NeutralGray50,
    inverseOnSurface = NeutralGray900,
    inversePrimary = PokeballRed
)

/**
 * Custom shapes for rounded, friendly design aesthetic.
 * Increased corner radius throughout for a softer, more approachable feel.
 */
private val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * PokedexClaude app theme with modern, minimalist design.
 *
 * Features:
 * - Pokeball-inspired color palette (red + blue)
 * - Warm neutral backgrounds for friendliness
 * - Refined typography with optimal readability
 * - Rounded shapes throughout
 * - Support for dynamic color on Android 12+
 * - Full dark mode support
 *
 * Design Philosophy:
 * Per Material Design 3 guidelines, this theme prioritizes:
 * - Visual hierarchy through typography and color
 * - Accessibility with WCAG AA compliant contrast ratios
 * - Consistency through systematic spacing and shape design
 * - Personality that reflects the Pokemon brand without being childish
 *
 * @param darkTheme Whether to use dark theme colors
 * @param dynamicColor Whether to use dynamic color from Android 12+ (default: false for brand consistency)
 * @param content The composable content to theme
 */
@Composable
fun PokedexClaudeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled by default to maintain brand identity
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}