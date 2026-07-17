package banhmi.senboard.ime.keyboard.core

import banhmi.senboard.ime.keyboard.models.KeyHandler

class SenBoardController(private val context: SenBoardContext) {
    val state: SenBoardState
        get() = context.state

    fun handle(handler: KeyHandler) {
        handler.handle(context)
    }

    fun handleDoubleTap(handler: KeyHandler) {
        handler.handleDoubleTap(context)
    }
}
