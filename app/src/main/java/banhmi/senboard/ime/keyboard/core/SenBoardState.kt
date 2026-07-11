package banhmi.senboard.ime.keyboard.core

import androidx.compose.runtime.Immutable
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.models.Mode
import banhmi.senboard.ime.keyboard.models.ShiftMode

@Immutable
data class SenBoardState(
    val mode: Mode = CharactersMode,
    val shiftMode: ShiftMode = ShiftMode.Off,
) {
    val isShifted: Boolean
        get() = shiftMode != ShiftMode.Off
}
