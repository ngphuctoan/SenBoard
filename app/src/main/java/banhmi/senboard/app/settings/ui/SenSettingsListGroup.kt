package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import banhmi.senboard.app.settings.ui.scope.SenSettingsListGroupScope
import banhmi.senboard.app.settings.ui.scope.SenSettingsListGroupScopeImpl

@Composable
fun ColumnScope.SenSettingsListGroup(
    isSubMenu: Boolean = false,
    showIcons: Boolean = true,
    content: @Composable SenSettingsListGroupScope.() -> Unit,
) {
    val scope = remember {
        SenSettingsListGroupScopeImpl(
            scope = this,
            isSubMenu = isSubMenu,
            showIcons = showIcons,
        )
    }

    Column(modifier = Modifier.padding(vertical = scope.verticalMargin)) {
        scope.content()
    }
}
