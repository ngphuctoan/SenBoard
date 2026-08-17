package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.engine.provideVietnameseEngine
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

class SenCharKeyHandler(private val char: Char) : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) {
        val engine = provideVietnameseEngine(preferences.vietnameseEngineType)

        val newChar = char.run { if (state.isShifted) uppercase() else lowercase() }
        val newComposingText = state.composingText + newChar

        imService.inputConnection.setComposingText(engine.convertWord(newComposingText), 1)

        onSetState(
            state.copy(
                shiftMode = if (state.shiftMode == ShiftMode.Shifted) {
                    ShiftMode.Off
                } else {
                    state.shiftMode
                },
                composingText = newComposingText,
            ),
        )
    }

    override fun handleDoubleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) = handleTap(state, onSetState, preferences, imService)

    override fun handleLongTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) = handleTap(state, onSetState, preferences, imService)
}
