package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.dsl.mode

val SymbolicsMode = mode("symbolics", StandardLayout) {
    // Row 1: Extended symbols & math operators (10 keys)
    textKeys("~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆")

    // Row 2: Currency & special characters (9 keys)
    textKeys("£", "¥", "€", "¢", "^", "°", "=", "{", "}")

    // Row 3: Switch to Numerics (?123) + Brackets & Quotes + Backspace (9 keys)
    switchModeKey(ModeRegistry.Numerics, displayLabel = "?123")
    textKeys("\\", "%", "<", ">", "«", "»", "[")
    backspaceKey()

    // Row 4: Switch to Characters (ABC) + Punctuation + Space + Return (6 keys)
    switchModeKey(ModeRegistry.Characters)
    charKey(",")
    charKey(".")
    iconKey(Icons.Outlined.KeyboardAlt, CharKeyHandler(" "))
    charKey("?")
    returnKey()
}
