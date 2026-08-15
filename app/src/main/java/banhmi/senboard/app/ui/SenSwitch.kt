package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object SenSwitchDefaults {
    val UncheckedIcon: @Composable (Modifier) -> Unit = { modifier ->
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = null,
            modifier = modifier,
        )
    }

    val CheckedIcon: @Composable (Modifier) -> Unit = { modifier ->
        Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = null,
            modifier = modifier,
        )
    }
}

@Composable
fun SenSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    uncheckedIcon: @Composable (Modifier) -> Unit = SenSwitchDefaults.UncheckedIcon,
    checkedIcon: @Composable (Modifier) -> Unit = SenSwitchDefaults.CheckedIcon,
) {
    Switch(
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            val iconModifier = Modifier.size(SwitchDefaults.IconSize)
            if (checked) checkedIcon(iconModifier) else uncheckedIcon(iconModifier)
        },
        modifier = modifier,
    )
}
