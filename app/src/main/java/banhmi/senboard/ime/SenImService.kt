package banhmi.senboard.ime

import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.SpaceBar
import androidx.compose.ui.platform.ComposeView
import banhmi.senboard.keyboard.SenBoard
import banhmi.senboard.keyboard.Toolbar
import banhmi.senboard.keyboard.keys.KeyData
import banhmi.senboard.keyboard.keys.KeyDisplay
import banhmi.senboard.keyboard.layouts.Generic105Layout
import banhmi.senboard.ui.theme.SenBoardTheme

class SenImService : LifecycleImService() {
    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                SenBoardTheme {
                    SenBoard {
                        Column {
                            Toolbar { }
                            Generic105Layout(
                                slots = listOf(
                                    // Row 1
                                    KeyData(
                                        display = KeyDisplay.Text("q"),
                                        supportDisplay = KeyDisplay.Text("1"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("w"),
                                        supportDisplay = KeyDisplay.Text("2"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("e"),
                                        supportDisplay = KeyDisplay.Text("3"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("r"),
                                        supportDisplay = KeyDisplay.Text("4"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("t"),
                                        supportDisplay = KeyDisplay.Text("5"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("y"),
                                        supportDisplay = KeyDisplay.Text("6"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("u"),
                                        supportDisplay = KeyDisplay.Text("7"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("i"),
                                        supportDisplay = KeyDisplay.Text("8"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("o"),
                                        supportDisplay = KeyDisplay.Text("9"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("p"),
                                        supportDisplay = KeyDisplay.Text("0"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
                                    ),

                                    // Row 2
                                    KeyData(
                                        display = KeyDisplay.Text("a"),
                                        supportDisplay = KeyDisplay.Text("@"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("s"),
                                        supportDisplay = KeyDisplay.Text("#"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("d"),
                                        supportDisplay = KeyDisplay.Text("$"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("f"),
                                        supportDisplay = KeyDisplay.Text("%"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("g"),
                                        supportDisplay = KeyDisplay.Text("&"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("h"),
                                        supportDisplay = KeyDisplay.Text("-"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("j"),
                                        supportDisplay = KeyDisplay.Text("+"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("k"),
                                        supportDisplay = KeyDisplay.Text("("),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("l"),
                                        supportDisplay = KeyDisplay.Text(")"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
                                    ),

                                    // Row 3
                                    KeyData(
                                        display = KeyDisplay.Icon(Icons.Outlined.KeyboardArrowUp),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("z"),
                                        supportDisplay = KeyDisplay.Text("*"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("x"),
                                        supportDisplay = KeyDisplay.Text("\""),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("c"),
                                        supportDisplay = KeyDisplay.Text("'"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("v"),
                                        supportDisplay = KeyDisplay.Text(":"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("b"),
                                        supportDisplay = KeyDisplay.Text(";"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("n"),
                                        supportDisplay = KeyDisplay.Text("!"),
                                    ),
                                    KeyData(
                                        display = KeyDisplay.Text("m"),
                                        supportDisplay = KeyDisplay.Text("?"),
                                    ),
                                    KeyData(display = KeyDisplay.Text("!")),
                                    KeyData(display = KeyDisplay.Text("?")),
                                    KeyData(
                                        display = KeyDisplay.Icon(Icons.Outlined.KeyboardArrowUp),
                                    ),

                                    // Row 4
                                    KeyData(display = KeyDisplay.Text("?123")),
                                    KeyData(display = KeyDisplay.Text(",")),
                                    KeyData(),
                                    KeyData(display = KeyDisplay.Text(".")),
                                    KeyData(display = KeyDisplay.Icon(Icons.Outlined.InsertEmoticon)),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}