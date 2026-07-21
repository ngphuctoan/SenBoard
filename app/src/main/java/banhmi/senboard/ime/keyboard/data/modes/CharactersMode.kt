package banhmi.senboard.ime.keyboard.data.modes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.KeyboardAlt
import banhmi.senboard.ime.keyboard.core.handlers.BackSpaceKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ReturnKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.SpaceKeyHandler
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
            display = KeyDisplay.Char("q"),
            handler = CharKeyHandler("q"),
        ),
        KeyData(
            display = KeyDisplay.Char("w"),
            handler = CharKeyHandler("w"),
        ),
        KeyData(
            display = KeyDisplay.Char("e"),
            handler = CharKeyHandler("e"),
        ),
        KeyData(
            display = KeyDisplay.Char("r"),
            handler = CharKeyHandler("r"),
        ),
        KeyData(
            display = KeyDisplay.Char("t"),
            handler = CharKeyHandler("t"),
        ),
        KeyData(
            display = KeyDisplay.Char("y"),
            handler = CharKeyHandler("y"),
        ),
        KeyData(
            display = KeyDisplay.Char("u"),
            handler = CharKeyHandler("u"),
        ),
        KeyData(
            display = KeyDisplay.Char("i"),
            handler = CharKeyHandler("i"),
        ),
        KeyData(
            display = KeyDisplay.Char("o"),
            handler = CharKeyHandler("o"),
        ),
        KeyData(
            display = KeyDisplay.Char("p"),
            handler = CharKeyHandler("p"),
        ),

        KeyData(
            display = KeyDisplay.Char("a"),
            handler = CharKeyHandler("a"),
        ),
        KeyData(
            display = KeyDisplay.Char("s"),
            handler = CharKeyHandler("s"),
        ),
        KeyData(
            display = KeyDisplay.Char("d"),
            handler = CharKeyHandler("d"),
        ),
        KeyData(
            display = KeyDisplay.Char("f"),
            handler = CharKeyHandler("f"),
        ),
        KeyData(
            display = KeyDisplay.Char("g"),
            handler = CharKeyHandler("g"),
        ),
        KeyData(
            display = KeyDisplay.Char("h"),
            handler = CharKeyHandler("h"),
        ),
        KeyData(
            display = KeyDisplay.Char("j"),
            handler = CharKeyHandler("j"),
        ),
        KeyData(
            display = KeyDisplay.Char("k"),
            handler = CharKeyHandler("k"),
        ),
        KeyData(
            display = KeyDisplay.Char("l"),
            handler = CharKeyHandler("l"),
        ),

        KeyData(
            display = KeyDisplay.Shift,
            handler = ShiftKeyHandler,
        ),
        KeyData(
            display = KeyDisplay.Char("z"),
            handler = CharKeyHandler("z"),
        ),
        KeyData(
            display = KeyDisplay.Char("x"),
            handler = CharKeyHandler("x"),
        ),
        KeyData(
            display = KeyDisplay.Char("c"),
            handler = CharKeyHandler("c"),
        ),
        KeyData(
            display = KeyDisplay.Char("v"),
            handler = CharKeyHandler("v"),
        ),
        KeyData(
            display = KeyDisplay.Char("b"),
            handler = CharKeyHandler("b"),
        ),
        KeyData(
            display = KeyDisplay.Char("n"),
            handler = CharKeyHandler("n"),
        ),
        KeyData(
            display = KeyDisplay.Char("m"),
            handler = CharKeyHandler("m"),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
            handler = BackSpaceKeyHandler,
        ),

        KeyData(
            display = KeyDisplay.Text("?123"),
            handler = SwitchModeHandler(ModeRegistry.Numerics),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.Outlined.KeyboardAlt),
        ),
        KeyData(
            display = KeyDisplay.Text(","),
            handler = CharKeyHandler(","),
        ),
        KeyData(
            handler = SpaceKeyHandler,
        ),
        KeyData(
            display = KeyDisplay.Text("."),
            handler = CharKeyHandler("."),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
            handler = ReturnKeyHandler,
        ),
    ),
)
