package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PanelPreferencesRepository
import com.example.model.CrosshairColorOption
import com.example.model.CrosshairStyle
import com.example.model.DashboardSection
import com.example.model.GamerGenre
import com.example.model.HudCornerPosition
import com.example.model.HudLayoutPreset
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.model.PanelSettings
import com.example.model.PanelUiState
import com.example.model.PerformanceProfileMode
import com.example.model.ThemeAccentMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class PanelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PanelPreferencesRepository(application.applicationContext)
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    private val _uiState = MutableStateFlow(PanelUiState())
    val uiState: StateFlow<PanelUiState> = _uiState.asStateFlow()

    private var logSequence = 10L
    private var tickCount = 0

    init {
        viewModelScope.launch {
            val saved = repository.settingsFlow.first()
            _uiState.update { it.copy(settings = saved) }
        }
        startDemoTelemetryLoop()
    }

    private fun startDemoTelemetryLoop() {
        viewModelScope.launch {
            while (true) {
                delay(750L)
                val current = _uiState.value
                if (!current.isDemoLive) continue

                tickCount++
                val mode = current.settings.performanceMode
                val wave = sin(tickCount * 0.55)
                val wave2 = cos(tickCount * 0.35)

                val nextFps = (mode.targetFps + (wave * 2.5)).roundToInt().coerceAtLeast(30)
                val nextPing = (mode.basePing + (wave2 * 2.5)).roundToInt().coerceAtLeast(5)
                val nextFrameTime = (1000f / nextFps.toFloat())
                val nextGpu = (mode.baseGpuLoad + (wave * 5.0)).roundToInt().coerceIn(20, 96)
                val nextCpu = ((mode.baseGpuLoad * 0.6f) + (wave2 * 4.0)).roundToInt().coerceIn(15, 88)
                val nextTemp = (mode.baseTempC + (wave * 2.0)).roundToInt().coerceIn(38, 79)

                val nextFpsHistory = (current.fpsHistory + nextFps.toFloat()).takeLast(20)
                val nextPingHistory = (current.pingHistory + nextPing.toFloat()).takeLast(20)

                _uiState.update {
                    it.copy(
                        demoFps = nextFps,
                        demoPingMs = nextPing,
                        demoFrameTimeMs = nextFrameTime,
                        demoGpuLoad = nextGpu,
                        demoCpuLoad = nextCpu,
                        demoTempC = nextTemp,
                        sessionSeconds = it.sessionSeconds + 1,
                        fpsHistory = nextFpsHistory,
                        pingHistory = nextPingHistory
                    )
                }
            }
        }
    }

    fun selectSection(section: DashboardSection) {
        _uiState.update { it.copy(activeSection = section) }
    }

    fun toggleDemoTelemetry() {
        val nextLive = !_uiState.value.isDemoLive
        _uiState.update { it.copy(isDemoLive = nextLive) }
        appendLog(
            if (nextLive) "Demo telemetry stream RESUMED" else "Demo telemetry stream PAUSED",
            if (nextLive) LogLevel.SUCCESS else LogLevel.WARN
        )
    }

    fun updateSettings(transform: (PanelSettings) -> PanelSettings) {
        val updated = transform(_uiState.value.settings)
        _uiState.update { it.copy(settings = updated) }
        viewModelScope.launch {
            repository.saveSettings(updated)
        }
    }

    fun selectHudPreset(preset: HudLayoutPreset) {
        updateSettings { current ->
            when (preset) {
                HudLayoutPreset.TACTICAL_COMPACT -> current.copy(
                    hudPreset = preset,
                    showFpsOverlay = true,
                    showPingOverlay = true,
                    showMiniGraphOverlay = false,
                    showCornerBrackets = true,
                    showScanlines = false
                )
                HudLayoutPreset.STREAMER_PRO -> current.copy(
                    hudPreset = preset,
                    showFpsOverlay = true,
                    showPingOverlay = true,
                    showMiniGraphOverlay = true,
                    showCornerBrackets = true,
                    showScanlines = true
                )
                HudLayoutPreset.FULL_TELEMETRY -> current.copy(
                    hudPreset = preset,
                    showFpsOverlay = true,
                    showPingOverlay = true,
                    showMiniGraphOverlay = true,
                    showCornerBrackets = true,
                    showScanlines = true,
                    showBackdropArt = true
                )
                HudLayoutPreset.MINIMALIST -> current.copy(
                    hudPreset = preset,
                    showFpsOverlay = true,
                    showPingOverlay = false,
                    showMiniGraphOverlay = false,
                    showCornerBrackets = false,
                    showScanlines = false
                )
            }
        }
        appendLog("HUD Layout preset applied: ${preset.label}", LogLevel.INFO)
    }

    fun selectFpsCorner(corner: HudCornerPosition) {
        updateSettings { it.copy(fpsCorner = corner) }
        appendLog("Telemetry HUD badge anchored to ${corner.label}", LogLevel.INFO)
    }

    fun selectPerformanceMode(mode: PerformanceProfileMode) {
        updateSettings { it.copy(performanceMode = mode) }
        appendLog("Demo dashboard profile set to ${mode.label} (${mode.targetFps}Hz)", LogLevel.SUCCESS)
    }

    fun selectCrosshairStyle(style: CrosshairStyle) {
        updateSettings { it.copy(crosshairStyle = style) }
        appendLog("Reticle gallery preview: ${style.label}", LogLevel.INFO)
    }

    fun selectCrosshairColor(color: CrosshairColorOption) {
        updateSettings { it.copy(crosshairColor = color) }
        appendLog("Reticle neon pigment: ${color.label}", LogLevel.INFO)
    }

    fun selectThemeAccent(accent: ThemeAccentMode) {
        updateSettings { it.copy(themeAccent = accent) }
        appendLog("Cyberpunk UI accent switched to ${accent.label}", LogLevel.INFO)
    }

    fun updateGamerCallsign(callsign: String) {
        val sanitized = callsign.trim().take(18).ifEmpty { "PHANTOM_01" }
        updateSettings { it.copy(gamerCallsign = sanitized) }
        appendLog("Profile callsign updated to $sanitized", LogLevel.SUCCESS)
    }

    fun selectGamerGenre(genre: GamerGenre) {
        updateSettings { it.copy(gamerGenre = genre) }
        appendLog("Profile focus set to ${genre.label}", LogLevel.INFO)
    }

    fun selectHudSlot(slot: String) {
        updateSettings { it.copy(activeSlot = slot) }
        appendLog("Loaded visual HUD configuration $slot", LogLevel.INFO)
    }

    fun resetAllDefaults() {
        val defaults = PanelSettings()
        _uiState.update {
            it.copy(
                settings = defaults,
                isDemoLive = true,
                demoFps = defaults.performanceMode.targetFps,
                demoPingMs = defaults.performanceMode.basePing
            )
        }
        viewModelScope.launch {
            repository.saveSettings(defaults)
        }
        appendLog("Dashboard preferences restored to cyber defaults.", LogLevel.WARN)
    }

    private fun appendLog(message: String, level: LogLevel) {
        val entry = LogEntry(
            id = logSequence++,
            timestamp = timeFormat.format(Date()),
            message = message,
            level = level
        )
        _uiState.update { state ->
            state.copy(logs = (listOf(entry) + state.logs).take(25))
        }
    }
}
