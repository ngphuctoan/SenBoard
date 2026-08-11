package banhmi.senboard.data.preferences

import banhmi.senboard.ime.engine.VietnameseEngine

data class SenPreferences(
    /*====================
    Input Method
    ====================*/
    // Can only accept an integer, so access the enum value directly
    val vietnameseEngine: Int = VietnameseEngine.Cvnss40.value,
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
    val hapticsIntensity: Int = 50,
)
