package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object SenSettingsTitleDefaults {
    @Composable
    fun style(): TextStyle = MaterialTheme.typography.titleLargeEmphasized
}

@Composable
fun SenSettingsTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = SenSettingsHeaderDefaults.style(),
) {
    Text(
        text = text,
        style = style,
        modifier = modifier,
    )
}