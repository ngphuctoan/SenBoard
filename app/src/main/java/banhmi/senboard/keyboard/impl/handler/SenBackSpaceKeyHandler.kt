package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

object SenBackSpaceKeyHandler : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
        onSaveBigram: (String, String) -> Unit,
    ) {
        imService.inputConnection.finishComposingText()

        /* For when deleting a portion of text, replace the selected text with blank,
        as deleteSurroundingText deletes the last character even when there is a selection */
        imService.inputConnection.run {
            if (getSelectedText(0).isNullOrEmpty()) {
                deleteSurroundingText(1, 0)
            } else {
                commitText("", 1)
            }
        }

        onSetState(state.copy(composingText = SenBoardStateDefaults.EmptyComposingText))
    }

    override fun handleDoubleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
        onSaveBigram: (String, String) -> Unit,
    ) = handleTap(state, onSetState, preferences, imService, onSaveBigram)

    override fun handleLongTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
        onSaveBigram: (String, String) -> Unit,
    ) = handleTap(state, onSetState, preferences, imService, onSaveBigram)
}
