package banhmi.senboard.app.settings.models

import androidx.compose.runtime.Composable

sealed interface SenSettingsItemAction {
    data object None : SenSettingsItemAction
    data class Trailing(val content: @Composable () -> Unit) : SenSettingsItemAction
    data class Bottom(val content: @Composable () -> Unit) : SenSettingsItemAction
}