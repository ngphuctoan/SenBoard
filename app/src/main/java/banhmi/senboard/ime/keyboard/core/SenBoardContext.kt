package banhmi.senboard.ime.keyboard.core

import banhmi.senboard.shared.settings.AppearanceSettings
import banhmi.senboard.shared.settings.InputMethodSettings
import banhmi.senboard.shared.settings.SoundsAndHapticsSettings
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.media.AudioManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.view.inputmethod.InputConnection
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import banhmi.senboard.shared.settings.SenSettingsKeys
import kotlinx.coroutines.flow.StateFlow

@Stable
class SenBoardContext(
    private val im: InputMethodService?,
    val inputMethodStateFlow: StateFlow<InputMethodSettings>,
    val appearanceStateFlow: StateFlow<AppearanceSettings>,
    val soundsAndHapticsStateFlow: StateFlow<SoundsAndHapticsSettings>,
    initialState: SenBoardState = SenBoardState(),
) {
    var state by mutableStateOf(initialState)
        internal set

    val inputMethodState: InputMethodSettings
        get() = inputMethodStateFlow.value

    val appearanceState: AppearanceSettings
        get() = appearanceStateFlow.value

    val soundsAndHapticsState: SoundsAndHapticsSettings
        get() = soundsAndHapticsStateFlow.value

    fun getEditor(): InputConnection? = im?.currentInputConnection

    fun getHaptic(): Vibrator? = im?.let {
        // Looks so bad because VIBRATOR_MANAGER_SERVICE is only available on Android 12
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = it.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
            (manager as VibratorManager).defaultVibrator
        } else {
            val service = it.getSystemService(
                @Suppress("DEPRECATION") Context.VIBRATOR_SERVICE
            )
            service as Vibrator
        }
    }

    fun getAudioManager(): AudioManager? = im?.let {
        it.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}
