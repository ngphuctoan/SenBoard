package banhmi.senboard.ime.keyboard.core

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import banhmi.senboard.app.settings.SenBoardPreferences
import banhmi.senboard.ime.keyboard.models.KeyHandler
import kotlin.math.roundToInt

class SenBoardController(val context: SenBoardContext) {
    val state: SenBoardState
        get() = context.state

    fun handle(handler: KeyHandler) {
        playFeedback()
        handler.handle(context)
    }

    fun handleDoubleTap(handler: KeyHandler) {
        playFeedback()
        handler.handleDoubleTap(context)
    }

    private fun playFeedback() {
        val service = context.service ?: return
        val prefs = SenBoardPreferences(service)

        // 1. Play Click Sound
        if (prefs.soundEnabled) {
            val audioManager = service.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            val volume = prefs.soundVolume / 100f
            audioManager?.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, volume)
        }

        // 2. Play Haptic vibration
        if (prefs.hapticEnabled) {
            try {
                val vibrator = service.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (vibrator?.hasVibrator() == true) {
                    val intensity = prefs.hapticIntensity
                    val duration = (intensity / 2).toLong().coerceIn(10, 50)
                    val amplitude = (intensity * 2.55).roundToInt().coerceIn(1, 255)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(duration)
                    }
                }
            } catch (e: Exception) {
                // Prevent crashes from SecurityException or hardware issues
            }
        }
    }
}
