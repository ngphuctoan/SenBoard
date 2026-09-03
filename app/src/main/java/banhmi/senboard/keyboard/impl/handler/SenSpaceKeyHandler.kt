package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

object SenSpaceKeyHandler : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        if (uiState.inputTypeComposingAllowed) {
            val convertedComposingText = preferencesState.vietnameseEngine //
                .convertWord(uiState.composingText)

            onUpdateWordSuggestions(onGetBestCandidates(convertedComposingText))
        }

        inputConnection.finishComposingText()

        val lastTwoChars = inputConnection.getTextBeforeCursor(2, 0)

        if (
            preferencesState.spaceBarShortcutEnabled //
            && lastTwoChars?.length == 2 //
            && lastTwoChars.last() == ' ' //
            && lastTwoChars.first().isLetterOrDigit()
        ) {
            inputConnection.deleteSurroundingText(1, 0)
            inputConnection.commitText(". ", 1)
        } else {
            inputConnection.commitText(" ", 1)
        }

        updateShiftModeAutomatically()
        clearComposingText()
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
