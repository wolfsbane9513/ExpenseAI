package com.expenseai.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// App is dark-only by design (OLED fintech palette); no light scheme.
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00FF9D), // Electric Emerald
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF003320),
    onPrimaryContainer = Color(0xFF00FF9D),
    secondary = Color(0xFF00D2FF), // Cyber Blue
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF002533),
    onSecondaryContainer = Color(0xFF00D2FF),
    tertiary = Color(0xFFBD00FF), // Neon Violet
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF240033),
    onTertiaryContainer = Color(0xFFBD00FF),
    background = Color(0xFF000000), // True Black
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0A0A0A), // Deep Zinc
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF121212),
    onSurfaceVariant = Color(0xFF94A3B8), // secondary text, ~7:1 on surface (was #666666, ~3.3:1 — WCAG fail)
    outline = Color(0xFF3F3F46), // was #1A1A1A: invisible on black background
    error = Color(0xFFFF3B30),
    onError = Color.White
)

@Composable
fun FIREOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
