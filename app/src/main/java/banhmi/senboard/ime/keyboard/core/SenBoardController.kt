package banhmi.senboard.ime.keyboard.core

import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.shared.settings.InputMethodSettings

class SenBoardController(val context: SenBoardContext) {
    val state: SenBoardState
        get() = context.state

    fun handle(handler: KeyHandler) {
        performHaptic()
        performSoundEffect()
        handler.handle(context)
        if (handler !is ShiftKeyHandler) updateShiftModeByContext()
    }

    fun handleDoubleTap(handler: KeyHandler) {
        performHaptic()
        performSoundEffect()
        handler.handleDoubleTap(context)
        if (handler !is ShiftKeyHandler) updateShiftModeByContext()
    }

    fun updateShiftModeByContext() {
        if (state.shiftMode == ShiftMode.CapsLocked) return

        val shouldBeShifted = context.inputMethodState.autoCapitalizationEnabled && shouldAutoCapitalize()
        val shiftMode = if (shouldBeShifted) ShiftMode.Automatic else ShiftMode.Off

        context.state = state.copy(shiftMode = shiftMode)
    }

    private fun shouldAutoCapitalize(): Boolean =
        (context.getEditor()?.getCursorCapsMode(EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES) ?: 0) != 0

    private fun performHaptic() {
        val haptic = context.getHaptic() ?: return

        val intensity = context.soundsAndHapticsState.hapticIntensity
        val duration = (intensity / 2).toLong().coerceIn(10, 50)
        val amplitude = (intensity * 2.55).toInt().coerceIn(1, 255)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haptic.vibrate(VibrationEffect.createOneShot(duration, amplitude))
        } else {
            @Suppress("DEPRECATION") haptic.vibrate(duration)
        }
    }

    private fun performSoundEffect() {
        val audioManager = context.getAudioManager() ?: return

        val volume = context.soundsAndHapticsState.soundVolume / 100f
        if (volume <= 0f) return

        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, volume)
    }
}
