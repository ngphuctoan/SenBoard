package banhmi.senboard.shared.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SenSettingsKeys {
    // Input Method
    val autoCapitalizationEnabled = booleanPreferencesKey("auto_capitalization_enabled")
    val spaceBarShortcutEnabled = booleanPreferencesKey("space_bar_shortcut_enabled")
    val easterEggEnabled = booleanPreferencesKey("easter_egg_enabled")

    // Appearance
    val fullWidthKeyboard = booleanPreferencesKey("full_width_keyboard")
    val showKeyBackground = booleanPreferencesKey("show_key_background")

    // Sounds & Haptics
    val hapticIntensity = intPreferencesKey("haptic_intensity")
    val soundVolume = intPreferencesKey("sound_volume")
}
