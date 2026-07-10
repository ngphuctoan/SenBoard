package banhmi.senboard.keyboard.keys

import banhmi.senboard.keyboard.SenBoardContext

sealed interface KeyAction {
    data class Character(val raw: String) : KeyAction

    data object Space : KeyAction
    data object Backspace : KeyAction
    data object Shift : KeyAction
    data object Enter : KeyAction

    data class Custom(
        val handler: SenBoardContext.() -> Unit
    ) : KeyAction

    data object None : KeyAction
}
