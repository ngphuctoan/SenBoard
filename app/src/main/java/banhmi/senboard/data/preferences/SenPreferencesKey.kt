package banhmi.senboard.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

/*====================
Input Method
====================*/
// Vietnamese engine is an enum that holds the key's values as an integer
val vietnameseEngineType = intPreferencesKey("vietnamese_engine_type")
val autoCapitalizationEnabled = booleanPreferencesKey("auto_capitalization_enabled")
val spaceBarShortcutEnabled = booleanPreferencesKey("space_bar_shortcut_enabled")
val wordSuggestionsEnabled = booleanPreferencesKey("word_suggestions_enabled")

/*====================
Appearance
====================*/
val numberRowEnabled = booleanPreferencesKey("number_row_enabled")
val keyBackgroundEnabled = booleanPreferencesKey("key_background_enabled")
val keyBackgroundShadowEnabled = booleanPreferencesKey("key_background_shadow_enabled")

/*====================
Haptics
====================*/
val hapticsEnabled = booleanPreferencesKey("haptics_enabled")
val hapticsIntensity = intPreferencesKey("haptics_intensity")

/*====================
Easter Eggs
====================*/
val easterEggsEnabled = booleanPreferencesKey("easter_eggs_enabled")
val aaaaaModeEnabled = booleanPreferencesKey("aaaaa_mode_enabled")

/*====================
Developer Options
====================*/
val developerOptionsEnabled = booleanPreferencesKey("developer_options_enabled")
