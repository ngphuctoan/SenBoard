package banhmi.senboard.ime.keyboard.ui.sizing

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SenBoardAutoSizeText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    minFontSize: TextUnit = 12.sp,
    maxFontSize: TextUnit = 32.sp,
    overrideFontSize: TextUnit? = null,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val textMeasurer = rememberTextMeasurer()

        val maxWidthPx = constraints.maxWidth
        val maxHeightPx = constraints.maxHeight

        val bestFontSize = remember(text, maxWidthPx, maxHeightPx, style, overrideFontSize) {
            if (overrideFontSize != null) return@remember overrideFontSize

            textMeasurer.calculateBestFontSize(
                text = text,
                style = style,
                maxWidthPx = maxWidthPx,
                maxHeightPx = maxHeightPx,
                minFontSize = minFontSize,
                maxFontSize = maxFontSize,
            )
        }

        Text(
            text = text,
            style = style.copy(fontSize = bestFontSize),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible,
        )
    }
}
