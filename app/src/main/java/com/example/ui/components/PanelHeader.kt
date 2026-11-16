package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ThemeAccentMode
import com.example.ui.theme.OrbitronFontFamily
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomCard
import com.example.ui.theme.PhantomCardElevated
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import com.example.ui.theme.PhantomSuccess
import com.example.ui.theme.PhantomTextDim
import com.example.ui.theme.PhantomWarning

@Composable
fun PhantomPanelHeader(
    isDemoLive: Boolean,
    demoFps: Int,
    demoPingMs: Int,
    themeAccent: ThemeAccentMode,
    isCompact: Boolean,
    onToggleLive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_shimmer")
    val shimmerShift by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_shift"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "status_dot_pulse"
    )

    val statusColor by animateColorAsState(
        targetValue = if (isDemoLive) PhantomCyan else PhantomWarning,
        label = "status_color"
    )

    val accentColors = when (themeAccent) {
        ThemeAccentMode.DUAL_CYBER -> listOf(PhantomCyan, PhantomNeonBlue, PhantomCrimson)
        ThemeAccentMode.NEON_BLUE_FOCUS -> listOf(PhantomCyan, PhantomNeonBlue, PhantomCyan)
        ThemeAccentMode.CRIMSON_RED_FOCUS -> listOf(PhantomCrimson, Color(0xFFDC2626), PhantomCyan)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(colors = listOf(PhantomCard, PhantomCardElevated)))
            .drawBehind {
                val startX = size.width * shimmerShift
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = accentColors,
                        startX = startX - size.width * 0.45f,
                        endX = startX + size.width * 0.85f
                    ),
                    size = Size(size.width, 2.5.dp.toPx())
                )
            },
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PhantomBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 12.dp else 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(Brush.linearGradient(colors = listOf(PhantomNeonBlue, PhantomCrimson)))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bolt,
                        contentDescription = stringResource(R.string.cd_logo_icon),
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "PHANTOMX",
                        style = TextStyle(
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isCompact) 16.sp else 20.sp,
                            letterSpacing = 2.sp,
                            brush = Brush.horizontalGradient(colors = listOf(PhantomCyan, PhantomCrimson))
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.app_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        color = PhantomTextDim,
                        letterSpacing = 0.8.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Live Demo FPS + Ping Pill
                Surface(
                    color = PhantomCardElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PhantomBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${demoFps} FPS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PhantomSuccess
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = PhantomTextDim
                        )
                        Text(
                            text = "${demoPingMs}ms",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PhantomCyan
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .testTag("header_status_badge")
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onToggleLive),
                    color = statusColor.copy(alpha = 0.13f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.42f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .alpha(if (isDemoLive) pulseAlpha else 0.7f)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = if (isDemoLive) "LIVE" else "PAUSED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimulationDisclaimerBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("disclaimer_banner"),
        color = PhantomNeonBlue.copy(alpha = 0.10f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, PhantomCyan.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.cd_disclaimer_icon),
                tint = PhantomCyan,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(R.string.disclaimer_text),
                style = MaterialTheme.typography.bodySmall,
                color = PhantomCyan.copy(alpha = 0.92f)
            )
        }
    }
}
