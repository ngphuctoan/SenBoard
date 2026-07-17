package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.KeyStyle
import banhmi.senboard.ime.keyboard.models.KeyVariant
import banhmi.senboard.ime.keyboard.models.Layout
import banhmi.senboard.ime.keyboard.models.Mode
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.ime.keyboard.models.invoke
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope
import banhmi.senboard.ime.keyboard.ui.sizing.SenBoardAutoSizeText
import banhmi.senboard.ime.keyboard.ui.sizing.calculateBestFontSize
import androidx.compose.ui.platform.LocalDensity

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
private fun calculateReferenceFontSize(
    screenWidth: Dp,
    screenHeight: Dp,
    layout: Layout,
    style: KeyStyle,
): TextUnit {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val rowCount = layout.keyRows.size
    // I probably don't have to worry too much, common layouts have 10-11 keys per row
    // Also we assume getting the row size never fails so a fallback is pretty unlikely
    val maxKeyCountPerRow = layout.keyRows.maxOfOrNull { it.keys.size } ?: 10

    val standardKeyWidth = screenWidth / maxKeyCountPerRow
    val standardKeyHeight = screenHeight / rowCount

    // Account for padding
    val availableWidth = (standardKeyWidth - style.contentPadding * 2).coerceAtLeast(0.dp)
    val availableHeight = (standardKeyHeight - style.contentPadding * 2).coerceAtLeast(0.dp)

    val maxWidthPx = with(density) { availableWidth.toPx() }.toInt()
    val maxHeightPx = with(density) { availableHeight.toPx() }.toInt()

    return textMeasurer.calculateBestFontSize(
        text = "W",
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = style.fontWeight,
        ),
        maxWidthPx = maxWidthPx,
        maxHeightPx = maxHeightPx,
        minFontSize = style.minFontSize,
        maxFontSize = style.maxFontSize,
    )
}

@Composable
private fun SenBoardKeyDisplay(
    display: KeyDisplay.Static,
    style: KeyStyle,
    overrideFontSize: TextUnit? = null,
) {
    when (display) {
        is KeyDisplay.Text -> SenBoardAutoSizeText(
            text = display.label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = style.fontWeight,
            ),
            minFontSize = style.minFontSize,
            maxFontSize = style.maxFontSize,
            overrideFontSize = overrideFontSize,
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
    val layout = mode.layout
    val slots = mode.slots

    val isCapsLocked = controller.state.shiftMode == ShiftMode.CapsLocked

    val referenceFontSize = calculateReferenceFontSize(
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        layout = layout,
        style = KeyVariant.Neutral(),
    )

    // Thanks to ChatGPT for all of this, and the indexing bugfix!
    // Set key to layout name to force layout subtree to be recreated
    key(layout.name) {
        SenBoardColumn {
            var rowOffset = 0

            for (keyRow in layout.keyRows) {
                val currentRowOffset = rowOffset
                rowOffset += keyRow.keys.size

                SenBoardRow(heightWeight = keyRow.heightWeight) {
                    for ((keyIndex, key) in keyRow.keys.withIndex()) {
                        val slot = slots[currentRowOffset + keyIndex]
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
                                margin = layout.keyMargins(screenWidth).getPaddingValues(),
                                forceHighlight = slot.handler is ShiftKeyHandler && isCapsLocked,
                            ) {
                                val style = key.variant()
                                SenBoardKeyContent(contentPadding = style.contentPadding) {
                                    val overrideFontSize = if (
                                        style.useReferenceFontSize &&
                                        display is KeyDisplay.Text &&
                                        display.label.length == 1
                                    ) {
                                        referenceFontSize
                                    } else {
                                        null
                                    }
                                    SenBoardKeyDisplay(display, style, overrideFontSize)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
