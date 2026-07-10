package banhmi.senboard.ime

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.ComposeView
import banhmi.senboard.keyboard.SenBoard
import banhmi.senboard.keyboard.SenBoardContext
import banhmi.senboard.keyboard.SenBoardManager
import banhmi.senboard.keyboard.SenBoardState
import banhmi.senboard.keyboard.Toolbar
import banhmi.senboard.keyboard.keys.KeyAction
import banhmi.senboard.keyboard.keys.KeyData
import banhmi.senboard.keyboard.keys.KeyDisplay
import banhmi.senboard.keyboard.layouts.Generic105Layout
import banhmi.senboard.ui.theme.SenBoardTheme

class SenImService : LifecycleImService() {
    val state = SenBoardState()

    val manager by lazy {
        SenBoardManager(
            context = SenBoardContext(
                im = this,
                state = state,
            ),
        )
    }

    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                SenBoardTheme {
                    val color = MaterialTheme.colorScheme.surfaceContainer
                    val isDark = isSystemInDarkTheme()

                    SideEffect {
                        this@SenImService.setNavBarColor(
                            color = color,
                            lightIcons = isDark,
                        )
                    }

                    SenBoard {
                        Column {
                            Toolbar { }
                            Generic105Layout(
                                manager = manager,
                                slots = listOf(
                                    // Row 1
                                    KeyData(
                                        display = KeyDisplay.Text("q"),
                                        supportDisplay = KeyDisplay.Text("1"),
                                        action = KeyAction.Character("q"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("w"),
                                        supportDisplay = KeyDisplay.Text("2"),
                                        action = KeyAction.Character("w"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("e"),
                                        supportDisplay = KeyDisplay.Text("3"),
                                        action = KeyAction.Character("e"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("r"),
                                        supportDisplay = KeyDisplay.Text("4"),
                                        action = KeyAction.Character("r"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("t"),
                                        supportDisplay = KeyDisplay.Text("5"),
                                        action = KeyAction.Character("t"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("y"),
                                        supportDisplay = KeyDisplay.Text("6"),
                                        action = KeyAction.Character("y"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("u"),
                                        supportDisplay = KeyDisplay.Text("7"),
                                        action = KeyAction.Character("u"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("i"),
                                        supportDisplay = KeyDisplay.Text("8"),
                                        action = KeyAction.Character("i"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("o"),
                                        supportDisplay = KeyDisplay.Text("9"),
                                        action = KeyAction.Character("o"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("p"),
                                        supportDisplay = KeyDisplay.Text("0"),
                                        action = KeyAction.Character("p"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(
                                            Icons.AutoMirrored.Outlined.Backspace,
                                        ),
                                        action = KeyAction.Backspace,
                                    ),

                                    // Row 2
                                    KeyData(
                                        display = KeyDisplay.Text("a"),
                                        supportDisplay = KeyDisplay.Text("@"),
                                        action = KeyAction.Character("a"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("s"),
                                        supportDisplay = KeyDisplay.Text("#"),
                                        action = KeyAction.Character("s"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("d"),
                                        supportDisplay = KeyDisplay.Text("$"),
                                        action = KeyAction.Character("d"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("f"),
                                        supportDisplay = KeyDisplay.Text("%"),
                                        action = KeyAction.Character("f"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("g"),
                                        supportDisplay = KeyDisplay.Text("&"),
                                        action = KeyAction.Character("g"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("h"),
                                        supportDisplay = KeyDisplay.Text("-"),
                                        action = KeyAction.Character("h"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("j"),
                                        supportDisplay = KeyDisplay.Text("+"),
                                        action = KeyAction.Character("j"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("k"),
                                        supportDisplay = KeyDisplay.Text("("),
                                        action = KeyAction.Character("k"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("l"),
                                        supportDisplay = KeyDisplay.Text(")"),
                                        action = KeyAction.Character("l"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(
                                            Icons.AutoMirrored.Outlined.KeyboardReturn,
                                        ),
                                        action = KeyAction.Enter,
                                    ),

                                    // Row 3
                                    KeyData(
                                        display = KeyDisplay.Icon(
                                            Icons.Outlined.KeyboardArrowUp,
                                        ),
                                        action = KeyAction.Shift,
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("z"),
                                        supportDisplay = KeyDisplay.Text("*"),
                                        action = KeyAction.Character("z"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("x"),
                                        supportDisplay = KeyDisplay.Text("\""),
                                        action = KeyAction.Character("x"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("c"),
                                        supportDisplay = KeyDisplay.Text("'"),
                                        action = KeyAction.Character("c"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("v"),
                                        supportDisplay = KeyDisplay.Text(":"),
                                        action = KeyAction.Character("v"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("b"),
                                        supportDisplay = KeyDisplay.Text(";"),
                                        action = KeyAction.Character("b"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("n"),
                                        supportDisplay = KeyDisplay.Text("!"),
                                        action = KeyAction.Character("n"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("m"),
                                        supportDisplay = KeyDisplay.Text("?"),
                                        action = KeyAction.Character("m"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("!"),
                                        action = KeyAction.Character("!"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("?"),
                                        action = KeyAction.Character("?"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(
                                            Icons.Outlined.KeyboardArrowUp,
                                        ),
                                        action = KeyAction.Shift,
                                    ),

                                    // Row 4
                                    KeyData(
                                        display = KeyDisplay.Text("?123"),
                                        action = KeyAction.Custom {
                                            // TODO: switch to symbols layout
                                        },
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text(","),
                                        action = KeyAction.Character(","),
                                    ),
                                    KeyData(
                                        action = KeyAction.Space,
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("."),
                                        action = KeyAction.Character("."),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(
                                            Icons.Outlined.InsertEmoticon,
                                        ),
                                        action = KeyAction.Custom {
                                            // TODO: switch to emoji layout
                                        },
                                    ),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}