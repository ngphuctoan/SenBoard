package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.shared.settings.InputMethodSettings

data object BackSpaceKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) {
        val editor = context.getEditor()
        val selectedText = editor?.getSelectedText(0)

        if (!selectedText.isNullOrEmpty()) editor.commitText("", 1)
        else editor?.deleteSurroundingText(1, 0)
    }
}
