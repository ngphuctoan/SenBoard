package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.SpaceBar
import banhmi.senboard.ime.keyboard.core.handlers.BackSpaceKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ReturnKeyHandler
import banhmi.senboard.ime.keyboard.data.layouts.AaaaaLayout
import banhmi.senboard.ime.keyboard.models.KeyData
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.Mode

// Easter egg inspired by "aaaaa" by dkter
// App repo: https://github.com/dkter/aaaaa
val AaaaaMode = Mode(
    name = "aaaaa",
    layout = AaaaaLayout,
    slots = listOf(
        KeyData(
            display = KeyDisplay.Char("a"),
            handler = CharKeyHandler("a"),
        ),

        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
            handler = BackSpaceKeyHandler,
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.Outlined.SpaceBar),
            handler = CharKeyHandler(" "),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
            handler = ReturnKeyHandler,
        ),
    ),
)
