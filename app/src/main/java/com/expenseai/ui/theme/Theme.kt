package com.expenseai.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2D6A4F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB7E4C7),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4A6741),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCEDBD),
    onSecondaryContainer = Color(0xFF082004),
    tertiary = Color(0xFF3B6470),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBFEAF7),
    onTertiaryContainer = Color(0xFF001F27),
    background = Color(0xFFF8FAF5),
    onBackground = Color(0xFF1A1C19),
    surface = Color(0xFFF8FAF5),
    onSurface = Color(0xFF1A1C19),
    surfaceVariant = Color(0xFFDDE5D9),
    onSurfaceVariant = Color(0xFF424940),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    outline = Color(0xFF72796F)
)
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
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFF1A1A1A),
    error = Color(0xFFFF3B30),
    onError = Color.White
)

@Composable
fun ExpenseAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
