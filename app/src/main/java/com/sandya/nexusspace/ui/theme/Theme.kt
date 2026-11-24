package com.sandya.nexusspace.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NexusGreen,
    onPrimary = Color(0xFF000000),
    primaryContainer = NexusGreenDark,
    onPrimaryContainer = NexusGreenLight,
    secondary = NexusGreenLight,
    onSecondary = Color(0xFF000000),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFFB3B3B3),
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = NexusGreen,
    onPrimary = LightSurface,
    primaryContainer = NexusGreenLight,
    onPrimaryContainer = NexusGreenDark,
    secondary = NexusGreenDark,
    onSecondary = LightSurface,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = ErrorColor
)

@Composable
fun NexusSpaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
