package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.CrosshairColorOption
import com.example.model.CrosshairStyle
import com.example.model.PanelSettings
import com.example.ui.theme.PhantomBgDark
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomCard
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextDim
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedCrosshairCanvas(
    settings: PanelSettings,
    overrideStyle: CrosshairStyle? = null,
    scaleMultiplier: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val style = overrideStyle ?: settings.crosshairStyle
    val infiniteTransition = rememberInfiniteTransition(label = "crosshair_anim_${style.name}")
    val speed = settings.animationSpeed.coerceIn(0.4f, 2.2f)

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (4200f / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reticle_rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (1100f / speed).toInt()),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reticle_pulse"
    )

    val activePhase = if (settings.animateCrosshair) phase else 0f
    val activePulse = if (settings.animateCrosshair) pulse else 0.5f

    Canvas(modifier = modifier) {
        drawAnimatedReticle(
            center = center,
            style = style,
            color = settings.crosshairColor.color,
            sizeDp = settings.crosshairSize * scaleMultiplier,
            gapDp = settings.crosshairGap * scaleMultiplier,
            thicknessDp = settings.crosshairThickness * scaleMultiplier.coerceAtMost(1.5f),
            glow = settings.crosshairGlow,
            rotationDeg = activePhase,
            pulseNorm = activePulse
        )
    }
}

@Composable
fun CrosshairGalleryShowcaseCard(
    settings: PanelSettings,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("crosshair_showcase_card"),
        color = PhantomCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PhantomBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ANIMATED CROSSHAIR GALLERY",
                        style = MaterialTheme.typography.titleSmall,
                        color = PhantomCyan
                    )
                    Text(
                        text = "${settings.crosshairStyle.label} • Visual Preview Only",
                        style = MaterialTheme.typography.bodySmall,
                        color = PhantomTextDim
                    )
                }
                Surface(
                    color = PhantomCrimson.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, PhantomCrimson.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = settings.crosshairColor.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = settings.crosshairColor.color,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PhantomBgDark)
                        .border(1.dp, PhantomBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CalibrationGridBackground()
                    AnimatedCrosshairCanvas(
                        settings = settings,
                        scaleMultiplier = 1.0f,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "1.0x LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = PhantomTextDim,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PhantomBgDark)
                        .border(1.dp, PhantomBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CalibrationGridBackground()
                    AnimatedCrosshairCanvas(
                        settings = settings,
                        scaleMultiplier = 1.65f,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "1.65x ZOOM",
                        style = MaterialTheme.typography.labelSmall,
                        color = PhantomCyan,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CrosshairDesignGridCard(
    settings: PanelSettings,
    onSelectStyle: (CrosshairStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Reticle Design Gallery",
        icon = Icons.Filled.AutoAwesome,
        accentColor = PhantomCrimson,
        trailingBadge = "6 STYLES",
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CrosshairStyle.entries.chunked(2).forEach { rowStyles ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowStyles.forEach { style ->
                        val selected = style == settings.crosshairStyle
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectStyle(style) }
                                .testTag("gallery_style_${style.name.lowercase()}"),
                            color = if (selected) PhantomCrimson.copy(alpha = 0.14f) else PhantomBgDark,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selected) PhantomCrimson else PhantomBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(74.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF090B12)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AnimatedCrosshairCanvas(
                                        settings = settings.copy(
                                            crosshairSize = 18f,
                                            crosshairGap = 5f,
                                            crosshairThickness = 2.2f
                                        ),
                                        overrideStyle = style,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = style.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) PhantomCrimson else PhantomText,
                                    maxLines = 1
                                )
                                Text(
                                    text = style.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PhantomTextDim,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CrosshairStyleControlsCard(
    settings: PanelSettings,
    onUpdateSettings: ((PanelSettings) -> PanelSettings) -> Unit,
    onSelectColor: (CrosshairColorOption) -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Reticle Visual Styling",
        icon = Icons.Filled.CenterFocusStrong,
        accentColor = PhantomPurple,
        modifier = modifier
    ) {
        CrosshairColorPicker(
            selectedColor = settings.crosshairColor,
            onSelectColor = onSelectColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        CyberSliderRow(
            label = "Design Size",
            value = settings.crosshairSize,
            valueRange = 12f..40f,
            valueFormatted = SliderValueLabel(settings.crosshairSize, "dp"),
            accentColor = PhantomCyan,
            onValueChange = { v -> onUpdateSettings { it.copy(crosshairSize = v) } },
            testTag = "slider_reticle_size"
        )
        CyberSliderRow(
            label = "Inner Spacing",
            value = settings.crosshairGap,
            valueRange = 2f..18f,
            valueFormatted = SliderValueLabel(settings.crosshairGap, "dp"),
            accentColor = PhantomCrimson,
            onValueChange = { v -> onUpdateSettings { it.copy(crosshairGap = v) } },
            testTag = "slider_reticle_gap"
        )
        CyberSliderRow(
            label = "Line Weight",
            value = settings.crosshairThickness,
            valueRange = 1.5f..5.0f,
            valueFormatted = String.format("%.1fdp", settings.crosshairThickness),
            accentColor = PhantomPurple,
            onValueChange = { v -> onUpdateSettings { it.copy(crosshairThickness = v) } },
            testTag = "slider_reticle_thickness"
        )

        CyberToggleRow(
            label = "Animate Reticle Motion",
            checked = settings.animateCrosshair,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(animateCrosshair = checked) } },
            testTag = "toggle_animate_crosshair"
        )
        CyberToggleRow(
            label = "Neon Bloom Aura",
            checked = settings.crosshairGlow,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(crosshairGlow = checked) } },
            testTag = "toggle_crosshair_glow"
        )
    }
}

@Composable
private fun CalibrationGridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 18.dp.toPx()
        val lineColor = PhantomBorder.copy(alpha = 0.45f)
        var x = 0f
        while (x <= size.width) {
            drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), 1f)
            x += step
        }
        var y = 0f
        while (y <= size.height) {
            drawLine(lineColor, Offset(0f, y), Offset(size.width, y), 1f)
            y += step
        }
    }
}

private fun DrawScope.drawAnimatedReticle(
    center: Offset,
    style: CrosshairStyle,
    color: Color,
    sizeDp: Float,
    gapDp: Float,
    thicknessDp: Float,
    glow: Boolean,
    rotationDeg: Float,
    pulseNorm: Float
) {
    val len = (sizeDp * 0.55f).dp.toPx()
    val gap = (gapDp + (pulseNorm * 2.5f)).dp.toPx()
    val stroke = thicknessDp.dp.toPx()
    val glowColor = color.copy(alpha = 0.28f)

    fun drawSeg(start: Offset, end: Offset, segColor: Color = color) {
        if (glow) {
            drawLine(glowColor, start, end, stroke + 5f, StrokeCap.Round)
        }
        drawLine(segColor, start, end, stroke, StrokeCap.Round)
    }

    when (style) {
        CrosshairStyle.CYBER_PULSE -> {
            drawSeg(Offset(center.x, center.y - gap - len), Offset(center.x, center.y - gap))
            drawSeg(Offset(center.x, center.y + gap), Offset(center.x, center.y + gap + len))
            drawSeg(Offset(center.x - gap - len, center.y), Offset(center.x - gap, center.y))
            drawSeg(Offset(center.x + gap, center.y), Offset(center.x + gap + len, center.y))
            drawCircle(color, radius = (stroke * 0.9f), center = center)
        }

        CrosshairStyle.ORBITAL_RING -> {
            val radius = gap + len * 0.72f
            if (glow) {
                drawCircle(glowColor, radius = radius, center = center, style = Stroke(stroke + 4f))
            }
            rotate(rotationDeg, pivot = center) {
                drawArc(
                    color = color,
                    startAngle = 0f,
                    sweepAngle = 70f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = color,
                    startAngle = 180f,
                    sweepAngle = 70f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )
            }
            rotate(-rotationDeg * 1.4f, pivot = center) {
                val innerR = radius * 0.56f
                drawArc(
                    color = PhantomCrimson,
                    startAngle = 90f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(center.x - innerR, center.y - innerR),
                    size = Size(innerR * 2f, innerR * 2f),
                    style = Stroke(stroke * 0.85f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = PhantomCrimson,
                    startAngle = 270f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(center.x - innerR, center.y - innerR),
                    size = Size(innerR * 2f, innerR * 2f),
                    style = Stroke(stroke * 0.85f, cap = StrokeCap.Round)
                )
            }
            drawCircle(color, radius = 2.5.dp.toPx(), center = center)
        }

        CrosshairStyle.QUANTUM_DOT -> {
            val outerR = (gap + len * 0.45f) * (0.85f + pulseNorm * 0.35f)
            if (glow) {
                drawCircle(color.copy(alpha = 0.16f), radius = outerR + 4.dp.toPx(), center = center)
            }
            drawCircle(color.copy(alpha = 0.7f), radius = outerR, center = center, style = Stroke(stroke * 0.8f))
            drawCircle(color, radius = (stroke * 1.35f).coerceAtLeast(3.dp.toPx()), center = center)
        }

        CrosshairStyle.NEON_X_WING -> {
            rotate(45f, pivot = center) {
                drawSeg(Offset(center.x, center.y - gap - len), Offset(center.x, center.y - gap))
                drawSeg(Offset(center.x, center.y + gap), Offset(center.x, center.y + gap + len))
                drawSeg(Offset(center.x - gap - len, center.y), Offset(center.x - gap, center.y))
                drawSeg(Offset(center.x + gap, center.y), Offset(center.x + gap + len, center.y))
            }
            drawCircle(PhantomCyan, radius = 2.2.dp.toPx(), center = center)
        }

        CrosshairStyle.PRISM_CORE -> {
            val r = gap + len * 0.8f
            rotate(rotationDeg * 0.65f, pivot = center) {
                val pts = listOf(
                    Offset(center.x, center.y - r),
                    Offset(center.x + r, center.y),
                    Offset(center.x, center.y + r),
                    Offset(center.x - r, center.y)
                )
                for (i in pts.indices) {
                    drawSeg(pts[i], pts[(i + 1) % pts.size])
                }
            }
            drawCircle(color, radius = 2.5.dp.toPx(), center = center)
        }

        CrosshairStyle.HOLO_BRACKET -> {
            val r = gap + len * 0.78f
            val wing = r * 0.48f
            // 4 dynamic corner brackets
            drawSeg(Offset(center.x - r, center.y - r), Offset(center.x - r + wing, center.y - r))
            drawSeg(Offset(center.x - r, center.y - r), Offset(center.x - r, center.y - r + wing))

            drawSeg(Offset(center.x + r, center.y - r), Offset(center.x + r - wing, center.y - r))
            drawSeg(Offset(center.x + r, center.y - r), Offset(center.x + r, center.y - r + wing))

            drawSeg(Offset(center.x - r, center.y + r), Offset(center.x - r + wing, center.y + r))
            drawSeg(Offset(center.x - r, center.y + r), Offset(center.x - r, center.y + r - wing))

            drawSeg(Offset(center.x + r, center.y + r), Offset(center.x + r - wing, center.y + r))
            drawSeg(Offset(center.x + r, center.y + r), Offset(center.x + r, center.y + r - wing))

            val pulseRadius = 2.2.dp.toPx() + pulseNorm * 1.6.dp.toPx()
            drawCircle(color, radius = pulseRadius, center = center)
        }
    }
}
