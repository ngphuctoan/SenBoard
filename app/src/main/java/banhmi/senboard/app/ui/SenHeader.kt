package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object SenHeaderDefaults {
    @Composable
    fun style(): TextStyle = MaterialTheme.typography.labelLarge

    @Composable
    fun color(): Color = MaterialTheme.colorScheme.primary

    val Padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
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
