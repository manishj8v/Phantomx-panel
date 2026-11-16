package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.ThemeAccentMode
import com.example.ui.theme.PhantomBgDark
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AmbientCyberBackground(
    themeAccent: ThemeAccentMode = ThemeAccentMode.DUAL_CYBER,
    animationSpeed: Float = 1.0f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val duration = (12000f / animationSpeed.coerceIn(0.4f, 2.2f)).toInt()
    val infiniteTransition = rememberInfiniteTransition(label = "bg_glow_transition")
    val driftPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bg_drift_phase"
    )

    val primaryOrb = when (themeAccent) {
        ThemeAccentMode.DUAL_CYBER -> PhantomCrimson
        ThemeAccentMode.NEON_BLUE_FOCUS -> PhantomCyan
        ThemeAccentMode.CRIMSON_RED_FOCUS -> PhantomCrimson
    }
    val secondaryOrb = when (themeAccent) {
        ThemeAccentMode.DUAL_CYBER -> PhantomCyan
        ThemeAccentMode.NEON_BLUE_FOCUS -> PhantomNeonBlue
        ThemeAccentMode.CRIMSON_RED_FOCUS -> Color(0xFF991B1B)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PhantomBgDark)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stepPx = 38.dp.toPx()
            val gridColor = secondaryOrb.copy(alpha = 0.045f)

            var x = 0f
            while (x <= size.width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += stepPx
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += stepPx
            }

            val driftX = cos(driftPhase.toDouble()).toFloat() * 44.dp.toPx()
            val driftY = sin(driftPhase.toDouble()).toFloat() * 34.dp.toPx()

            val g1Center = Offset(size.width * 0.14f + driftX, size.height * 0.10f - driftY)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryOrb.copy(alpha = 0.17f), Color.Transparent),
                    center = g1Center,
                    radius = size.maxDimension * 0.50f
                ),
                center = g1Center,
                radius = size.maxDimension * 0.50f
            )

            val g2Center = Offset(size.width * 0.86f - driftX, size.height * 0.88f + driftY)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(secondaryOrb.copy(alpha = 0.16f), Color.Transparent),
                    center = g2Center,
                    radius = size.maxDimension * 0.48f
                ),
                center = g2Center,
                radius = size.maxDimension * 0.48f
            )
        }
        content()
    }
}
