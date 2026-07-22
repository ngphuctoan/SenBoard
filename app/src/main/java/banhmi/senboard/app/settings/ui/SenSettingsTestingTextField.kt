package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.KeyboardAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.SenSettingsTestingTextField(
    radius: Dp = 24.dp,
    margin: PaddingValues = PaddingValues(12.dp),
    padding: PaddingValues = PaddingValues(horizontal = 16.dp),
    onInputMethodChange: () -> Unit,
) {
    val state = rememberTextFieldState()

    val containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceContainerHighest
    else MaterialTheme.colorScheme.surfaceContainerLowest

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomEnd)
            .padding(margin)
            .imePadding()
            .height(radius * 2),
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = { Text("Nhấp để gõ thử tại đây") },
        trailingIcon = {
            Row {
                if (state.text.isNotBlank()) {
                    IconButton(onClick = { state.clearText() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.Backspace,
                            contentDescription = "Đổi phương thức nhập",
                        )
                    }
                }
                IconButton(onClick = onInputMethodChange) {
                    Icon(
                        Icons.Rounded.KeyboardAlt,
                        contentDescription = "Đổi phương thức nhập",
                    )
                }
            }
        },
        shape = RoundedCornerShape(radius),
        contentPadding = padding,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}
