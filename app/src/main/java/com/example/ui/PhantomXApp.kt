package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.DashboardSection
import com.example.model.PanelUiState
import com.example.ui.components.AmbientCyberBackground
import com.example.ui.components.ConsoleLogCard
import com.example.ui.components.CrosshairDesignGridCard
import com.example.ui.components.CrosshairGalleryShowcaseCard
import com.example.ui.components.CrosshairStyleControlsCard
import com.example.ui.components.DashboardSettingsCard
import com.example.ui.components.GamerProfileCard
import com.example.ui.components.HudLayerControlsCard
import com.example.ui.components.HudLayoutPreviewCard
import com.example.ui.components.HudPresetSelectorCard
import com.example.ui.components.LiveSparklineCard
import com.example.ui.components.PerformanceHeroCard
import com.example.ui.components.PerformanceModeSelectorCard
import com.example.ui.components.PhantomPanelHeader
import com.example.ui.components.SimulationDisclaimerBanner
import com.example.ui.components.TelemetryGaugesGrid
import com.example.ui.theme.PhantomCard
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextDim

@Composable
fun PhantomXApp(
    viewModel: PanelViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.activeSection != DashboardSection.PERFORMANCE) {
        BackHandler {
            viewModel.selectSection(DashboardSection.PERFORMANCE)
        }
    }

    AmbientCyberBackground(
        themeAccent = uiState.settings.themeAccent,
        animationSpeed = uiState.settings.animationSpeed
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isCompact = maxWidth < 600.dp
            val isExpanded = maxWidth >= 960.dp

            if (isExpanded) {
                ExpandedDashboardLayout(uiState = uiState, viewModel = viewModel)
            } else if (!isCompact) {
                MediumRailLayout(uiState = uiState, viewModel = viewModel)
            } else {
                CompactPhoneLayout(uiState = uiState, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun CompactPhoneLayout(
    uiState: PanelUiState,
    viewModel: PanelViewModel
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = PhantomCard.copy(alpha = 0.96f),
                contentColor = PhantomText,
                windowInsets = WindowInsets.navigationBars,
                tonalElevation = 0.dp
            ) {
                DashboardSection.entries.forEach { section ->
                    val selected = section == uiState.activeSection
                    NavigationBarItem(
                        selected = selected,
                        onClick = { viewModel.selectSection(section) },
                        modifier = Modifier.testTag("nav_tab_${section.name.lowercase()}"),
                        icon = {
                            Icon(
                                imageVector = sectionIcon(section, selected),
                                contentDescription = section.title
                            )
                        },
                        label = {
                            Text(
                                text = sectionShortLabel(section),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PhantomCyan,
                            selectedTextColor = PhantomCyan,
                            indicatorColor = PhantomNeonBlue.copy(alpha = 0.20f),
                            unselectedIconColor = PhantomTextDim,
                            unselectedTextColor = PhantomTextDim
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PhantomPanelHeader(
                isDemoLive = uiState.isDemoLive,
                demoFps = uiState.demoFps,
                demoPingMs = uiState.demoPingMs,
                themeAccent = uiState.settings.themeAccent,
                isCompact = true,
                onToggleLive = viewModel::toggleDemoTelemetry
            )

            SimulationDisclaimerBanner()

            Crossfade(
                targetState = uiState.activeSection,
                animationSpec = tween(durationMillis = 220),
                label = "compact_section_crossfade"
            ) { section ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    when (section) {
                        DashboardSection.PERFORMANCE -> {
                            PerformanceHeroCard(uiState = uiState)
                            TelemetryGaugesGrid(uiState = uiState)
                            LiveSparklineCard(
                                fpsHistory = uiState.fpsHistory,
                                pingHistory = uiState.pingHistory
                            )
                            PerformanceModeSelectorCard(
                                selectedMode = uiState.settings.performanceMode,
                                onSelectMode = viewModel::selectPerformanceMode
                            )
                        }

                        DashboardSection.HUD_LAYOUT -> {
                            HudLayoutPreviewCard(uiState = uiState)
                            HudPresetSelectorCard(
                                settings = uiState.settings,
                                onSelectPreset = viewModel::selectHudPreset,
                                onSelectCorner = viewModel::selectFpsCorner
                            )
                            HudLayerControlsCard(
                                settings = uiState.settings,
                                onUpdateSettings = viewModel::updateSettings
                            )
                        }

                        DashboardSection.CROSSHAIRS -> {
                            CrosshairGalleryShowcaseCard(settings = uiState.settings)
                            CrosshairDesignGridCard(
                                settings = uiState.settings,
                                onSelectStyle = viewModel::selectCrosshairStyle
                            )
                            CrosshairStyleControlsCard(
                                settings = uiState.settings,
                                onUpdateSettings = viewModel::updateSettings,
                                onSelectColor = viewModel::selectCrosshairColor
                            )
                        }

                        DashboardSection.PROFILE_SETTINGS -> {
                            GamerProfileCard(
                                settings = uiState.settings,
                                onUpdateCallsign = viewModel::updateGamerCallsign,
                                onSelectGenre = viewModel::selectGamerGenre,
                                onSelectSlot = viewModel::selectHudSlot
                            )
                            DashboardSettingsCard(
                                settings = uiState.settings,
                                isDemoLive = uiState.isDemoLive,
                                onSelectThemeAccent = viewModel::selectThemeAccent,
                                onUpdateSettings = viewModel::updateSettings,
                                onToggleDemoLive = viewModel::toggleDemoTelemetry,
                                onResetDefaults = viewModel::resetAllDefaults
                            )
                            ConsoleLogCard(logs = uiState.logs)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun MediumRailLayout(
    uiState: PanelUiState,
    viewModel: PanelViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        NavigationRail(
            containerColor = PhantomCard.copy(alpha = 0.94f),
            contentColor = PhantomText
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            DashboardSection.entries.forEach { section ->
                val selected = section == uiState.activeSection
                NavigationRailItem(
                    selected = selected,
                    onClick = { viewModel.selectSection(section) },
                    modifier = Modifier.testTag("rail_tab_${section.name.lowercase()}"),
                    icon = {
                        Icon(
                            imageVector = sectionIcon(section, selected),
                            contentDescription = section.title
                        )
                    },
                    label = {
                        Text(
                            text = sectionShortLabel(section),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = PhantomCyan,
                        selectedTextColor = PhantomCyan,
                        indicatorColor = PhantomNeonBlue.copy(alpha = 0.22f),
                        unselectedIconColor = PhantomTextDim,
                        unselectedTextColor = PhantomTextDim
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PhantomPanelHeader(
                isDemoLive = uiState.isDemoLive,
                demoFps = uiState.demoFps,
                demoPingMs = uiState.demoPingMs,
                themeAccent = uiState.settings.themeAccent,
                isCompact = false,
                onToggleLive = viewModel::toggleDemoTelemetry
            )
            SimulationDisclaimerBanner()

            when (uiState.activeSection) {
                DashboardSection.PERFORMANCE -> {
                    PerformanceHeroCard(uiState = uiState)
                    TelemetryGaugesGrid(uiState = uiState)
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        LiveSparklineCard(
                            fpsHistory = uiState.fpsHistory,
                            pingHistory = uiState.pingHistory,
                            modifier = Modifier.weight(1f)
                        )
                        PerformanceModeSelectorCard(
                            selectedMode = uiState.settings.performanceMode,
                            onSelectMode = viewModel::selectPerformanceMode,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                DashboardSection.HUD_LAYOUT -> {
                    HudLayoutPreviewCard(uiState = uiState)
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        HudPresetSelectorCard(
                            settings = uiState.settings,
                            onSelectPreset = viewModel::selectHudPreset,
                            onSelectCorner = viewModel::selectFpsCorner,
                            modifier = Modifier.weight(1f)
                        )
                        HudLayerControlsCard(
                            settings = uiState.settings,
                            onUpdateSettings = viewModel::updateSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                DashboardSection.CROSSHAIRS -> {
                    CrosshairGalleryShowcaseCard(settings = uiState.settings)
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        CrosshairDesignGridCard(
                            settings = uiState.settings,
                            onSelectStyle = viewModel::selectCrosshairStyle,
                            modifier = Modifier.weight(1f)
                        )
                        CrosshairStyleControlsCard(
                            settings = uiState.settings,
                            onUpdateSettings = viewModel::updateSettings,
                            onSelectColor = viewModel::selectCrosshairColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                DashboardSection.PROFILE_SETTINGS -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        GamerProfileCard(
                            settings = uiState.settings,
                            onUpdateCallsign = viewModel::updateGamerCallsign,
                            onSelectGenre = viewModel::selectGamerGenre,
                            onSelectSlot = viewModel::selectHudSlot,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardSettingsCard(
                            settings = uiState.settings,
                            isDemoLive = uiState.isDemoLive,
                            onSelectThemeAccent = viewModel::selectThemeAccent,
                            onUpdateSettings = viewModel::updateSettings,
                            onToggleDemoLive = viewModel::toggleDemoTelemetry,
                            onResetDefaults = viewModel::resetAllDefaults,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    ConsoleLogCard(logs = uiState.logs)
                }
            }
        }
    }
}

@Composable
private fun ExpandedDashboardLayout(
    uiState: PanelUiState,
    viewModel: PanelViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PhantomPanelHeader(
            isDemoLive = uiState.isDemoLive,
            demoFps = uiState.demoFps,
            demoPingMs = uiState.demoPingMs,
            themeAccent = uiState.settings.themeAccent,
            isCompact = false,
            onToggleLive = viewModel::toggleDemoTelemetry
        )

        SimulationDisclaimerBanner()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Left Sidebar Column
            Column(
                modifier = Modifier
                    .width(240.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DashboardSection.entries.forEach { section ->
                    SidebarNavItem(
                        section = section,
                        selected = section == uiState.activeSection,
                        onClick = { viewModel.selectSection(section) }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                ConsoleLogCard(logs = uiState.logs)
            }

            // Center Main Workspace
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PerformanceHeroCard(uiState = uiState)
                TelemetryGaugesGrid(uiState = uiState)
                HudLayoutPreviewCard(uiState = uiState)
                CrosshairGalleryShowcaseCard(settings = uiState.settings)
                CrosshairDesignGridCard(
                    settings = uiState.settings,
                    onSelectStyle = viewModel::selectCrosshairStyle
                )
            }

            // Right Configuration & Profile Column
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                GamerProfileCard(
                    settings = uiState.settings,
                    onUpdateCallsign = viewModel::updateGamerCallsign,
                    onSelectGenre = viewModel::selectGamerGenre,
                    onSelectSlot = viewModel::selectHudSlot
                )
                PerformanceModeSelectorCard(
                    selectedMode = uiState.settings.performanceMode,
                    onSelectMode = viewModel::selectPerformanceMode
                )
                HudPresetSelectorCard(
                    settings = uiState.settings,
                    onSelectPreset = viewModel::selectHudPreset,
                    onSelectCorner = viewModel::selectFpsCorner
                )
                CrosshairStyleControlsCard(
                    settings = uiState.settings,
                    onUpdateSettings = viewModel::updateSettings,
                    onSelectColor = viewModel::selectCrosshairColor
                )
                DashboardSettingsCard(
                    settings = uiState.settings,
                    isDemoLive = uiState.isDemoLive,
                    onSelectThemeAccent = viewModel::selectThemeAccent,
                    onUpdateSettings = viewModel::updateSettings,
                    onToggleDemoLive = viewModel::toggleDemoTelemetry,
                    onResetDefaults = viewModel::resetAllDefaults
                )
            }
        }
    }
}

@Composable
private fun SidebarNavItem(
    section: DashboardSection,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("sidebar_nav_${section.name.lowercase()}"),
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = if (selected) BorderStroke(1.dp, PhantomCyan.copy(alpha = 0.38f)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (selected) {
                        Brush.linearGradient(
                            colors = listOf(
                                PhantomNeonBlue.copy(alpha = 0.22f),
                                PhantomCrimson.copy(alpha = 0.14f)
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    }
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = sectionIcon(section, selected),
                    contentDescription = section.title,
                    tint = if (selected) PhantomCyan else PhantomTextDim,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) PhantomText else PhantomTextDim
                )
            }

            if (section.badge != null) {
                Surface(
                    color = PhantomCrimson.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = section.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = PhantomCrimson,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

private fun sectionIcon(section: DashboardSection, selected: Boolean): ImageVector = when (section) {
    DashboardSection.PERFORMANCE -> if (selected) Icons.Filled.Speed else Icons.Outlined.Speed
    DashboardSection.HUD_LAYOUT -> if (selected) Icons.Filled.GridView else Icons.Outlined.GridView
    DashboardSection.CROSSHAIRS -> if (selected) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome
    DashboardSection.PROFILE_SETTINGS -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
}

private fun sectionShortLabel(section: DashboardSection): String = when (section) {
    DashboardSection.PERFORMANCE -> "Dashboard"
    DashboardSection.HUD_LAYOUT -> "HUD"
    DashboardSection.CROSSHAIRS -> "Reticles"
    DashboardSection.PROFILE_SETTINGS -> "Profile"
}
