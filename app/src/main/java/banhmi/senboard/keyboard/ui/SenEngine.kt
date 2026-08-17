package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.engine.VietnameseEngineType

object SenEngineIconDefaults {
    val IconSize: Dp = 24.dp

    val IconShape: Shape = ShapeDefaults.ExtraSmall

    @Composable
    fun textStyle(): TextStyle =
        MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

    internal val BorderWidth: Dp = 2.dp

    internal val ContentAlignment: Alignment = Alignment.Center

    internal val TextMaxSize: TextUnit = 16.sp
}

@Composable
fun SenEngineIcon(
    engineType: VietnameseEngineType,
    modifier: Modifier = Modifier,
    iconSize: Dp = SenEngineIconDefaults.IconSize,
    iconShape: Shape = SenEngineIconDefaults.IconShape,
    textStyle: TextStyle = SenEngineIconDefaults.textStyle(),
) {
    Box(
        modifier = modifier
            .border(
                width = SenEngineIconDefaults.BorderWidth,
                color = LocalContentColor.current,
                shape = iconShape,
            )
            .size(iconSize),
        contentAlignment = SenEngineIconDefaults.ContentAlignment,
    ) {
        Text(
            text = engineType.shortName,
            style = textStyle,
            autoSize = TextAutoSize.StepBased(maxFontSize = SenEngineIconDefaults.TextMaxSize),
            maxLines = 1,
        )
    }

}

@Composable
fun SenEngineSwitcher(
    engineType: VietnameseEngineType,
    onEngineSwitch: (VietnameseEngineType) -> Unit,
    content: @Composable (VietnameseEngineType) -> Unit,
) {
    val engineTypeIndex = VietnameseEngineType.entries.indexOf(engineType)
    val nextEngineTypeIndex = (engineTypeIndex + 1) % VietnameseEngineType.entries.size

    IconButton(onClick = { onEngineSwitch(VietnameseEngineType.entries[nextEngineTypeIndex]) }) {
        content(engineType)
    }
}
