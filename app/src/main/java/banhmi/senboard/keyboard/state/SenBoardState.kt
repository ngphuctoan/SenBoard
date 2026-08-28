package banhmi.senboard.keyboard.state

import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.provideMode
import banhmi.senboard.utils.EMPTY

enum class ShiftMode {
    Off, Shifted, CapsLocked;
}

// These are the initial values of the state
object SenBoardStateDefaults {
    val DefaultModeType = SenModeType.Characters

    // TODO: Add number row support
    fun modeType(showNumberRow: Boolean) = DefaultModeType

    val FallbackShiftMode: ShiftMode = ShiftMode.Off

    fun shiftMode(autoCapitalizationEnabled: Boolean): ShiftMode =
        if (autoCapitalizationEnabled) ShiftMode.Shifted else FallbackShiftMode
}

data class SenBoardState(
    val modeType: SenModeType = SenBoardStateDefaults.DefaultModeType,
    val shiftMode: ShiftMode = SenBoardStateDefaults.FallbackShiftMode,
    val composingText: String = String.EMPTY,
    val wordSuggestions: List<String> = emptyList(),
) {
    val isShifted
        get() = shiftMode == ShiftMode.Shifted || shiftMode == ShiftMode.CapsLocked

    val mode
        get() = provideMode(modeType)
}
