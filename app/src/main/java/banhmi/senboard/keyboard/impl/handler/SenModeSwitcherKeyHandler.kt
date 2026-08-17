package banhmi.senboard.keyboard.impl.handler

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.SenImServiceFacade
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.model.SenKeyHandler
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.provideMode

class SenModeSwitcherKeyHandler(private val modeType: SenModeType) : SenKeyHandler {
    override fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) {
        onSetState(state.copy(mode = provideMode(modeType)))
    }
}
