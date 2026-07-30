package banhmi.senboard.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

/*====================
Input Method
====================*/
// Vietnamese engine is an enum that holds the key's values as an integer
val vietnameseEngine = intPreferencesKey("vietnamese_engine")
val autoCapitalizationEnabled = booleanPreferencesKey("auto_capitalization_enabled")
val spaceBarShortcutEnabled = booleanPreferencesKey("space_bar_shortcut_enabled")
val easterEggEnabled = booleanPreferencesKey("easter_egg_enabled")

/*====================
Appearance
====================*/
val showKeyBackground = booleanPreferencesKey("show_key_background")

/*====================
Sounds and Haptics
====================*/
val hapticIntensity = intPreferencesKey("haptic_intensity")
val soundVolume = intPreferencesKey("sound_volume")
