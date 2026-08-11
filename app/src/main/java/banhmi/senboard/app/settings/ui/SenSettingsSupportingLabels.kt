package banhmi.senboard.app.settings.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object SenSettingsSupportingLabelsDefaults {
    @Composable
    fun style(): TextStyle = MaterialTheme.typography.bodyMedium
}

@Composable
fun SenSettingsSupportingLabels(
    vararg supportingLabels: String, // No need to call listOf!
    modifier: Modifier = Modifier,
    supportingDelimiter: String = ", ",
    // supportingDelimiter: String = " \u2022 ",
    style: TextStyle = SenSettingsSupportingLabelsDefaults.style(),
) {
    // Re-cast supportingLabels to List<String> just to be sure
    Text(
        text = supportingLabels.toList().joinToString(supportingDelimiter),
        style = style,
        modifier = modifier,
    )
}
