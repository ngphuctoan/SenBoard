package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

class SenCharKeyHandler(
    private val char: Char,
) : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        val newChar = char.run { if (uiState.isShifted) uppercase() else lowercase() }
        val newComposingText = uiState.composingText + newChar
        val convertedComposingText = preferencesState.vietnameseEngine //
            .convertWord(newComposingText)

        inputConnection.setComposingText(convertedComposingText, 1)

        updateShiftModeAutomatically()
        onUpdateComposingText(newComposingText)
        onUpdateWordSuggestions(
            onGetClosestWords(convertedComposingText),
        )
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
