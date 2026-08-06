package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode

val CharactersMode = mode("characters", StandardLayout) {
    charKeys("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    charKeys("a", "s", "d", "f", "g", "h", "j", "k", "l")
    shiftKey()
    charKeys("z", "x", "c", "v", "b", "n", "m")
    backspaceKey()
    switchModeKey(ModeRegistry.Numerics)
    charKey(",")
    charKey(".")
    iconKey(Icons.Outlined.KeyboardAlt, CharKeyHandler(" "))
    charKey("?")
    returnKey()
}
