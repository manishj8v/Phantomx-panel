package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Layers
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.HudCornerPosition
import com.example.model.HudLayoutPreset
import com.example.model.PanelSettings
import com.example.model.PanelUiState
import com.example.ui.theme.PhantomBgDark
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomCard
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import com.example.ui.theme.PhantomSuccess
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextDim

@Composable
fun HudLayoutPreviewCard(
    uiState: PanelUiState,
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val animatedOpacity by animateFloatAsState(targetValue = settings.hudOpacity, label = "hud_opacity")
    val animatedScale by animateFloatAsState(targetValue = settings.hudScale, label = "hud_scale")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hud_layout_preview_card"),
        color = PhantomCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PhantomBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VISUAL HUD LAYOUT PREVIEW",
                    style = MaterialTheme.typography.titleSmall,
                    color = PhantomCyan
                )
                Surface(
                    color = PhantomNeonBlue.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = settings.hudPreset.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = PhantomCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFF0A0D16))
            ) {
                if (settings.showBackdropArt) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cyber_banner_1794849917824),
                        contentDescription = stringResource(R.string.cd_cyber_banner),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.55f)
                    )
                }

                // Subtle CRT Scanlines + Corner Brackets
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (settings.showScanlines) {
                        val step = 4.dp.toPx()
                        var y = 0f
                        while (y < size.height) {
                            drawLine(
                                Color.Black.copy(alpha = 0.18f),
                                Offset(0f, y),
                                Offset(size.width, y),
                                1.dp.toPx()
                            )
                            y += step
                        }
                    }

                    if (settings.showCornerBrackets) {
                        val len = 22.dp.toPx()
                        val inset = 12.dp.toPx()
                        val stroke = 2.dp.toPx()
                        val bracketColor = PhantomCyan.copy(alpha = animatedOpacity * 0.75f)
                        // Top-Left
                        drawLine(bracketColor, Offset(inset, inset), Offset(inset + len, inset), stroke)
                        drawLine(bracketColor, Offset(inset, inset), Offset(inset, inset + len), stroke)
                        // Top-Right
                        drawLine(bracketColor, Offset(size.width - inset, inset), Offset(size.width - inset - len, inset), stroke)
                        drawLine(bracketColor, Offset(size.width - inset, inset), Offset(size.width - inset, inset + len), stroke)
                        // Bottom-Left
                        drawLine(bracketColor, Offset(inset, size.height - inset), Offset(inset + len, size.height - inset), stroke)
                        drawLine(bracketColor, Offset(inset, size.height - inset), Offset(inset, size.height - inset - len), stroke)
                        // Bottom-Right
                        drawLine(bracketColor, Offset(size.width - inset, size.height - inset), Offset(size.width - inset - len, size.height - inset), stroke)
                        drawLine(bracketColor, Offset(size.width - inset, size.height - inset), Offset(size.width - inset, size.height - inset - len), stroke)
                    }
                }

                // Centered Animated Reticle Preview
                AnimatedCrosshairCanvas(
                    settings = settings,
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.Center)
                        .alpha(animatedOpacity)
                )

                // Movable Telemetry Overlay Badge
                if (settings.showFpsOverlay || settings.showPingOverlay) {
                    val alignment = when (settings.fpsCorner) {
                        HudCornerPosition.TOP_LEFT -> Alignment.TopStart
                        HudCornerPosition.TOP_RIGHT -> Alignment.TopEnd
                        HudCornerPosition.BOTTOM_LEFT -> Alignment.BottomStart
                        HudCornerPosition.BOTTOM_RIGHT -> Alignment.BottomEnd
                    }
                    Surface(
                        modifier = Modifier
                            .align(alignment)
                            .padding(18.dp)
                            .graphicsLayer(
                                alpha = animatedOpacity,
                                scaleX = animatedScale,
                                scaleY = animatedScale
                            ),
                        color = PhantomBgDark.copy(alpha = 0.82f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PhantomCyan.copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (settings.showFpsOverlay) {
                                Text(
                                    text = "${uiState.demoFps} FPS",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PhantomSuccess
                                )
                            }
                            if (settings.showPingOverlay) {
                                Text(
                                    text = "${uiState.demoPingMs} ms",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PhantomCyan
                                )
                            }
                        }
                    }
                }

                // Mini Graph Overlay in opposite bottom corner
                if (settings.showMiniGraphOverlay) {
                    val graphAlign = if (settings.fpsCorner == HudCornerPosition.BOTTOM_LEFT) {
                        Alignment.BottomEnd
                    } else {
                        Alignment.BottomStart
                    }
                    Surface(
                        modifier = Modifier
                            .align(graphAlign)
                            .padding(18.dp)
                            .width(126.dp)
                            .height(50.dp)
                            .graphicsLayer(alpha = animatedOpacity),
                        color = PhantomBgDark.copy(alpha = 0.80f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PhantomCrimson.copy(alpha = 0.45f))
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                        ) {
                            val history = uiState.fpsHistory
                            if (history.size >= 2) {
                                val path = Path()
                                history.forEachIndexed { idx, value ->
                                    val x = (idx.toFloat() / (history.size - 1)) * size.width
                                    val norm = ((value - 45f) / 110f).coerceIn(0.1f, 0.9f)
                                    val y = size.height - norm * size.height
                                    if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                                }
                                drawPath(
                                    path = path,
                                    brush = Brush.horizontalGradient(listOf(PhantomCyan, PhantomCrimson)),
                                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
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
fun HudPresetSelectorCard(
    settings: PanelSettings,
    onSelectPreset: (HudLayoutPreset) -> Unit,
    onSelectCorner: (HudCornerPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "HUD Layout Presets",
        icon = Icons.Filled.GridView,
        accentColor = PhantomCyan,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HudLayoutPreset.entries.forEach { preset ->
                val selected = preset == settings.hudPreset
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectPreset(preset) }
                        .testTag("hud_preset_${preset.name.lowercase()}"),
                    color = if (selected) PhantomNeonBlue.copy(alpha = 0.16f) else PhantomBgDark,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (selected) PhantomCyan else PhantomBorder)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                        Text(
                            text = preset.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) PhantomCyan else PhantomText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = preset.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = PhantomTextDim
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "FPS / Ping Widget Corner",
                style = MaterialTheme.typography.bodyMedium,
                color = PhantomTextDim
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HudCornerPosition.entries.forEach { corner ->
                    val active = corner == settings.fpsCorner
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelectCorner(corner) }
                            .testTag("hud_corner_${corner.name.lowercase()}"),
                        color = if (active) PhantomCrimson.copy(alpha = 0.16f) else PhantomBgDark,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (active) PhantomCrimson else PhantomBorder)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = corner.shortTag,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (active) PhantomCrimson else PhantomTextDim
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HudLayerControlsCard(
    settings: PanelSettings,
    onUpdateSettings: ((PanelSettings) -> PanelSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "HUD Visual Modules",
        icon = Icons.Filled.Layers,
        accentColor = PhantomCrimson,
        modifier = modifier
    ) {
        CyberSliderRow(
            label = "Overlay Opacity",
            value = settings.hudOpacity * 100f,
            valueRange = 35f..100f,
            valueFormatted = SliderValueLabel(settings.hudOpacity * 100f, "%"),
            accentColor = PhantomCyan,
            onValueChange = { pct -> onUpdateSettings { it.copy(hudOpacity = pct / 100f) } },
            testTag = "slider_hud_opacity"
        )
        CyberSliderRow(
            label = "Badge Scale",
            value = settings.hudScale * 100f,
            valueRange = 80f..125f,
            valueFormatted = SliderValueLabel(settings.hudScale * 100f, "%"),
            accentColor = PhantomCrimson,
            onValueChange = { pct -> onUpdateSettings { it.copy(hudScale = pct / 100f) } },
            testTag = "slider_hud_scale"
        )

        CyberToggleRow(
            label = "FPS Counter Widget",
            checked = settings.showFpsOverlay,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showFpsOverlay = checked) } },
            testTag = "toggle_fps_overlay"
        )
        CyberToggleRow(
            label = "Ping Latency Widget",
            checked = settings.showPingOverlay,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showPingOverlay = checked) } },
            testTag = "toggle_ping_overlay"
        )
        CyberToggleRow(
            label = "Mini Sparkline Widget",
            checked = settings.showMiniGraphOverlay,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showMiniGraphOverlay = checked) } },
            testTag = "toggle_mini_graph"
        )
        CyberToggleRow(
            label = "Holographic Corner Brackets",
            checked = settings.showCornerBrackets,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showCornerBrackets = checked) } },
            testTag = "toggle_corner_brackets"
        )
        CyberToggleRow(
            label = "CRT Scanline Texture",
            checked = settings.showScanlines,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showScanlines = checked) } },
            testTag = "toggle_hud_scanlines"
        )
        CyberToggleRow(
            label = "Sci-Fi Horizon Backdrop",
            checked = settings.showBackdropArt,
            onCheckedChange = { checked -> onUpdateSettings { it.copy(showBackdropArt = checked) } },
            testTag = "toggle_backdrop_art"
        )
    }
}
