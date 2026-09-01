package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

class SenTextKeyHandler(
    private val text: String,
) : SenKeyHandler {
    // Character key handler should be handling Vietnamese engine conversion instead
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        if (uiState.inputTypeComposingAllowed) {
            val convertedComposingText = preferencesState.vietnameseEngine //
                .convertWord(uiState.composingText)

            onUpdateWordSuggestions(onGetClosestWords(convertedComposingText))
        }

        inputConnection.finishComposingText()
        inputConnection.commitText(text, 1)

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
