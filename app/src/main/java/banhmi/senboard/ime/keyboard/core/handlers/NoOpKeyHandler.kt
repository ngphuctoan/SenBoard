package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.shared.settings.InputMethodSettings

data object NoOpKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) = Unit
}
