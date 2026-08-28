package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.engine.provideVietnameseEngine
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

class SenTextKeyHandler(private val text: String) : SenKeyHandler {
    // Character key handler should be handling Vietnamese engine conversion instead
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
        imService.inputConnection.commitText(text, 1)

        if (composedText.isNotBlank() && state.previousWord.isNotBlank())
            onSaveBigram(state.previousWord, composedText)

        onSetState(
            state.copy(
                shiftMode = if (state.shiftMode == ShiftMode.Shifted) {
                    ShiftMode.Off
                } else {
                    state.shiftMode
                },
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
