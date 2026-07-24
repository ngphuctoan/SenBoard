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
import banhmi.senboard.ime.keyboard.models.KeyAltData
import banhmi.senboard.ime.keyboard.models.KeyAltRow
import banhmi.senboard.ime.keyboard.models.KeyAltPopup
import banhmi.senboard.ime.keyboard.models.Mode
import banhmi.senboard.ime.keyboard.models.PopupAlignment

val CharactersMode = Mode(
    name = "characters",
    layout = StandardLayout,
    slots = listOf(
        // ROW 1
        KeyData(
            display = KeyDisplay.Char("q"),
            handler = CharKeyHandler("q"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("1"),
                                CharKeyHandler("1"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("w"),
            handler = CharKeyHandler("w"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("2"),
                                CharKeyHandler("2"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("e"),
            handler = CharKeyHandler("e"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ê"), CharKeyHandler("ê")),
                            KeyAltData(KeyDisplay.Char("ế"), CharKeyHandler("ế")),
                            KeyAltData(KeyDisplay.Char("ề"), CharKeyHandler("ề")),
                            KeyAltData(KeyDisplay.Char("ể"), CharKeyHandler("ể")),
                            KeyAltData(KeyDisplay.Char("ễ"), CharKeyHandler("ễ")),
                            KeyAltData(KeyDisplay.Char("ệ"), CharKeyHandler("ệ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("é"), CharKeyHandler("é")),
                            KeyAltData(KeyDisplay.Char("è"), CharKeyHandler("è")),
                            KeyAltData(KeyDisplay.Char("ẻ"), CharKeyHandler("ẻ")),
                            KeyAltData(KeyDisplay.Char("ẽ"), CharKeyHandler("ẽ")),
                            KeyAltData(KeyDisplay.Char("ẹ"), CharKeyHandler("ẹ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("3"),
                                CharKeyHandler("3"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("r"),
            handler = CharKeyHandler("r"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("4"),
                                CharKeyHandler("4"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("t"),
            handler = CharKeyHandler("t"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("5"),
                                CharKeyHandler("5"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("y"),
            handler = CharKeyHandler("y"),
            popup = KeyAltPopup(
                alignment = PopupAlignment.Center,
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ý"), CharKeyHandler("ý")),
                            KeyAltData(KeyDisplay.Char("ỳ"), CharKeyHandler("ỳ")),
                            KeyAltData(KeyDisplay.Char("ỷ"), CharKeyHandler("ỷ")),
                            KeyAltData(KeyDisplay.Char("ỹ"), CharKeyHandler("ỹ")),
                            KeyAltData(KeyDisplay.Char("ỵ"), CharKeyHandler("ỵ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("6"),
                                CharKeyHandler("6"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("u"),
            handler = CharKeyHandler("u"),
            popup = KeyAltPopup(
                alignment = PopupAlignment.End,
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ư"), CharKeyHandler("ư")),
                            KeyAltData(KeyDisplay.Char("ứ"), CharKeyHandler("ứ")),
                            KeyAltData(KeyDisplay.Char("ừ"), CharKeyHandler("ừ")),
                            KeyAltData(KeyDisplay.Char("ử"), CharKeyHandler("ử")),
                            KeyAltData(KeyDisplay.Char("ữ"), CharKeyHandler("ữ")),
                            KeyAltData(KeyDisplay.Char("ự"), CharKeyHandler("ự")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ú"), CharKeyHandler("ú")),
                            KeyAltData(KeyDisplay.Char("ù"), CharKeyHandler("ù")),
                            KeyAltData(KeyDisplay.Char("ủ"), CharKeyHandler("ủ")),
                            KeyAltData(KeyDisplay.Char("ũ"), CharKeyHandler("ũ")),
                            KeyAltData(KeyDisplay.Char("ụ"), CharKeyHandler("ụ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("7"),
                                CharKeyHandler("7"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("i"),
            handler = CharKeyHandler("i"),
            popup = KeyAltPopup(
                alignment = PopupAlignment.End,
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("í"), CharKeyHandler("í")),
                            KeyAltData(KeyDisplay.Char("ì"), CharKeyHandler("ì")),
                            KeyAltData(KeyDisplay.Char("ỉ"), CharKeyHandler("ỉ")),
                            KeyAltData(KeyDisplay.Char("ĩ"), CharKeyHandler("ĩ")),
                            KeyAltData(KeyDisplay.Char("ị"), CharKeyHandler("ị")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("8"),
                                CharKeyHandler("8"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("o"),
            handler = CharKeyHandler("o"),
            popup = KeyAltPopup(
                alignment = PopupAlignment.End,
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ơ"), CharKeyHandler("ơ")),
                            KeyAltData(KeyDisplay.Char("ớ"), CharKeyHandler("ớ")),
                            KeyAltData(KeyDisplay.Char("ờ"), CharKeyHandler("ờ")),
                            KeyAltData(KeyDisplay.Char("ở"), CharKeyHandler("ở")),
                            KeyAltData(KeyDisplay.Char("ỡ"), CharKeyHandler("ỡ")),
                            KeyAltData(KeyDisplay.Char("ợ"), CharKeyHandler("ợ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ô"), CharKeyHandler("ô")),
                            KeyAltData(KeyDisplay.Char("ố"), CharKeyHandler("ố")),
                            KeyAltData(KeyDisplay.Char("ồ"), CharKeyHandler("ồ")),
                            KeyAltData(KeyDisplay.Char("ổ"), CharKeyHandler("ổ")),
                            KeyAltData(KeyDisplay.Char("ỗ"), CharKeyHandler("ỗ")),
                            KeyAltData(KeyDisplay.Char("ộ"), CharKeyHandler("ộ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ó"), CharKeyHandler("ó")),
                            KeyAltData(KeyDisplay.Char("ò"), CharKeyHandler("ò")),
                            KeyAltData(KeyDisplay.Char("ỏ"), CharKeyHandler("ỏ")),
                            KeyAltData(KeyDisplay.Char("õ"), CharKeyHandler("õ")),
                            KeyAltData(KeyDisplay.Char("ọ"), CharKeyHandler("ọ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("9"),
                                CharKeyHandler("9"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("p"),
            handler = CharKeyHandler("p"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("0"), CharKeyHandler("0"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),

        // ROW 2
        KeyData(
            display = KeyDisplay.Char("a"),
            handler = CharKeyHandler("a"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("ă"), CharKeyHandler("ă")),
                            KeyAltData(KeyDisplay.Char("ắ"), CharKeyHandler("ắ")),
                            KeyAltData(KeyDisplay.Char("ằ"), CharKeyHandler("ằ")),
                            KeyAltData(KeyDisplay.Char("ẳ"), CharKeyHandler("ẳ")),
                            KeyAltData(KeyDisplay.Char("ẵ"), CharKeyHandler("ẵ")),
                            KeyAltData(KeyDisplay.Char("ặ"), CharKeyHandler("ặ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("â"), CharKeyHandler("â")),
                            KeyAltData(KeyDisplay.Char("ấ"), CharKeyHandler("ấ")),
                            KeyAltData(KeyDisplay.Char("ầ"), CharKeyHandler("ầ")),
                            KeyAltData(KeyDisplay.Char("ẩ"), CharKeyHandler("ẩ")),
                            KeyAltData(KeyDisplay.Char("ẫ"), CharKeyHandler("ẫ")),
                            KeyAltData(KeyDisplay.Char("ậ"), CharKeyHandler("ậ")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("á"),
                                CharKeyHandler("á"),
                                isStartingPoint = true,
                            ),
                            KeyAltData(KeyDisplay.Char("à"), CharKeyHandler("à")),
                            KeyAltData(KeyDisplay.Char("ả"), CharKeyHandler("ả")),
                            KeyAltData(KeyDisplay.Char("ã"), CharKeyHandler("ã")),
                            KeyAltData(KeyDisplay.Char("ạ"), CharKeyHandler("ạ")),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Char("s"),
            handler = CharKeyHandler("s"),
        ),
        KeyData(
            display = KeyDisplay.Char("d"),
            handler = CharKeyHandler("d"),
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Char("đ"),
                                CharKeyHandler("đ"),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
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

        // ROW 3
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

        // ROW 4
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
            popup = KeyAltPopup(
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char(";"), CharKeyHandler(";")),
                            KeyAltData(KeyDisplay.Char(":"), CharKeyHandler(":")),
                            KeyAltData(KeyDisplay.Char("_"), CharKeyHandler("_")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Text(","),
                                CharKeyHandler(","),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            handler = SpaceKeyHandler,
        ),
        KeyData(
            display = KeyDisplay.Text("."),
            handler = CharKeyHandler("."),
            popup = KeyAltPopup(
                alignment = PopupAlignment.End,
                rows = listOf(
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("!"), CharKeyHandler("!")),
                            KeyAltData(KeyDisplay.Char("?"), CharKeyHandler("?")),
                            KeyAltData(KeyDisplay.Char("\""), CharKeyHandler("\"")),
                            KeyAltData(KeyDisplay.Char("'"), CharKeyHandler("'")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(KeyDisplay.Char("("), CharKeyHandler("(")),
                            KeyAltData(KeyDisplay.Char(")"), CharKeyHandler(")")),
                        ),
                    ),
                    KeyAltRow(
                        listOf(
                            KeyAltData(
                                KeyDisplay.Text("."),
                                CharKeyHandler("."),
                                isStartingPoint = true,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        KeyData(
            display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
            handler = ReturnKeyHandler,
        ),
    ),
)