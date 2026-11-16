package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.PhantomCrimson
import com.example.ui.theme.PhantomCyan
import com.example.ui.theme.PhantomNeonBlue
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomSuccess

enum class DashboardSection(val title: String, val badge: String? = null) {
    PERFORMANCE("Dashboard", "LIVE"),
    HUD_LAYOUT("HUD Layout", "GRID"),
    CROSSHAIRS("Reticles", "FX"),
    PROFILE_SETTINGS("Profile & Settings", null)
}

enum class HudLayoutPreset(val label: String, val description: String) {
    TACTICAL_COMPACT("Tactical Edge", "Clean corner telemetry with minimal center obstruction"),
    STREAMER_PRO("Streamer Pro", "Balanced FPS, ping, and live performance sparkline"),
    FULL_TELEMETRY("Full Telemetry", "Complete diagnostic brackets, scanlines, and graphs"),
    MINIMALIST("Minimalist", "Essential FPS counter only for pure screen clarity")
}

enum class HudCornerPosition(val label: String, val shortTag: String) {
    TOP_LEFT("Top Left", "TL"),
    TOP_RIGHT("Top Right", "TR"),
    BOTTOM_LEFT("Bottom Left", "BL"),
    BOTTOM_RIGHT("Bottom Right", "BR")
}

enum class PerformanceProfileMode(
    val label: String,
    val targetFps: Int,
    val basePing: Int,
    val baseGpuLoad: Int,
    val baseTempC: Int
) {
    BALANCED("Balanced", 120, 14, 62, 58),
    HYPER_FRAME("Hyper Frame", 144, 12, 84, 68),
    LOW_LATENCY("Low Latency", 120, 9, 71, 61),
    ECO_COOL("Eco Thermal", 60, 18, 38, 46)
}

enum class CrosshairStyle(val label: String, val subtitle: String) {
    CYBER_PULSE("Cyber Pulse", "Breathing neon segmented cross"),
    ORBITAL_RING("Orbital Ring", "Counter-rotating holographic arcs"),
    QUANTUM_DOT("Quantum Dot", "Pulsing halo around precision core"),
    NEON_X_WING("Neon X-Wing", "Angled cyberpunk diagonal fins"),
    PRISM_CORE("Prism Core", "Rotating diamond vector matrix"),
    HOLO_BRACKET("Holo Bracket", "Dynamic corner scanner brackets")
}

enum class CrosshairColorOption(val label: String, val color: Color) {
    NEON_BLUE("Neon Cyan", PhantomCyan),
    CRIMSON_RED("Cyber Red", PhantomCrimson),
    ELECTRIC_COBALT("Cobalt Blue", PhantomNeonBlue),
    PLASMA_VIOLET("Plasma Violet", PhantomPurple),
    MATRIX_GREEN("Matrix Green", PhantomSuccess),
    PURE_WHITE("Pure White", Color(0xFFFFFFFF))
}

enum class ThemeAccentMode(val label: String) {
    DUAL_CYBER("Blue + Red Dual"),
    NEON_BLUE_FOCUS("Neon Blue Focus"),
    CRIMSON_RED_FOCUS("Crimson Red Focus")
}

enum class GamerGenre(val label: String) {
    CYBER_ARENA("Cyber Arena"),
    SCI_FI_RACER("Sci-Fi Racer"),
    OPEN_WORLD("Open World"),
    ESPORTS_SIM("Esports Sim")
}

enum class LogLevel {
    INFO,
    SUCCESS,
    WARN
}

data class LogEntry(
    val id: Long,
    val timestamp: String,
    val message: String,
    val level: LogLevel
)

data class PanelSettings(
    val hudPreset: HudLayoutPreset = HudLayoutPreset.STREAMER_PRO,
    val fpsCorner: HudCornerPosition = HudCornerPosition.TOP_RIGHT,
    val hudOpacity: Float = 0.90f,
    val hudScale: Float = 1.0f,
    val showFpsOverlay: Boolean = true,
    val showPingOverlay: Boolean = true,
    val showMiniGraphOverlay: Boolean = true,
    val showCornerBrackets: Boolean = true,
    val showScanlines: Boolean = true,
    val showBackdropArt: Boolean = true,
    val crosshairStyle: CrosshairStyle = CrosshairStyle.CYBER_PULSE,
    val crosshairColor: CrosshairColorOption = CrosshairColorOption.NEON_BLUE,
    val crosshairSize: Float = 24f,
    val crosshairGap: Float = 7f,
    val crosshairThickness: Float = 2.5f,
    val crosshairGlow: Boolean = true,
    val animateCrosshair: Boolean = true,
    val performanceMode: PerformanceProfileMode = PerformanceProfileMode.HYPER_FRAME,
    val themeAccent: ThemeAccentMode = ThemeAccentMode.DUAL_CYBER,
    val animationSpeed: Float = 1.0f,
    val gamerCallsign: String = "PHANTOM_01",
    val gamerGenre: GamerGenre = GamerGenre.CYBER_ARENA,
    val activeSlot: String = "SLOT A"
)

data class PanelUiState(
    val isDemoLive: Boolean = true,
    val activeSection: DashboardSection = DashboardSection.PERFORMANCE,
    val settings: PanelSettings = PanelSettings(),
    val demoFps: Int = 144,
    val demoPingMs: Int = 12,
    val demoFrameTimeMs: Float = 6.9f,
    val demoCpuLoad: Int = 48,
    val demoGpuLoad: Int = 82,
    val demoTempC: Int = 66,
    val sessionSeconds: Int = 0,
    val fpsHistory: List<Float> = listOf(138f, 141f, 144f, 142f, 143f, 144f, 140f, 142f, 144f, 143f, 144f, 141f, 143f, 144f, 142f, 144f),
    val pingHistory: List<Float> = listOf(13f, 12f, 14f, 12f, 11f, 12f, 13f, 12f, 11f, 12f, 13f, 12f, 12f, 11f, 12f, 12f),
    val logs: List<LogEntry> = defaultInitialLogs()
)

fun defaultInitialLogs(): List<LogEntry> = listOf(
    LogEntry(1L, "09:24:01", "PhantomX Cyber Dashboard v3.0 ready.", LogLevel.INFO),
    LogEntry(2L, "09:24:02", "Demo telemetry stream active (simulated FPS & ping).", LogLevel.SUCCESS),
    LogEntry(3L, "09:24:03", "Animated Crosshair Design Gallery loaded (6 styles).", LogLevel.INFO),
    LogEntry(4L, "09:24:04", "Visual UI Demo Mode — safe interface preview.", LogLevel.WARN)
)
