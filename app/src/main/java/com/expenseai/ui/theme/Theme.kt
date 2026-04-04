package com.expenseai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    primary = Color(0xFF9CD8AC),
    onPrimary = Color(0xFF003920),
    primaryContainer = Color(0xFF0B5132),
    onPrimaryContainer = Color(0xFFB7E4C7),
    secondary = Color(0xFFB0D1A2),
    onSecondary = Color(0xFF1D3616),
    secondaryContainer = Color(0xFF334D2B),
    onSecondaryContainer = Color(0xFFCCEDBD),
    tertiary = Color(0xFFA3CEDB),
    onTertiary = Color(0xFF023640),
    tertiaryContainer = Color(0xFF224C57),
    onTertiaryContainer = Color(0xFFBFEAF7),
    background = Color(0xFF1A1C19),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DD),
    surfaceVariant = Color(0xFF424940),
    onSurfaceVariant = Color(0xFFC1C9BD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    outline = Color(0xFF8B9389)
)

@Composable
fun ExpenseAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
