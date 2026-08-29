package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

object SenSpaceKeyHandler : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        val convertedComposingText = preferencesState.vietnameseEngine //
            .convertWord(uiState.composingText)

        inputConnection.finishComposingText()
        inputConnection.commitText(" ", 1)

        clearComposingText()
        onUpdateWordSuggestions(
            onGetBestCandidates(convertedComposingText),
        )
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
