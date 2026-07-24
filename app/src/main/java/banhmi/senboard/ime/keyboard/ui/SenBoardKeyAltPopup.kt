package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyAltData
import banhmi.senboard.ime.keyboard.models.KeyAltPopup
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.KeyStyle

@Composable
fun KeyAltContainer(
    isHovered: Boolean,
    keyWidth: Dp,
    keyHeight: Dp,
    style: KeyStyle,
    activeStyle: KeyStyle,
    content: @Composable () -> Unit,
) {
    val finalStyle = if (isHovered) activeStyle else style
    Surface(
        modifier = Modifier.size(keyWidth, keyHeight),
        color = finalStyle.color,
        contentColor = finalStyle.contentColor,
        shape = finalStyle.shape,
    ) {
        content()
    }
}

@Composable
fun KeyAltContent(
    contentPadding: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun SenBoardKeyAltPopup(
    context: SenBoardContext,
    popupData: KeyAltPopup,
    hoveredItem: KeyAltData?,
    keyWidth: Dp,
    keyHeight: Dp,
    style: KeyStyle,
    activeStyle: KeyStyle,
    offset: IntOffset = IntOffset.Zero,
) {
    Popup(
        alignment = Alignment.TopStart,
        offset = offset,
        properties = PopupProperties(clippingEnabled = false),
    ) {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = style.shape,
            ),
        ) {
            val maxColumns = popupData.rows.maxOfOrNull { it.keys.size } ?: 0
            
            for (row in popupData.rows) {
                val rowArrangement = when (popupData.alignment) {
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.Start -> androidx.compose.foundation.layout.Arrangement.Start
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.Center -> androidx.compose.foundation.layout.Arrangement.Center
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.End -> androidx.compose.foundation.layout.Arrangement.End
                }
                
                Row(
                    modifier = Modifier.width(keyWidth * maxColumns),
                    horizontalArrangement = rowArrangement
                ) {
                    for (keyAltData in row.keys) {
                        val isHovered = keyAltData === hoveredItem

                        KeyAltContainer(isHovered, keyWidth, keyHeight, style, activeStyle) {
                            KeyAltContent {
                                val display = when (val value = keyAltData.display) {
                                    is KeyDisplay.Dynamic -> value(context.state)
                                    is KeyDisplay.Static -> value
                                }
                                SenBoardKeyDisplay(
                                    display = display,
                                    style = if (isHovered) activeStyle else style,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun calculatePopupState(
    pointerPosition: Offset,
    popup: KeyAltPopup,
    keyWidthPx: Float,
    keyHeightPx: Float,
): Pair<KeyAltData?, IntOffset> {
    val maxColumns = popup.rows.maxOfOrNull { it.keys.size } ?: 0
    var startCol = 0
    var found = false
    for (row in popup.rows) {
        for ((keyIndex, altData) in row.keys.withIndex()) {
            if (altData.isStartingPoint) {
                val rowOffset = when (popup.alignment) {
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.Start -> 0
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.Center -> (maxColumns - row.keys.size) / 2
                    banhmi.senboard.ime.keyboard.models.PopupAlignment.End -> maxColumns - row.keys.size
                }
                startCol = keyIndex + rowOffset
                found = true
                break
            }
        }
        if (found) break
    }

    val yOffset = -(popup.rows.size * keyHeightPx)
    val xOffset = -startCol * keyWidthPx

    val localX = pointerPosition.x - xOffset
    val localY = pointerPosition.y - yOffset

    val r = kotlin.math.floor(localY / keyHeightPx).toInt()
    val c = kotlin.math.floor(localX / keyWidthPx).toInt()

    val hoveredItem = if (pointerPosition.y > keyHeightPx) {
        null // Cancel if cursor moves below the base key
    } else {
        val clampedRow = r.coerceIn(0, popup.rows.lastIndex)
        val row = popup.rows[clampedRow]
        
        val rowOffset = when (popup.alignment) {
            banhmi.senboard.ime.keyboard.models.PopupAlignment.Start -> 0
            banhmi.senboard.ime.keyboard.models.PopupAlignment.Center -> (maxColumns - row.keys.size) / 2
            banhmi.senboard.ime.keyboard.models.PopupAlignment.End -> maxColumns - row.keys.size
        }
        
        val logicalC = c - rowOffset
        val clampedCol = logicalC.coerceIn(0, row.keys.lastIndex)
        row.keys[clampedCol]
    }

    return Pair(hoveredItem, IntOffset(xOffset.toInt(), yOffset.toInt()))
}
