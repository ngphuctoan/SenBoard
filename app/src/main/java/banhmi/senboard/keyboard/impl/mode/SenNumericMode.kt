package banhmi.senboard.keyboard.impl.mode

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SpaceBar
import banhmi.senboard.keyboard.impl.handler.SenSpaceKeyHandler
import banhmi.senboard.keyboard.model.SenKeyDisplay
import banhmi.senboard.keyboard.model.SenLayoutType
import banhmi.senboard.keyboard.model.SenModeProvider
import banhmi.senboard.keyboard.model.senAltNone
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senNumberKey
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSecondaryContainerKeyStyle
import banhmi.senboard.keyboard.model.senTertiaryContainerKeyStyle
import banhmi.senboard.keyboard.model.senTextKey

// This layout is going to be shared for tel, decimal, and signed number input, I am too lazy :b
val senNumericMode: SenModeProvider = { _, _ ->
    senMode(SenLayoutType.Numeric) {
        // First row
        senNumberKey(1, "")
        senNumberKey(2, "ABC")
        senNumberKey(3, "DEF")
        senBackSpaceKey()

        // Second row
        senNumberKey(4, "GHI")
        senNumberKey(5, "JKL")
        senNumberKey(6, "MNO")
        senKey(
            styleProvider = senSecondaryContainerKeyStyle,
            display = SenKeyDisplay.Icon(Icons.Outlined.SpaceBar),
            handler = SenSpaceKeyHandler,
            altProvider = { senAltNone() },
        )

        // Third row
        senNumberKey(7, "PQRS")
        senNumberKey(8, "TUV")
        senNumberKey(9, "WXYZ")
        senTextKey("*+#", styleProvider = senTertiaryContainerKeyStyle)
        //senModeSwitcherKey(SenModeType.Numbers, "123")

        // Fourth row
        senTextKey("-")
        senNumberKey(0, "+")
        senTextKey(",")
        senReturnKey()
    }
}
