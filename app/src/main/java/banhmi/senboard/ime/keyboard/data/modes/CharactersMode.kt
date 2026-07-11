package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.InsertEmoticon
import banhmi.senboard.ime.keyboard.core.handlers.BackSpaceKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ReturnKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.SwitchModeHandler
import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.models.KeyData
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.Mode

val CharactersMode = Mode(
    name = "characters",
    layout = StandardLayout,
    slots = listOf(
        KeyData(
            display = KeyDisplay.Text("q"),
            handler = CharKeyHandler("q"),
        ),
        KeyData(
            display = KeyDisplay.Text("w"),
            handler = CharKeyHandler("w"),
        ),
        KeyData(
            display = KeyDisplay.Text("e"),
            handler = CharKeyHandler("e"),
        ),
        KeyData(
            display = KeyDisplay.Text("r"),
            handler = CharKeyHandler("r"),
        ),
        KeyData(
            display = KeyDisplay.Text("t"),
            handler = CharKeyHandler("t"),
        ),
        KeyData(
            display = KeyDisplay.Text("y"),
            handler = CharKeyHandler("y"),
        ),
        KeyData(
            display = KeyDisplay.Text("u"),
            handler = CharKeyHandler("u"),
        ),
        KeyData(
            display = KeyDisplay.Text("i"),
            handler = CharKeyHandler("i"),
        ),
        KeyData(
            display = KeyDisplay.Text("o"),
            handler = CharKeyHandler("o"),
        ),
        KeyData(
            display = KeyDisplay.Text("p"),
            handler = CharKeyHandler("p"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
            handler = BackSpaceKeyHandler,
        ),

        KeyData(
            display = KeyDisplay.Text("a"),
            handler = CharKeyHandler("a"),
        ),
        KeyData(
            display = KeyDisplay.Text("s"),
            handler = CharKeyHandler("s"),
        ),
        KeyData(
            display = KeyDisplay.Text("d"),
            handler = CharKeyHandler("d"),
        ),
        KeyData(
            display = KeyDisplay.Text("f"),
            handler = CharKeyHandler("f"),
        ),
        KeyData(
            display = KeyDisplay.Text("g"),
            handler = CharKeyHandler("g"),
        ),
        KeyData(
            display = KeyDisplay.Text("h"),
            handler = CharKeyHandler("h"),
        ),
        KeyData(
            display = KeyDisplay.Text("j"),
            handler = CharKeyHandler("j"),
        ),
        KeyData(
            display = KeyDisplay.Text("k"),
            handler = CharKeyHandler("k"),
        ),
        KeyData(
            display = KeyDisplay.Text("l"),
            handler = CharKeyHandler("l"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
            handler = ReturnKeyHandler,
        ),

        KeyData(
            display = KeyDisplay.Shift,
            handler = ShiftKeyHandler,
        ),
        KeyData(
            display = KeyDisplay.Text("z"),
            handler = CharKeyHandler("z"),
        ),
        KeyData(
            display = KeyDisplay.Text("x"),
            handler = CharKeyHandler("x"),
        ),
        KeyData(
            display = KeyDisplay.Text("c"),
            handler = CharKeyHandler("c"),
        ),
        KeyData(
            display = KeyDisplay.Text("v"),
            handler = CharKeyHandler("v"),
        ),
        KeyData(
            display = KeyDisplay.Text("b"),
            handler = CharKeyHandler("b"),
        ),
        KeyData(
            display = KeyDisplay.Text("n"),
            handler = CharKeyHandler("n"),
        ),
        KeyData(
            display = KeyDisplay.Text("m"),
            handler = CharKeyHandler("m"),
        ),
        KeyData(
            display = KeyDisplay.Text("!"),
            handler = CharKeyHandler("!"),
        ),
        KeyData(
            display = KeyDisplay.Text("?"),
            handler = CharKeyHandler("?"),
        ),
        KeyData(
            display = KeyDisplay.Shift,
            handler = ShiftKeyHandler,
        ),

        KeyData(
            display = KeyDisplay.Text("?123"),
            handler = SwitchModeHandler(ModeId.Numerics),
        ),
        KeyData(
            display = KeyDisplay.Text(","),
            handler = CharKeyHandler(","),
        ),
        KeyData(
            handler = CharKeyHandler(" "),
        ),
        KeyData(
            display = KeyDisplay.Text("."),
            handler = CharKeyHandler("."),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.Outlined.InsertEmoticon),
        ),
    ),
)
