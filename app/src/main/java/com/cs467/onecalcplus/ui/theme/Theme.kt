package com.cs467.onecalcplus.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Surface,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Surface,
    secondary = Secondary,
    onSecondary = Surface,
    tertiary = Tertiary,
    onTertiary = Surface,
    background = Surface,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerLow,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error,
    outlineVariant = OutlineVariant
)

@Composable
fun OneCalcPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled to strictly follow the "Kinetic Brutalism" design
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always use dark theme as per DESIGN.md "obsidian-toned environment"

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
