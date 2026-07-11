package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode

data object ShiftKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.state = context.state.copy(
            shiftMode = when (context.state.shiftMode) {
                ShiftMode.Off -> ShiftMode.Shifted
                ShiftMode.Shifted, ShiftMode.CapsLocked -> ShiftMode.Off
            }
        )
    }

    override fun handleDoubleTap(context: SenBoardContext) {
        context.state = context.state.copy(shiftMode = ShiftMode.CapsLocked)
    }
}
