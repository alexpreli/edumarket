package com.edumarket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary          = EduBlue,
    onPrimary        = EduOnPrimary,
    primaryContainer = EduBlueLight,
    secondary        = EduAmber,
    onSecondary      = EduOnSecondary,
    background       = EduBackground,
    onBackground     = EduOnBackground,
    surface          = EduSurface,
    onSurface        = EduOnSurface,
    surfaceVariant   = EduSurfaceVar,
    outline          = EduOutline,
    error            = Color_Error_Light
)

private val DarkColorScheme = darkColorScheme(
    primary          = EduBlueDarkTheme,
    onPrimary        = EduOnPrimaryDark,
    secondary        = EduAmberDarkTheme,
    background       = EduBackgroundDark,
    onBackground     = EduOnBackgroundDark,
    surface          = EduSurfaceDark,
    onSurface        = EduOnBackgroundDark
)

@Composable
fun EduMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}