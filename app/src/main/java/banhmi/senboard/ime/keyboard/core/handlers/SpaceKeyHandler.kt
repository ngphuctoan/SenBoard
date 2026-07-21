package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.shared.settings.InputMethodSettings

data object SpaceKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.getEditor()?.commitText(" ", 1)
    }

    override fun handleDoubleTap(context: SenBoardContext) {
        val editor = context.getEditor()

        if (context.inputMethodState.spaceBarShortcutEnabled && shouldInsertPeriod(context)) {
            editor?.deleteSurroundingText(1, 0)
            editor?.commitText(". ", 1)
        } else {
            handle(context)
        }
    }

    private fun shouldInsertPeriod(context: SenBoardContext): Boolean {
        val beforeCursor = context.getEditor()?.getTextBeforeCursor(2, 0) ?: return false
        return beforeCursor.endsWith(" ") && beforeCursor.dropLast(1).lastOrNull()
            ?.isWhitespace() == false
    }
}
