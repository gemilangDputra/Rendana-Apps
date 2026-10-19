package com.gdproduct.rendana.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    // Primary
    primary = light_primary,
    onPrimary = light_primary_contrast,
    primaryContainer = light_primary_soft,
    onPrimaryContainer = light_primary_dark,

    // Secondary
    secondary = light_primary_dark,
    onSecondary = light_text_on_primary,
    secondaryContainer = light_primary_light,
    onSecondaryContainer = light_primary_dark,

    // Tertiary
    tertiary = light_chart_savings,
    onTertiary = light_text_on_primary,
    tertiaryContainer = light_info_soft,
    onTertiaryContainer = light_chart_savings,

    // Background
    background = light_background,
    onBackground = light_text,

    // Surface
    surface = light_surface,
    onSurface = light_text,
    surfaceVariant = light_background_secondary,
    onSurfaceVariant = light_text_secondary,
    surfaceTint = light_primary,

    // Inverse
    inverseSurface = light_text,
    inverseOnSurface = light_surface,
    inversePrimary = light_primary_light,

    // Error
    error = light_error,
    onError = light_text_on_expense,
    errorContainer = light_error_soft,
    onErrorContainer = light_expense_text,

    // Outline
    outline = light_outline,
    outlineVariant = light_border,

    // Scrim
    scrim = Color(0xFF000000)
)

val DarkColorScheme = darkColorScheme(
    // Primary
    primary = dark_primary,
    onPrimary = dark_text_on_primary,
    primaryContainer = dark_primary_soft,
    onPrimaryContainer = dark_primary_light,
	
    // Secondary
    secondary = dark_primary_dark,
    onSecondary = dark_text_on_primary,
    secondaryContainer = dark_primary_soft,
    onSecondaryContainer = dark_primary,
	
    // Tertiary
    tertiary = dark_chart_savings,
    onTertiary = dark_background,
    tertiaryContainer = dark_info_soft,
    onTertiaryContainer = dark_chart_savings,
	
    // Background
    background = dark_background,
    onBackground = dark_text,
	
    // Surface
    surface = dark_surface,
    onSurface = dark_text,
    surfaceVariant = dark_background_secondary,
    onSurfaceVariant = dark_text_secondary,
    surfaceTint = dark_primary,
	
    // Inverse
    inverseSurface = dark_text,
    inverseOnSurface = dark_surface,
    inversePrimary = dark_primary_dark,
	
    // Error
    error = dark_error,
    onError = dark_text_on_expense,
    errorContainer = dark_error_soft,
    onErrorContainer = dark_expense_text,
	
    // Outline
    outline = dark_outline,
    outlineVariant = dark_border,
	
    // Scrim
    scrim = Color(0xFF000000)
)

@Composable
fun RendanaTheme(
    themeMode: String = "FollowSystem", 
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "DarkMode" -> true
        "LightMode" -> false
        else -> isSystemInDarkTheme()
    }
	
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val rendanaColors = if (darkTheme) DarkRendanaColors else LightRendanaColors
	
    CompositionLocalProvider(
        LocalRendanaColors provides rendanaColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}