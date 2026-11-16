package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.GamerGenre
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.model.PanelSettings
import com.example.model.ThemeAccentMode
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
fun GamerProfileCard(
    settings: PanelSettings,
    onUpdateCallsign: (String) -> Unit,
    onSelectGenre: (GamerGenre) -> Unit,
    onSelectSlot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingCallsign by remember { mutableStateOf(false) }
    var draftCallsign by remember(settings.gamerCallsign) { mutableStateOf(settings.gamerCallsign) }

    CyberSettingCard(
        title = "Cyber Gamer Profile",
        icon = Icons.Filled.Person,
        accentColor = PhantomCyan,
        trailingBadge = "LVL 42",
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_profile_avatar_1794849919167),
                contentDescription = stringResource(R.string.cd_profile_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(listOf(PhantomCyan, PhantomCrimson)),
                        shape = CircleShape
                    )
            )

            Column(modifier = Modifier.weight(1f)) {
                if (editingCallsign) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = draftCallsign,
                            onValueChange = { draftCallsign = it.take(18) },
                            singleLine = true,
                            label = { Text("Callsign") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PhantomCyan,
                                unfocusedBorderColor = PhantomBorder,
                                focusedTextColor = PhantomText,
                                unfocusedTextColor = PhantomText
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_gamer_callsign")
                        )
                        IconButton(
                            onClick = {
                                onUpdateCallsign(draftCallsign)
                                editingCallsign = false
                            },
                            modifier = Modifier.testTag("save_callsign_btn")
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Save Callsign", tint = PhantomSuccess)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = settings.gamerCallsign,
                            style = MaterialTheme.typography.titleMedium,
                            color = PhantomText
                        )
                        IconButton(
                            onClick = { editingCallsign = true },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("edit_callsign_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Callsign",
                                tint = PhantomCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "APEX TIER // ${settings.gamerGenre.label.uppercase()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = PhantomCrimson
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Preferred Gaming Genre",
            style = MaterialTheme.typography.bodySmall,
            color = PhantomTextDim,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GamerGenre.entries.forEach { genre ->
                val active = genre == settings.gamerGenre
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectGenre(genre) }
                        .testTag("genre_chip_${genre.name.lowercase()}"),
                    color = if (active) PhantomNeonBlue.copy(alpha = 0.18f) else PhantomBgDark,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (active) PhantomCyan else PhantomBorder)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = genre.label.split(" ").first(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (active) PhantomCyan else PhantomTextDim,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Saved HUD Profile Slots",
            style = MaterialTheme.typography.bodySmall,
            color = PhantomTextDim,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("SLOT A", "SLOT B", "SLOT C").forEach { slot ->
                val active = slot == settings.activeSlot
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectSlot(slot) }
                        .testTag("hud_slot_${slot.lowercase().replace(" ", "_")}"),
                    color = if (active) PhantomCrimson.copy(alpha = 0.16f) else PhantomBgDark,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (active) PhantomCrimson else PhantomBorder)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (active) PhantomCrimson else PhantomTextDim
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardSettingsCard(
    settings: PanelSettings,
    isDemoLive: Boolean,
    onSelectThemeAccent: (ThemeAccentMode) -> Unit,
    onUpdateSettings: ((PanelSettings) -> PanelSettings) -> Unit,
    onToggleDemoLive: () -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Dashboard Settings",
        icon = Icons.Filled.Settings,
        accentColor = PhantomCrimson,
        modifier = modifier
    ) {
        Text(
            text = "Cyberpunk Theme Accent",
            style = MaterialTheme.typography.bodySmall,
            color = PhantomTextDim,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeAccentMode.entries.forEach { accent ->
                val active = accent == settings.themeAccent
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectThemeAccent(accent) }
                        .testTag("theme_accent_${accent.name.lowercase()}"),
                    color = if (active) PhantomCyan.copy(alpha = 0.15f) else PhantomBgDark,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (active) PhantomCyan else PhantomBorder)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = accent.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (active) PhantomCyan else PhantomTextDim,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CyberSliderRow(
            label = "UI Animation Speed",
            value = settings.animationSpeed,
            valueRange = 0.5f..2.0f,
            valueFormatted = String.format("%.1fx", settings.animationSpeed),
            accentColor = PhantomCyan,
            onValueChange = { spd -> onUpdateSettings { it.copy(animationSpeed = spd) } },
            testTag = "slider_animation_speed"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onToggleDemoLive)
                    .testTag("settings_toggle_demo_btn"),
                color = if (isDemoLive) PhantomCyan.copy(alpha = 0.16f) else PhantomCrimson.copy(alpha = 0.16f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDemoLive) PhantomCyan else PhantomCrimson)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 13.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isDemoLive) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = if (isDemoLive) PhantomCyan else PhantomCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isDemoLive) "PAUSE DEMO TELEMETRY" else "RESUME DEMO TELEMETRY",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isDemoLive) PhantomCyan else PhantomCrimson
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onResetDefaults)
                    .testTag("settings_reset_defaults_btn"),
                color = PhantomCardElevated,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, PhantomBorder)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 13.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = null,
                        tint = PhantomCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RESTORE CYBER DEFAULTS",
                        style = MaterialTheme.typography.labelLarge,
                        color = PhantomText
                    )
                }
            }
        }
    }
}

@Composable
fun ConsoleLogCard(
    logs: List<LogEntry>,
    modifier: Modifier = Modifier
) {
    CyberSettingCard(
        title = "Telemetry Event Log",
        icon = Icons.Filled.Terminal,
        accentColor = PhantomPurple,
        trailingBadge = "${logs.size}",
        modifier = modifier.testTag("console_log_card")
    ) {
        logs.take(6).forEachIndexed { index, entry ->
            val msgColor = when (entry.level) {
                LogLevel.SUCCESS -> PhantomSuccess
                LogLevel.WARN -> PhantomWarning
                LogLevel.INFO -> PhantomCyan
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = entry.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = PhantomTextDim
                )
                Text(
                    text = entry.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = msgColor,
                    modifier = Modifier.weight(1f)
                )
            }
            if (index < minOf(logs.lastIndex, 5)) {
                HorizontalDivider(color = PhantomBorder.copy(alpha = 0.4f))
            }
        }
    }
}
