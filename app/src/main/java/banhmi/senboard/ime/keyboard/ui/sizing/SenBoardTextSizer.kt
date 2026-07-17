package banhmi.senboard.ime.keyboard.ui.sizing

import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun TextMeasurer.calculateBestFontSize(
    text: String,
    style: TextStyle,
    maxWidthPx: Int,
    maxHeightPx: Int,
    minFontSize: TextUnit = 12.sp,
    maxFontSize: TextUnit = 32.sp,
): TextUnit {
    val fontSizeRange = (minFontSize.value.toInt()..maxFontSize.value.toInt()).map { it.sp }
    
    var lowFontSize = 0
    var highFontSize = fontSizeRange.size - 1
    var currentBestFontSize = minFontSize

    while (lowFontSize <= highFontSize) {
        val midFontSize = (lowFontSize + highFontSize) / 2
        val currentFontSize = fontSizeRange[midFontSize]
        val result = measure(
            text = text,
            style = style.copy(fontSize = currentFontSize),
            constraints = Constraints(maxWidth = maxWidthPx, maxHeight = maxHeightPx),
            softWrap = false,
            maxLines = 1,
        )

        if (
            result.size.width <= maxWidthPx &&
            result.size.height <= maxHeightPx &&
            !result.hasVisualOverflow
        ) {
            currentBestFontSize = currentFontSize
            lowFontSize = midFontSize + 1
        } else {
            highFontSize = midFontSize - 1
        }
    }

    return currentBestFontSize
}
