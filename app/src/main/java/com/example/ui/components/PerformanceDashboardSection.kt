package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PanelUiState
import com.example.model.PerformanceProfileMode
import com.example.ui.theme.PhantomBgDark
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomCard
import com.example.ui.theme.PhantomCardElevated
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomSuccess
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextDim
import com.example.ui.theme.PhantomWarning

@Composable
fun PerformanceHeroCard(
    uiState: PanelUiState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("performance_hero_card"),
        color = PhantomCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PhantomBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cyber_banner_1794849917824),
                contentDescription = stringResource(R.string.cd_cyber_banner),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.38f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                PhantomBgDark.copy(alpha = 0.55f),
                                PhantomCard.copy(alpha = 0.92f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PERFORMANCE TELEMETRY",
                            style = MaterialTheme.typography.titleSmall,
                            color = PhantomCyan
                        )
                        Text(
                            text = "Demo Profile: ${uiState.settings.performanceMode.label}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PhantomTextDim
                        )
                    }

                    Surface(
                        color = PhantomCrimson.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, PhantomCrimson.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = formatSessionTime(uiState.sessionSeconds),
                            style = MaterialTheme.typography.labelMedium,
                            color = PhantomCrimson,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeroStatTile(
                        label = "FRAME RATE",
                        value = "${uiState.demoFps}",
                        unit = "FPS",
                        accent = PhantomCyan,
                        modifier = Modifier.weight(1f)
                    )
                    HeroStatTile(
                        label = "PING LATENCY",
                        value = "${uiState.demoPingMs}",
                        unit = "MS",
                        accent = PhantomCrimson,
                        modifier = Modifier.weight(1f)
                    )
                    HeroStatTile(
                        label = "FRAME TIME",
                        value = String.format("%.1f", uiState.demoFrameTimeMs),
                        unit = "MS",
                        accent = PhantomPurple,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroStatTile(
    label: String,
    value: String,
    unit: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = PhantomBgDark.copy(alpha = 0.78f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = PhantomTextDim,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    color = accent
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = PhantomTextDim,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun TelemetryGaugesGrid(
    uiState: PanelUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ArcGaugeCard(
            title = "FPS SYNC",
            valueText = "${uiState.demoFps}",
            progress = (uiState.demoFps / 144f).coerceIn(0.1f, 1f),
            accent = PhantomCyan,
            modifier = Modifier.weight(1f)
        )
        ArcGaugeCard(
            title = "GPU LOAD",
            valueText = "${uiState.demoGpuLoad}%",
            progress = (uiState.demoGpuLoad / 100f).coerceIn(0.1f, 1f),
            accent = PhantomCrimson,
            modifier = Modifier.weight(1f)
        )
        ArcGaugeCard(
            title = "CPU LOAD",
            valueText = "${uiState.demoCpuLoad}%",
            progress = (uiState.demoCpuLoad / 100f).coerceIn(0.1f, 1f),
            accent = PhantomNeonBlue,
            modifier = Modifier.weight(1f)
        )
        ArcGaugeCard(
            title = "THERMAL",
            valueText = "${uiState.demoTempC}°C",
            progress = (uiState.demoTempC / 90f).coerceIn(0.1f, 1f),
            accent = if (uiState.demoTempC > 70) PhantomWarning else PhantomSuccess,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ArcGaugeCard(
    title: String,
    valueText: String,
    progress: Float,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = 0.78f),
        label = "gauge_progress"
    )

    Surface(
        modifier = modifier,
        color = PhantomCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, PhantomBorder)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 5.dp.toPx()
                    drawArc(
                        color = PhantomBorder,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = accent,
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PhantomText
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = PhantomTextDim,
                maxLines = 1
            )
        }
    }
}

@Composable
fun LiveSparklineCard(
    fpsHistory: List<Float>,
    pingHistory: List<Float>,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Frame & Latency Graph",
        icon = Icons.Filled.NetworkPing,
        accentColor = PhantomCyan,
        trailingBadge = "DEMO LIVE",
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PhantomBgDark)
                .padding(8.dp)
        ) {
            val w = size.width
            val h = size.height

            // Grid lines
            for (i in 1..3) {
                val gy = h * (i / 4f)
                drawLine(
                    color = PhantomBorder.copy(alpha = 0.5f),
                    start = Offset(0f, gy),
                    end = Offset(w, gy),
                    strokeWidth = 1f
                )
            }

            if (fpsHistory.size >= 2) {
                val fpsPath = Path()
                fpsHistory.forEachIndexed { idx, value ->
                    val x = (idx.toFloat() / (fpsHistory.size - 1)) * w
                    val norm = ((value - 45f) / 110f).coerceIn(0.08f, 0.95f)
                    val y = h - (norm * h)
                    if (idx == 0) fpsPath.moveTo(x, y) else fpsPath.lineTo(x, y)
                }
                drawPath(
                    path = fpsPath,
                    color = PhantomCyan,
                    style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            if (pingHistory.size >= 2) {
                val pingPath = Path()
                pingHistory.forEachIndexed { idx, value ->
                    val x = (idx.toFloat() / (pingHistory.size - 1)) * w
                    val norm = (value / 35f).coerceIn(0.08f, 0.85f)
                    val y = h - (norm * h)
                    if (idx == 0) pingPath.moveTo(x, y) else pingPath.lineTo(x, y)
                }
                drawPath(
                    path = pingPath,
                    color = PhantomCrimson,
                    style = Stroke(width = 2.0.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).background(PhantomCyan, RoundedCornerShape(2.dp)))
                Text("Frame Rate (FPS)", style = MaterialTheme.typography.labelSmall, color = PhantomTextDim)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).background(PhantomCrimson, RoundedCornerShape(2.dp)))
                Text("Ping (ms)", style = MaterialTheme.typography.labelSmall, color = PhantomTextDim)
            }
        }
    }
}

@Composable
fun PerformanceModeSelectorCard(
    selectedMode: PerformanceProfileMode,
    onSelectMode: (PerformanceProfileMode) -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Dashboard Display Mode",
        icon = Icons.Filled.Speed,
        accentColor = PhantomCrimson,
        trailingBadge = "${selectedMode.targetFps}Hz",
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PerformanceProfileMode.entries.chunked(2).forEach { rowModes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowModes.forEach { mode ->
                        val active = mode == selectedMode
                        val icon = when (mode) {
                            PerformanceProfileMode.BALANCED -> Icons.Filled.Memory
                            PerformanceProfileMode.HYPER_FRAME -> Icons.Filled.Speed
                            PerformanceProfileMode.LOW_LATENCY -> Icons.Filled.NetworkPing
                            PerformanceProfileMode.ECO_COOL -> Icons.Filled.Thermostat
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectMode(mode) }
                                .testTag("perf_mode_${mode.name.lowercase()}"),
                            color = if (active) PhantomCrimson.copy(alpha = 0.14f) else PhantomBgDark,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (active) PhantomCrimson else PhantomBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = mode.label,
                                    tint = if (active) PhantomCrimson else PhantomCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = mode.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (active) PhantomText else PhantomTextDim
                                    )
                                    Text(
                                        text = "${mode.targetFps} FPS • ${mode.basePing}ms",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (active) PhantomCyan else PhantomTextDim
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatSessionTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
