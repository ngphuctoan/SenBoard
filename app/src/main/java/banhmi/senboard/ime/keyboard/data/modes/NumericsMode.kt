package banhmi.senboard.ime.keyboard.data.modes

import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode
import banhmi.senboard.ime.keyboard.models.KeyDisplay

val NumericsMode = mode("numerics", StandardLayout) {
    textKeys("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    textKeys("@", "#", "$", "%", "&", "-", "+", "(", ")")
    slot(display = KeyDisplay.Text("~]<"))
    textKeys("*", "/", "_", ":", ";", "'", "\"")
    backspaceKey()
    switchModeKey(ModeRegistry.Characters)
    textKeys("\\", "!")
    slot(handler = CharKeyHandler(" "))
    textKey("?")
    returnKey()
}