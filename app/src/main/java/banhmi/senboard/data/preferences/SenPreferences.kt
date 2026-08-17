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
    val easterEggEnabled: Boolean = false,

    /*====================
    Appearance
    ====================*/
    val showKeyBackground: Boolean = false,

    /*====================
    Haptics
    ====================*/
    val hapticsEnabled: Boolean = true,
    val hapticsIntensity: Int = 66,
)
