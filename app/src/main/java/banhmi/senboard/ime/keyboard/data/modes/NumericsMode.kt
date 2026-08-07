package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode

val NumericsMode = mode("numerics", StandardLayout) {
    // Row 1
    textKey("1")
    textKey("2")
    textKey("3")
    textKey("4")
    textKey("5")
    textKey("6")
    textKey("7")
    textKey("8")
    textKey("9")
    textKey("0")

    // Row 2
    textKey("@")
    textKey("#")
    textKey("$")
    textKey("%")
    textKey("&")
    textKey("-")
    textKey("+")
    textKey("(")
    textKey(")")

    // Row 3: Switch to Symbolics (=\<) + Punctuation + Backspace
    switchModeKey(ModeRegistry.Symbolics, displayLabel = "=\\<")
    textKey("*")
    textKey("/")
    textKey("_")
    textKey(":")
    textKey(";")
    textKey("'")
    textKey("\"")
    backspaceKey()

    // Row 4: Switch to Characters (ABC) + Punctuation + Space + Return
    switchModeKey(ModeRegistry.Characters)
    charKey(",")
    charKey(".")
    iconKey(Icons.Outlined.KeyboardAlt, CharKeyHandler(" "))
    charKey("?")
    returnKey()
}