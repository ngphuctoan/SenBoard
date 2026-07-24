package banhmi.senboard.ime.keyboard.models

import banhmi.senboard.ime.keyboard.core.handlers.NoOpKeyHandler

data class KeyAltData(
    val display: KeyDisplay = KeyDisplay.None,
    val handler: KeyHandler = NoOpKeyHandler,
    val isStartingPoint: Boolean = false,
)
