package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode

val CharactersMode = mode("characters", StandardLayout) {
    // Row 1
    charKey("q")
    charKey("w")
    charKey("e")
    charKey("r")
    charKey("t")
    charKey("y")
    charKey("u")
    charKey("i")
    charKey("o")
    charKey("p")

    // Row 2
    charKey("a")
    charKey("s")
    charKey("d")
    charKey("f")
    charKey("g")
    charKey("h")
    charKey("j")
    charKey("k")
    charKey("l")

    // Row 3
    shiftKey()
    charKey("z")
    charKey("x")
    charKey("c")
    charKey("v")
    charKey("b")
    charKey("n")
    charKey("m")
    backspaceKey()

    // Row 4
    switchModeKey(ModeRegistry.Numerics)
    charKey(",")
    charKey(".")
    iconKey(Icons.Outlined.KeyboardAlt, CharKeyHandler(" "))
    charKey("?")
    returnKey()
}
