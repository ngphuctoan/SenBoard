package banhmi.senboard.keyboard.impl.handler

import android.view.KeyEvent
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenKeyHandlerContext

object SenReturnKeyHandler : SenKeyHandler {
    override fun handleTap(
        context: SenKeyHandlerContext,
    ) = context.run {
        if (uiState.inputTypeComposingAllowed) {
            val convertedComposingText = preferencesState.vietnameseEngine //
                .convertWord(uiState.composingText)

            onUpdateWordSuggestions(onGetClosestWords(convertedComposingText))
        }

        inputConnection.finishComposingText()
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))

        clearComposingText()
    }

    override fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)

    override fun handleLongTap(
        context: SenKeyHandlerContext,
    ) = handleTap(context)
}
