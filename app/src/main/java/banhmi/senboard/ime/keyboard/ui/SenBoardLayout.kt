package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.runtime.remember
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.KeyStyle
import banhmi.senboard.ime.keyboard.models.Mode
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.ime.keyboard.models.invoke
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope

@Composable
private fun SenBoardColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        content()
    }
}

@Composable
private fun ColumnScope.SenBoardRow(
    heightWeight: Float = 1f,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .weight(heightWeight)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
private fun SenBoardKeyDisplay(
    display: KeyDisplay.Static,
    style: KeyStyle,
) {
    when (display) {
        is KeyDisplay.Text -> Text(
            text = display.label,
            style = style.typography,
        )

        is KeyDisplay.Icon -> Icon(
            display.icon,
            contentDescription = null,
            modifier = Modifier
                .size(style.iconSize)
                .rotate(display.rotation),
        )

        else -> {}
    }
}

@Composable
fun SenBoardScope.SenBoardLayout(
    mode: Mode,
    onKeyTap: (KeyHandler) -> Unit,
    onKeyDoubleTap: (KeyHandler) -> Unit,
) {
    val prefs = banhmi.senboard.app.settings.rememberPreferences()
    val showNumberRow = prefs.showNumberRow && mode.name == "characters"

    val dynamicKeyRows = remember(mode, showNumberRow) {
        if (showNumberRow) {
            val numberRow = banhmi.senboard.ime.keyboard.models.KeyRow(
                keys = List(10) { banhmi.senboard.ime.keyboard.models.Key(variant = banhmi.senboard.ime.keyboard.models.KeyVariant.Secondary) },
                heightWeight = 0.8f
            )
            listOf(numberRow) + mode.layout.keyRows
        } else {
            mode.layout.keyRows
        }
    }

    val dynamicSlots = remember(mode, showNumberRow) {
        if (showNumberRow) {
            val numberSlots = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").map { num ->
                banhmi.senboard.ime.keyboard.models.KeyData(
                    display = KeyDisplay.Text(num),
                    handler = CharKeyHandler(num)
                )
            }
            numberSlots + mode.slots
        } else {
            mode.slots
        }
    }

    val isCapsLocked = controller.state.shiftMode == ShiftMode.CapsLocked

    // Set key to layout name & showNumberRow to force layout subtree to be recreated when preferences change
    key("${mode.layout.name}_$showNumberRow") {
        SenBoardColumn {
            var slotIndex = 0

            for (keyRow in dynamicKeyRows) {
                SenBoardRow(heightWeight = keyRow.heightWeight) {
                    for (key in keyRow.keys) {
                        val slot = dynamicSlots.getOrNull(slotIndex)
                        slotIndex++

                        if (slot != null) {
                            val handler = slot.handler
                            val display = when (val value = slot.display) {
                                is KeyDisplay.Dynamic -> value(controller.state)
                                is KeyDisplay.Static -> value
                            }

                            SenBoardKeyArea(
                                key = key,
                                desc = if (display is KeyDisplay.Icon) display.description else null,
                                onTap = { onKeyTap(handler) },
                                onDoubleTap = { onKeyDoubleTap(handler) },
                            ) {
                                SenBoardKeyShape(
                                    margin = mode.layout.keyMargins(screenWidth).getPaddingValues(),
                                    forceHighlight = slot.handler is ShiftKeyHandler && isCapsLocked,
                                    isSpacer = display is KeyDisplay.None,
                                ) {
                                    SenBoardKeyContent {
                                        val style = key.variant()
                                        SenBoardKeyDisplay(display, style)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
