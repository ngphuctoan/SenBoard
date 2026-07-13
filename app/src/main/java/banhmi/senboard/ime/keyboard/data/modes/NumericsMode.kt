package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.InsertEmoticon
import banhmi.senboard.ime.keyboard.core.handlers.BackSpaceKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ReturnKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.SwitchModeHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.models.KeyData
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.Mode

val NumericsMode = Mode(
    name = "numerics",
    layout = StandardLayout,
    slots = listOf(
        KeyData(
            display = KeyDisplay.Text("1"),
            handler = CharKeyHandler("1"),
        ),
        KeyData(
            display = KeyDisplay.Text("2"),
            handler = CharKeyHandler("2"),
        ),
        KeyData(
            display = KeyDisplay.Text("3"),
            handler = CharKeyHandler("3"),
        ),
        KeyData(
            display = KeyDisplay.Text("4"),
            handler = CharKeyHandler("4"),
        ),
        KeyData(
            display = KeyDisplay.Text("5"),
            handler = CharKeyHandler("5"),
        ),
        KeyData(
            display = KeyDisplay.Text("6"),
            handler = CharKeyHandler("6"),
        ),
        KeyData(
            display = KeyDisplay.Text("7"),
            handler = CharKeyHandler("7"),
        ),
        KeyData(
            display = KeyDisplay.Text("8"),
            handler = CharKeyHandler("8"),
        ),
        KeyData(
            display = KeyDisplay.Text("9"),
            handler = CharKeyHandler("9"),
        ),
        KeyData(
            display = KeyDisplay.Text("0"),
            handler = CharKeyHandler("0"),
        ),

        KeyData(
            display = KeyDisplay.Text("-"),
            handler = CharKeyHandler("-"),
        ),
        KeyData(
            display = KeyDisplay.Text("/"),
            handler = CharKeyHandler("/"),
        ),
        KeyData(
            display = KeyDisplay.Text(":"),
            handler = CharKeyHandler(":"),
        ),
        KeyData(
            display = KeyDisplay.Text(";"),
            handler = CharKeyHandler(";"),
        ),
        KeyData(
            display = KeyDisplay.Text("("),
            handler = CharKeyHandler("("),
        ),
        KeyData(
            display = KeyDisplay.Text(")"),
            handler = CharKeyHandler(")"),
        ),
        KeyData(
            display = KeyDisplay.Text("$"),
            handler = CharKeyHandler("$"),
        ),
        KeyData(
            display = KeyDisplay.Text("&"),
            handler = CharKeyHandler("&"),
        ),
        KeyData(
            display = KeyDisplay.Text("@"),
            handler = CharKeyHandler("@"),
        ),

        KeyData(
            display = KeyDisplay.Text("#+="),
        ),
        KeyData(
            display = KeyDisplay.Text("."),
            handler = CharKeyHandler("."),
        ),
        KeyData(
            display = KeyDisplay.Text(","),
            handler = CharKeyHandler(","),
        ),
        KeyData(
            display = KeyDisplay.Text("?"),
            handler = CharKeyHandler("?"),
        ),
        KeyData(
            display = KeyDisplay.Text("!"),
            handler = CharKeyHandler("!"),
        ),
        KeyData(
            display = KeyDisplay.Text("'"),
            handler = CharKeyHandler("'"),
        ),
        KeyData(
            display = KeyDisplay.Text("\""),
            handler = CharKeyHandler("\""),
        ),
        KeyData(
            display = KeyDisplay.Text("^"),
            handler = CharKeyHandler("^"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
            handler = BackSpaceKeyHandler,
        ),

        KeyData(
            display = KeyDisplay.Text("ABC"),
            handler = SwitchModeHandler(ModeRegistry.Characters),
        ),
        KeyData(
            display = KeyDisplay.Text("~"),
            handler = CharKeyHandler("~"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.Outlined.InsertEmoticon),
        ),
        KeyData(
            handler = CharKeyHandler(" "),
        ),
        KeyData(
            display = KeyDisplay.Text("\\"),
            handler = CharKeyHandler("\\"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
            handler = ReturnKeyHandler,
        ),
    ),
)