package banhmi.senboard.app.settings.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SenSettingsSupportingLabels(
    vararg supportingLabels: String, // No need to call listOf!
    supportingDelimiter: String = " \u2022 ",
) {
    // Re-cast supportingLabels to List<String> just to be sure
    Text(supportingLabels.toList().joinToString(supportingDelimiter))
}
