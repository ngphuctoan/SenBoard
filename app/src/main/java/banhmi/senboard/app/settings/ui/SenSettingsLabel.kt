package banhmi.senboard.app.settings.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object SenSettingsLabelDefaults {
    @Composable
    fun style(): TextStyle = MaterialTheme.typography.titleMedium.copy(
        lineHeight = 20.sp,
    )
}

@Composable
fun SenSettingsLabel(
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = SenSettingsLabelDefaults.style(),
) {
    Text(
        text = title,
        style = style,
        modifier = modifier,
    )
}
