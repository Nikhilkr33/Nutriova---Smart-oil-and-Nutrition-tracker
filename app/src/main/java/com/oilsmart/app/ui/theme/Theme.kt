package com.oilsmart.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AmberContainer = Color(0xFFFFF3E0)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = TextOnGreen,
    primaryContainer = GreenSurface,
    onPrimaryContainer = GreenDark,
    secondary = GreenAccent,
    onSecondary = TextOnGreen,
    secondaryContainer = GreenSurface,
    onSecondaryContainer = GreenDark,
    tertiary = AmberPrimary,
    onTertiary = TextPrimary,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = AmberDark,
    background = NeutralBackground,
    onBackground = TextPrimary,
    surface = NeutralSurface,
    onSurface = TextPrimary,
    surfaceVariant = NeutralCard,
    onSurfaceVariant = TextSecondary,
    outline = NeutralBorder,
    outlineVariant = NeutralDivider,
    error = HealthDanger,
    onError = TextOnGreen
)

@Composable
fun OilSmartTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GreenPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
