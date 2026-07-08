package banhmi.senboard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import android.view.inputmethod.InputMethodManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.core.content.getSystemService

@Composable
fun KeyboardPreviewTextField(
    label: @Composable () -> Unit,
    tooltipLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    OutlinedTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        trailingIcon = {
            KeyboardPreviewIconButton(
                tooltipLabel,
                icon = { Icon(Icons.Outlined.Keyboard, contentDescription = tooltipLabel) },
                onClick = {
                    val imm = context.getSystemService<InputMethodManager>()
                    imm?.showInputMethodPicker()
                })
        },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun KeyboardPreviewIconButton(
    tooltipLabel: String, icon: @Composable () -> Unit, onClick: () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        tooltip = {
            PlainTooltip(
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Assertive
                    paneTitle = tooltipLabel
                }) {
                Text(tooltipLabel)
            }
        },
        state = rememberTooltipState(),
    ) {
        IconButton(onClick) {
            icon()
        }
    }
}