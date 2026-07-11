package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode

class CharKeyHandler(private val char: String) : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.getEditor()?.commitText(char.applyShiftMode(context.state.shiftMode), 1)
    }

    private fun String.applyShiftMode(shiftMode: ShiftMode): String =
        if (shiftMode == ShiftMode.Off) lowercase() else uppercase()
}
