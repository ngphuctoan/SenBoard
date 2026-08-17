package banhmi.senboard.keyboard.impl.handler

import android.view.KeyEvent
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

object SenReturnKeyHandler : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) {
        imService.inputConnection.finishComposingText()
        imService.inputConnection.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER),
        )

        onSetState(state.copy(composingText = SenBoardStateDefaults.EmptyComposingText))
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
