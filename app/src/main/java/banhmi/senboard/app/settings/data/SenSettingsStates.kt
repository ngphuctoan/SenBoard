package banhmi.senboard.app.settings.data

data class InputMethodSettings(
    val easterEggEnabled: Boolean = false,
)

data class SoundsAndHapticsSettings(
    val hapticIntensity: Int = 60,
    val soundVolume: Int = 50,
)
