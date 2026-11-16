package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val PhantomColorScheme = darkColorScheme(
    primary = PhantomCrimson,
    onPrimary = Color.White,
    primaryContainer = PhantomCrimson.copy(alpha = 0.18f),
    onPrimaryContainer = PhantomCrimson,
    secondary = PhantomCyan,
    onSecondary = PhantomBgDark,
    secondaryContainer = PhantomCyan.copy(alpha = 0.15f),
    onSecondaryContainer = PhantomCyan,
    tertiary = PhantomPurple,
    onTertiary = Color.White,
    tertiaryContainer = PhantomPurple.copy(alpha = 0.18f),
    onTertiaryContainer = PhantomPurple,
    background = PhantomBgDark,
    onBackground = PhantomText,
    surface = PhantomCard,
    onSurface = PhantomText,
    surfaceVariant = PhantomCardElevated,
    onSurfaceVariant = PhantomTextDim,
    outline = PhantomBorder,
    outlineVariant = PhantomBorder.copy(alpha = 0.6f),
    error = PhantomCrimson,
    onError = Color.White
)

private val PhantomShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

@Composable
fun PhantomXTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = PhantomColorScheme,
        typography = PhantomTypography,
        shapes = PhantomShapes,
        content = content
    )
}
