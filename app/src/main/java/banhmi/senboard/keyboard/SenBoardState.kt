package banhmi.senboard.keyboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ShiftMode {
    Off,
    Shifted,
    CapsLocked,
}

class SenBoardState {
    var shiftMode by mutableStateOf(ShiftMode.Off)
        internal set

    val isShifted get() = shiftMode != ShiftMode.Off
}
