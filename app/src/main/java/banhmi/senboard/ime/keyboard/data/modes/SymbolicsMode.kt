package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode

val SymbolicsMode = mode("symbolics", StandardLayout) {
    // Row 1: Extended symbols & math operators
    textKey("~")
    textKey("`")
    textKey("|")
    textKey("•")
    textKey("√")
    textKey("π")
    textKey("÷")
    textKey("×")
    textKey("¶")
    textKey("∆")

    // Row 2: Currency & special characters
    textKey("£")
    textKey("¥")
    textKey("€")
    textKey("¢")
    textKey("^")
    textKey("°")
    textKey("=")
    textKey("{")
    textKey("}")

    // Row 3: Switch to Numerics (?123) + Brackets & Quotes + Backspace
    switchModeKey(ModeRegistry.Numerics, displayLabel = "?123")
    textKey("\\")
    textKey("%")
    textKey("<")
    textKey(">")
    textKey("«")
    textKey("»")
    textKey("[")
    backspaceKey()

    // Row 4: Switch to Characters (ABC) + Punctuation + Space + Return
    switchModeKey(ModeRegistry.Characters)
    charKey(",")
    charKey(".")
    iconKey(Icons.Outlined.KeyboardAlt, CharKeyHandler(" "))
    charKey("?")
    returnKey()
}
