package com.fastingtimerpro.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = AppColors.primaryAccent,
    secondary = AppColors.secondaryAccent,
    background = AppColors.baseBackground,
    surface = AppColors.gradientTop,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = AppColors.primaryText,
    onSurface = AppColors.primaryText
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF0B6AA9),
    secondary = Color(0xFF0E8F5C),
    background = Color(0xFFF5F7FB),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0A0F18),
    onSurface = Color(0xFF0A0F18)
)

@Composable
fun FastingTimerProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
