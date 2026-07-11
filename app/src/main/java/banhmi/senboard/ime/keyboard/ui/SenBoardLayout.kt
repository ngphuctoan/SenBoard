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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.KeyHandler
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
private fun ColumnScope.SenBoardRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SenBoardScope.SenBoardLayout(
    mode: Mode,
    onKeyTap: (KeyHandler) -> Unit,
    onKeyDoubleTap: (KeyHandler) -> Unit,
) {
    val layout = mode.layout
    val slots = mode.slots

    val isCapsLocked = controller.state.shiftMode == ShiftMode.CapsLocked

    // Thanks, ChatGPT, for fixing the indexing bug!
    SenBoardColumn {
        var rowOffset = 0

        for (keyRow in layout.keyRows) {
            val currentRowOffset = rowOffset
            rowOffset += keyRow.keys.size

            SenBoardRow {
                for ((keyIndex, key) in keyRow.keys.withIndex()) {
                    val slot = slots[currentRowOffset + keyIndex]
                    val handler = slot.handler

                    val display = when (val value = slot.display) {
                        is KeyDisplay.Shift -> value(controller.state.shiftMode)
                        else -> value
                    }

                    SenBoardKeyArea(
                        key = key,
                        desc = if (display is KeyDisplay.Icon) display.description else null,
                        onTap = { onKeyTap(handler) },
                        onDoubleTap = { onKeyDoubleTap(handler) },
                    ) {
                        SenBoardKeyShape(
                            forceHighlight = slot.handler is ShiftKeyHandler && isCapsLocked,
                        ) {
                            SenBoardKeyContent {
                                val style = key.variant()

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
                        }
                    }
                }
            }
        }
    }
}
