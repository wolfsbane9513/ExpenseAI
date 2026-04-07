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
    primary = Color(0xFF059669),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF0EA5E9),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFE0F2FE),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun ExpenseAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
