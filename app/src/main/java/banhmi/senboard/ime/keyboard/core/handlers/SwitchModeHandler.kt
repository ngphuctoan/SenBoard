package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.data.modes.ModeRegistry
import banhmi.senboard.ime.keyboard.data.modes.value
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.shared.settings.InputMethodSettings

class SwitchModeHandler(private val modeFromRegistry: ModeRegistry) : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.state = context.state.copy(mode = modeFromRegistry.value)
    }
}
