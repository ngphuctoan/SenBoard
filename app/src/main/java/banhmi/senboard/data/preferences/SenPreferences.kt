package banhmi.senboard.data.preferences

import banhmi.senboard.engine.VietnameseEngineType

data class SenPreferences(
    /*====================
    Input Method
    ====================*/
    // The repository will handle the ID to type conversion
    val vietnameseEngineType: VietnameseEngineType = VietnameseEngineType.Cvss,
    val autoCapitalizationEnabled: Boolean = true,
    val spaceBarShortcutEnabled: Boolean = true,
    val wordSuggestionsEnabled: Boolean = true,

    /*====================
    Appearance
    ====================*/
    val keyBackgroundEnabled: Boolean = true,
    val keyBackgroundShadowEnabled: Boolean = true,
    val numberRowEnabled: Boolean = true,

    /*====================
    Haptics
    ====================*/
    val hapticsEnabled: Boolean = true,
    val hapticsIntensity: Int = 66,

    /*====================
    Easter Eggs
    ====================*/
    val easterEggsEnabled: Boolean = false,
    val aaaaaModeEnabled: Boolean = false,

    /*====================
    Developer Options
    ====================*/
    val developerOptionsEnabled: Boolean = false,
)
