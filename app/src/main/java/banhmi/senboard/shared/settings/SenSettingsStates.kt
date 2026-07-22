package banhmi.senboard.shared.settings

data class InputMethodSettings(
    val autoCapitalizationEnabled: Boolean = true,
    val spaceBarShortcutEnabled: Boolean = true,
    val easterEggEnabled: Boolean = false,
)

data class AppearanceSettings(
    val oledThemeEnabled: Boolean = false,
    val fullWidthKeyboard: Boolean = false,
    val showKeyBackground: Boolean = false,
)

data class SoundsAndHapticsSettings(
    val hapticIntensity: Int = 66,
    val soundVolume: Int = 50,
)
