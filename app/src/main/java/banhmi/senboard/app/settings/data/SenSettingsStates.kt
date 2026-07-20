package banhmi.senboard.app.settings.data

data class SoundsAndHapticsSettings(
    val hapticEnabled: Boolean = true,
    val hapticIntensity: Int = 60,
    val soundVolume: Int = 50,
)

data class AboutSettings(
    val easterEggEnabled: Boolean = false,
)
