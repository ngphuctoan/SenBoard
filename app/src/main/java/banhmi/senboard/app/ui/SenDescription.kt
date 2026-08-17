package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object SenDescriptionDefaults {
    val VerticalArrangement = Arrangement.spacedBy(12.dp)

    val ContentPadding = PaddingValues(horizontal = 8.dp)

    @Composable
    fun textStyle() = MaterialTheme.typography.bodyMedium

    @Composable
    fun contentColor() = MaterialTheme.colorScheme.onSurfaceVariant
}

@Composable
fun SenDescription(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = SenDescriptionDefaults.VerticalArrangement,
    contentPadding: PaddingValues = SenDescriptionDefaults.ContentPadding,
    textStyle: TextStyle = SenDescriptionDefaults.textStyle(),
    contentColor: Color = SenDescriptionDefaults.contentColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.merge(textStyle),
        LocalContentColor provides contentColor,
    ) {
        Column(
            verticalArrangement = verticalArrangement,
            modifier = modifier.padding(contentPadding),
        ) {
            content()
        }
    }
}
