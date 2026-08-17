package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.proxy.SenImServiceProxy

object SenShiftKeyHandler : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) = onSetState(
        state.copy(
            shiftMode = when (state.shiftMode) {
                ShiftMode.Off -> ShiftMode.Shifted
                ShiftMode.Shifted, ShiftMode.CapsLocked -> ShiftMode.Off
            },
        )
    )

    override fun handleDoubleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) = onSetState(state.copy(shiftMode = ShiftMode.CapsLocked))

    override fun handleLongTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceProxy,
    ) = handleTap(state, onSetState, preferences, imService)
}
