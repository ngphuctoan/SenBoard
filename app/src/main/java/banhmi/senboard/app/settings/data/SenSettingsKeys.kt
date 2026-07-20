package banhmi.senboard.app.settings.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SenSettingsKeys {
    // Sounds & Haptics
    val hapticEnabled = booleanPreferencesKey("haptic_enabled")
    val hapticIntensity = intPreferencesKey("haptic_intensity")
    val soundVolume = intPreferencesKey("sound_volume")

    // About
    val easterEggEnabled = booleanPreferencesKey("easter_egg_enabled")
}
