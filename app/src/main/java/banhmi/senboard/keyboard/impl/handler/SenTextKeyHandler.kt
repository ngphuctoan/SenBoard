package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext
import banhmi.senboard.keyboard.state.ShiftMode

class SenTextKeyHandler(
    private val text: String,
) : SenKeyHandler {
    // Character key handler should be handling Vietnamese engine conversion instead
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        inputConnection.finishComposingText()
        inputConnection.commitText(text, 1)

        onUpdateShiftMode(
            when (uiState.shiftMode) {
                ShiftMode.Shifted -> ShiftMode.Off
                else -> uiState.shiftMode
            },
        )
        clearComposingText()
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
