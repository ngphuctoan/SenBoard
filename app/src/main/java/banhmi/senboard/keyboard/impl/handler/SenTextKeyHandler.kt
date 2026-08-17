package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.SenImServiceFacade
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.model.SenKeyHandler

class SenTextKeyHandler(private val text: String) : SenKeyHandler {
    // Character key handler should be handling Vietnamese engine conversion instead
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) {
        imService.inputConnection.finishComposingText()
        imService.inputConnection.commitText(text, 1)

        onSetState(
            state.copy(
                shiftMode = if (state.shiftMode == ShiftMode.Shifted) {
                    ShiftMode.Off
                } else {
                    state.shiftMode
                },
                composingText = SenBoardStateDefaults.EmptyComposingText,
            ),
        )
    }

    override fun handleDoubleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) = handleTap(state, onSetState, preferences, imService)

    override fun handleLongTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) = handleTap(state, onSetState, preferences, imService)
}
