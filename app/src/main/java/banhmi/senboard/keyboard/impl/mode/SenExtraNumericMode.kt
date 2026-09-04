package banhmi.senboard.keyboard.impl.mode

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SpaceBar
import banhmi.senboard.keyboard.impl.handler.SenSpaceKeyHandler
import banhmi.senboard.keyboard.model.SenKeyDisplay
import banhmi.senboard.keyboard.model.SenLayoutType
import banhmi.senboard.keyboard.model.SenModeProvider
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.senAltNone
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senModeSwitcherKey
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSecondaryContainerKeyStyle
import banhmi.senboard.keyboard.model.senTextKey

// This layout is going to be shared for tel, decimal, and signed number input, I am too lazy :b
val senExtraNumericMode: SenModeProvider = { _, _ ->
    senMode(SenLayoutType.Numeric) {
        // First row
        senTextKey("+")
        senTextKey("-")
        senTextKey("*")
        senBackSpaceKey()

        // Second row
        senTextKey("/")
        senTextKey("<")
        senTextKey(">")
        senKey(
            styleProvider = senSecondaryContainerKeyStyle,
            display = SenKeyDisplay.Icon(Icons.Outlined.SpaceBar),
            handler = SenSpaceKeyHandler,
            altProvider = { senAltNone() },
        )

        // Third row
        senTextKey("=")
        senTextKey("&")
        senTextKey("%")
        senModeSwitcherKey(SenModeType.Numeric, "123")

        // Fourth row
        senTextKey("#")
        senTextKey("~")
        senTextKey("^")
        senReturnKey()
    }
}
