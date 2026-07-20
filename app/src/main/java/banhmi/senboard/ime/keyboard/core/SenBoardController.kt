package banhmi.senboard.ime.keyboard.core

import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import banhmi.senboard.ime.keyboard.models.KeyHandler

class SenBoardController(private val context: SenBoardContext) {
    val state: SenBoardState
        get() = context.state

    fun handle(handler: KeyHandler) {
        performHaptic()
        performSoundEffect()
        handler.handle(context)
    }

    fun handleDoubleTap(handler: KeyHandler) {
        performHaptic()
        performSoundEffect()
        handler.handleDoubleTap(context)
    }

    private fun performHaptic() {
        val preferences = context.getPreferences() ?: return
        val haptic = context.getHaptic() ?: return

        if (!preferences.hapticEnabled) return

        val intensity = preferences.hapticIntensity
        val duration = (intensity / 2).toLong().coerceIn(10, 50)
        val amplitude = (intensity * 2.55).toInt().coerceIn(1, 255)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haptic.vibrate(VibrationEffect.createOneShot(duration, amplitude))
        } else {
            @Suppress("DEPRECATION") haptic.vibrate(duration)
        }
    }

    private fun performSoundEffect() {
        val preferences = context.getPreferences() ?: return
        val audioManager = context.getAudioManager() ?: return

        val volume = preferences.soundVolume / 100f
        if (volume <= 0f) return

        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, volume)
    }
}
