package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler

data object NoOpKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) = Unit
}
