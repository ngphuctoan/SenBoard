package banhmi.senboard.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.m3RefPaletteCyan30
import banhmi.senboard.ui.theme.m3RefPaletteCyan80
import banhmi.senboard.ui.theme.m3RefPaletteCyan90

data class SenWaferColors(
    val color: Color,
    val contentColor: Color,
)

object SenWaferDefaults {
    val ContentPadding = PaddingValues(4.dp, 2.dp)

    val Shape = ShapeDefaults.Small

    @Composable
    fun textStyle() = MaterialTheme.typography.labelMediumEmphasized

    @Composable
    fun colors(
        darkTheme: Boolean = isSystemInDarkTheme(),
    ) = SenWaferColors(
        color = if (darkTheme) m3RefPaletteCyan80 else m3RefPaletteCyan90,
        contentColor = m3RefPaletteCyan30,
    )
}

// Not to be confused with chips, wafers are like side-by-side badges (e.g. "New")
@Composable
fun SenWafer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SenWaferDefaults.ContentPadding,
    shape: Shape = SenWaferDefaults.Shape,
    textStyle: TextStyle = SenWaferDefaults.textStyle(),
    colors: SenWaferColors = SenWaferDefaults.colors(),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(
                color = colors.color,
                shape = shape,
            )
            .padding(contentPadding),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides textStyle,
            LocalContentColor provides colors.contentColor,
        ) {
            content()
        }
    }
}
