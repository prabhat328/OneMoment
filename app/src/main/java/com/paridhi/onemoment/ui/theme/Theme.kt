package com.paridhi.onemoment.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = Color.White,
    primaryContainer = SoftLavender,
    onPrimaryContainer = CharcoalText,
    secondary = PastelSage,
    onSecondary = CharcoalText,
    tertiary = DustyPeach,
    onTertiary = CharcoalText,
    background = WarmIvory,
    onBackground = CharcoalText,
    surface = WarmIvory,
    onSurface = CharcoalText,
    surfaceVariant = SoftLavender,
    onSurfaceVariant = CharcoalText
)

// A simplified dark scheme that respects the pastel vibe but with darker tones
private val DarkColorScheme = darkColorScheme(
    primary = PastelSage,
    onPrimary = DarkSageGreen,
    secondary = SageGreen,
    onSecondary = Color.White,
    tertiary = MutedPeach,
    background = CharcoalText,
    onBackground = WarmIvory,
    surface = CharcoalText,
    onSurface = WarmIvory
)

@Composable
fun OneMomentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
