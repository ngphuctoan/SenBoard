package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.keyboard.model.SenKeyDisplay

@Composable
fun SenDisplay(
    // This should be evaluated before passing!
    display: SenKeyDisplay,
    modifier: Modifier = Modifier,
) {
    when (display) {
        is SenKeyDisplay.Text -> Text(
            text = display.text,
            autoSize = TextAutoSize.StepBased(maxFontSize = 24.sp),
            maxLines = 1,
            modifier = modifier,
        )

        is SenKeyDisplay.Icon -> Icon(
            imageVector = display.icon,
            contentDescription = display.description,
            modifier = modifier
                .sizeIn(maxWidth = 24.dp, maxHeight = 24.dp)
                .fillMaxSize()
                .rotate(display.transforms.rotation),
        )

        else -> {}
    }
}
