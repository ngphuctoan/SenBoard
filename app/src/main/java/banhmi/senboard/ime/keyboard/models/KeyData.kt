package banhmi.senboard.ime.keyboard.models

import banhmi.senboard.ime.keyboard.core.handlers.NoOpKeyHandler

data class KeyData(
    val display: KeyDisplay = KeyDisplay.None,
    val handler: KeyHandler = NoOpKeyHandler,
)
