package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext
import banhmi.senboard.keyboard.state.ShiftMode

object SenShiftKeyHandler : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        onUpdateShiftMode(
            when (uiState.shiftMode) {
                ShiftMode.Off -> ShiftMode.Shifted
                ShiftMode.Shifted, ShiftMode.CapsLocked -> ShiftMode.Off
            },
        )
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = context.onUpdateShiftMode(ShiftMode.CapsLocked)
}
