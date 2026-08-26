package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.engine.provideVietnameseEngine
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

object SenSpaceKeyHandler : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
        onSaveBigram: (String, String) -> Unit,
    ) {
        val engine = provideVietnameseEngine(preferences.vietnameseEngineType)
        val composedText = engine.convertWord(state.composingText)

        imService.inputConnection.finishComposingText()
        imService.inputConnection.commitText(" ", 1)

        if (composedText.isNotBlank() && state.previousWord.isNotBlank())
            onSaveBigram(state.previousWord, composedText)

        onSetState(
            state.copy(
                composingText = SenBoardStateDefaults.EmptyComposingText,
                previousWord = composedText,
            ),
        )
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
