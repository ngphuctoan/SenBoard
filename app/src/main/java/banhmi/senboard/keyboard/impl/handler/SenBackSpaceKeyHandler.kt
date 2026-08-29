package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

object SenBackSpaceKeyHandler : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        inputConnection.finishComposingText()

        /* For when deleting a portion of text, replace the selected text with blank,
        as deleteSurroundingText deletes the last character even when there is a selection */
        if (inputConnection.getSelectedText(0).isNullOrEmpty()) {
            inputConnection.deleteSurroundingText(1, 0)
        } else {
            inputConnection.commitText("", 1)
        }

        clearComposingText()
        clearWordSuggestions()
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
