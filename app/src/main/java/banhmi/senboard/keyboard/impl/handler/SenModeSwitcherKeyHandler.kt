package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext
import banhmi.senboard.keyboard.model.SenModeType

class SenModeSwitcherKeyHandler(
    private val modeType: SenModeType,
) : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        onUpdateModeType(modeType)
        clearComposingText()
    }
}
