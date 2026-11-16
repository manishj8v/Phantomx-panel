package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.CrosshairColorOption
import com.example.model.CrosshairStyle
import com.example.model.GamerGenre
import com.example.model.HudCornerPosition
import com.example.model.HudLayoutPreset
import com.example.model.PanelSettings
import com.example.model.PerformanceProfileMode
import com.example.model.ThemeAccentMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.panelDataStore: DataStore<Preferences> by preferencesDataStore(name = "phantomx_cyber_prefs")

class PanelPreferencesRepository(private val context: Context) {

    private object Keys {
        val HUD_PRESET = stringPreferencesKey("hud_preset")
        val FPS_CORNER = stringPreferencesKey("fps_corner")
        val HUD_OPACITY = floatPreferencesKey("hud_opacity")
        val HUD_SCALE = floatPreferencesKey("hud_scale")
        val SHOW_FPS_OVERLAY = booleanPreferencesKey("show_fps_overlay")
        val SHOW_PING_OVERLAY = booleanPreferencesKey("show_ping_overlay")
        val SHOW_MINI_GRAPH = booleanPreferencesKey("show_mini_graph")
        val SHOW_CORNER_BRACKETS = booleanPreferencesKey("show_corner_brackets")
        val SHOW_SCANLINES = booleanPreferencesKey("show_scanlines")
        val SHOW_BACKDROP_ART = booleanPreferencesKey("show_backdrop_art")
        val CROSSHAIR_STYLE = stringPreferencesKey("crosshair_style")
        val CROSSHAIR_COLOR = stringPreferencesKey("crosshair_color")
        val CROSSHAIR_SIZE = floatPreferencesKey("crosshair_size")
        val CROSSHAIR_GAP = floatPreferencesKey("crosshair_gap")
        val CROSSHAIR_THICKNESS = floatPreferencesKey("crosshair_thickness")
        val CROSSHAIR_GLOW = booleanPreferencesKey("crosshair_glow")
        val ANIMATE_CROSSHAIR = booleanPreferencesKey("animate_crosshair")
        val PERFORMANCE_MODE = stringPreferencesKey("performance_mode")
        val THEME_ACCENT = stringPreferencesKey("theme_accent")
        val ANIMATION_SPEED = floatPreferencesKey("animation_speed")
        val GAMER_CALLSIGN = stringPreferencesKey("gamer_callsign")
        val GAMER_GENRE = stringPreferencesKey("gamer_genre")
        val ACTIVE_SLOT = stringPreferencesKey("active_slot")
    }

    val settingsFlow: Flow<PanelSettings> = context.panelDataStore.data.map { prefs ->
        PanelSettings(
            hudPreset = prefs[Keys.HUD_PRESET]?.let { name ->
                HudLayoutPreset.entries.firstOrNull { it.name == name }
            } ?: HudLayoutPreset.STREAMER_PRO,
            fpsCorner = prefs[Keys.FPS_CORNER]?.let { name ->
                HudCornerPosition.entries.firstOrNull { it.name == name }
            } ?: HudCornerPosition.TOP_RIGHT,
            hudOpacity = prefs[Keys.HUD_OPACITY] ?: 0.90f,
            hudScale = prefs[Keys.HUD_SCALE] ?: 1.0f,
            showFpsOverlay = prefs[Keys.SHOW_FPS_OVERLAY] ?: true,
            showPingOverlay = prefs[Keys.SHOW_PING_OVERLAY] ?: true,
            showMiniGraphOverlay = prefs[Keys.SHOW_MINI_GRAPH] ?: true,
            showCornerBrackets = prefs[Keys.SHOW_CORNER_BRACKETS] ?: true,
            showScanlines = prefs[Keys.SHOW_SCANLINES] ?: true,
            showBackdropArt = prefs[Keys.SHOW_BACKDROP_ART] ?: true,
            crosshairStyle = prefs[Keys.CROSSHAIR_STYLE]?.let { name ->
                CrosshairStyle.entries.firstOrNull { it.name == name }
            } ?: CrosshairStyle.CYBER_PULSE,
            crosshairColor = prefs[Keys.CROSSHAIR_COLOR]?.let { name ->
                CrosshairColorOption.entries.firstOrNull { it.name == name }
            } ?: CrosshairColorOption.NEON_BLUE,
            crosshairSize = prefs[Keys.CROSSHAIR_SIZE] ?: 24f,
            crosshairGap = prefs[Keys.CROSSHAIR_GAP] ?: 7f,
            crosshairThickness = prefs[Keys.CROSSHAIR_THICKNESS] ?: 2.5f,
            crosshairGlow = prefs[Keys.CROSSHAIR_GLOW] ?: true,
            animateCrosshair = prefs[Keys.ANIMATE_CROSSHAIR] ?: true,
            performanceMode = prefs[Keys.PERFORMANCE_MODE]?.let { name ->
                PerformanceProfileMode.entries.firstOrNull { it.name == name }
            } ?: PerformanceProfileMode.HYPER_FRAME,
            themeAccent = prefs[Keys.THEME_ACCENT]?.let { name ->
                ThemeAccentMode.entries.firstOrNull { it.name == name }
            } ?: ThemeAccentMode.DUAL_CYBER,
            animationSpeed = prefs[Keys.ANIMATION_SPEED] ?: 1.0f,
            gamerCallsign = prefs[Keys.GAMER_CALLSIGN] ?: "PHANTOM_01",
            gamerGenre = prefs[Keys.GAMER_GENRE]?.let { name ->
                GamerGenre.entries.firstOrNull { it.name == name }
            } ?: GamerGenre.CYBER_ARENA,
            activeSlot = prefs[Keys.ACTIVE_SLOT] ?: "SLOT A"
        )
    }

    suspend fun saveSettings(settings: PanelSettings) {
        context.panelDataStore.edit { prefs ->
            prefs[Keys.HUD_PRESET] = settings.hudPreset.name
            prefs[Keys.FPS_CORNER] = settings.fpsCorner.name
            prefs[Keys.HUD_OPACITY] = settings.hudOpacity
            prefs[Keys.HUD_SCALE] = settings.hudScale
            prefs[Keys.SHOW_FPS_OVERLAY] = settings.showFpsOverlay
            prefs[Keys.SHOW_PING_OVERLAY] = settings.showPingOverlay
            prefs[Keys.SHOW_MINI_GRAPH] = settings.showMiniGraphOverlay
            prefs[Keys.SHOW_CORNER_BRACKETS] = settings.showCornerBrackets
            prefs[Keys.SHOW_SCANLINES] = settings.showScanlines
            prefs[Keys.SHOW_BACKDROP_ART] = settings.showBackdropArt
            prefs[Keys.CROSSHAIR_STYLE] = settings.crosshairStyle.name
            prefs[Keys.CROSSHAIR_COLOR] = settings.crosshairColor.name
            prefs[Keys.CROSSHAIR_SIZE] = settings.crosshairSize
            prefs[Keys.CROSSHAIR_GAP] = settings.crosshairGap
            prefs[Keys.CROSSHAIR_THICKNESS] = settings.crosshairThickness
            prefs[Keys.CROSSHAIR_GLOW] = settings.crosshairGlow
            prefs[Keys.ANIMATE_CROSSHAIR] = settings.animateCrosshair
            prefs[Keys.PERFORMANCE_MODE] = settings.performanceMode.name
            prefs[Keys.THEME_ACCENT] = settings.themeAccent.name
            prefs[Keys.ANIMATION_SPEED] = settings.animationSpeed
            prefs[Keys.GAMER_CALLSIGN] = settings.gamerCallsign
            prefs[Keys.GAMER_GENRE] = settings.gamerGenre.name
            prefs[Keys.ACTIVE_SLOT] = settings.activeSlot
        }
    }
}
