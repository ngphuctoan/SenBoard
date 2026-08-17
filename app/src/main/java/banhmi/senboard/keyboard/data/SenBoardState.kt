package banhmi.senboard.keyboard.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import banhmi.senboard.keyboard.impl.mode.SenCharactersMode
import banhmi.senboard.keyboard.model.SenMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ShiftMode {
    Off, Shifted, CapsLocked;
}

data class SenBoardState(
    val mode: SenMode,
    val shiftMode: ShiftMode,
    val composingText: String,
) {
    val isShifted: Boolean
        get() = shiftMode == ShiftMode.Shifted || shiftMode == ShiftMode.CapsLocked
}

object SenBoardStateDefaults {
    // These are the initial values of the state
    val Mode: SenMode = SenCharactersMode

    val FallbackShiftMode: ShiftMode = ShiftMode.Off

    fun shiftMode(autoCapitalizationEnabled: Boolean): ShiftMode =
        if (autoCapitalizationEnabled) ShiftMode.Shifted else FallbackShiftMode

    @JvmStatic
    val EmptyComposingText: String = ""
}

class SenBoardStateViewModel(mode: SenMode, shiftMode: ShiftMode) : ViewModel() {
    // Factory to pass in the initial state values
    companion object {
        val MODE_KEY = CreationExtras.Key<SenMode>()

        val SHIFT_MODE_KEY = CreationExtras.Key<ShiftMode>()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val mode = this[MODE_KEY] ?: SenBoardStateDefaults.Mode
                val shiftMode = this[SHIFT_MODE_KEY] ?: SenBoardStateDefaults.FallbackShiftMode
                SenBoardStateViewModel(mode, shiftMode)
            }
        }
    }

    private val _uiState: MutableStateFlow<SenBoardState> = MutableStateFlow(
        SenBoardState(
            mode = mode,
            shiftMode = shiftMode,
            composingText = SenBoardStateDefaults.EmptyComposingText,
        ),
    )

    val uiState: StateFlow<SenBoardState> = _uiState.asStateFlow()

    fun setState(newState: SenBoardState) = _uiState.update { newState }
}
