package banhmi.senboard.ime.keyboard.models

import banhmi.senboard.ime.keyboard.core.SenBoardContext

interface KeyHandler {
    fun handle(context: SenBoardContext)
    fun handleDoubleTap(context: SenBoardContext) {
        handle(context)
    }
}
