package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.shared.settings.InputMethodSettings

data object ShiftKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.state = context.state.copy(
            shiftMode = when (context.state.shiftMode) {
                ShiftMode.Off -> ShiftMode.Manual
                else -> ShiftMode.Off
            }
        )
    }

    override fun handleDoubleTap(context: SenBoardContext) {
        context.state = context.state.copy(shiftMode = ShiftMode.CapsLocked)
    }
}
