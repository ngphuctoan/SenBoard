package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.GoogleSansCodeFontFamily

object SenDebugMenuDefaults {
    val VerticalAlignment = Alignment.Bottom

    val VerticalArrangement = Arrangement.spacedBy(0.dp)
}

@Composable
fun SenDebugMenu(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = SenDebugMenuDefaults.VerticalArrangement,
    verticalAlignment: Alignment.Vertical = SenDebugMenuDefaults.VerticalAlignment,
    startContent: @Composable ColumnScope.() -> Unit = {},
    endContent: @Composable ColumnScope.() -> Unit = {},
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.align(verticalAlignment + Alignment.Start),
        ) {
            startContent()
        }
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(verticalAlignment + Alignment.End),
        ) {
            endContent()
        }
    }
}

data class SenDebugMenuTextColors(
    val color: Color,
    val contentColor: Color,
)

object SenDebugTextDefaults {
    val Padding = PaddingValues.Zero

    val Shape = RectangleShape

    @Composable
    fun textStyle() = LocalTextStyle.current.copy(
        fontFamily = GoogleSansCodeFontFamily,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.8f),
            offset = Offset(2f, 2f),
            blurRadius = 2f,
        ),
    )

    val Colors = SenDebugMenuTextColors(
        color = Color.Black.copy(alpha = 0.4f),
        contentColor = Color.White,
    )
}

@Composable
fun SenDebugText(
    text: String,
    modifier: Modifier = Modifier,
    padding: PaddingValues = SenDebugTextDefaults.Padding,
    shape: Shape = SenDebugTextDefaults.Shape,
    textStyle: TextStyle = SenDebugTextDefaults.textStyle(),
    colors: SenDebugMenuTextColors = SenDebugTextDefaults.Colors,
) {
    Text(
        text = text,
        style = textStyle,
        color = colors.contentColor,
        modifier = modifier
            .background(
                color = colors.color,
                shape = shape,
            )
            .padding(padding),
    )
}

object SenDebugSpacerDefaults {
    @Composable
    fun height() = with(LocalDensity.current) {
        SenDebugTextDefaults.textStyle().lineHeight.toDp()
    }
}

@Composable
fun SenDebugSpacer(
    modifier: Modifier = Modifier,
    height: Dp = SenDebugSpacerDefaults.height(),
) {
    Spacer(modifier = modifier.height(height))
}
