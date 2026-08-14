package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.minus
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlin.math.pow

object SenHeaderDefaults {
    @Composable
    fun style(): TextStyle = MaterialTheme.typography.bodyMedium

    @Composable
    fun color(): Color = MaterialTheme.colorScheme.primary

    val Padding: PaddingValues =
        PaddingValues(12.dp, 8.dp) - PaddingValues(vertical = ListItemDefaults.SegmentedGap)
}

@Composable
fun SenHeader(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = SenHeaderDefaults.style(),
    color: Color = SenHeaderDefaults.color(),
    padding: PaddingValues = SenHeaderDefaults.Padding,
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier.padding(padding),
    )
}
