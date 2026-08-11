package banhmi.senboard.app.settings.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SenSettingsInputTesterColors(
    val containerColor: Color,
    val outlineColor: Color,
)

object SenSettingsInputTesterDefaults {
    @Composable
    fun colors() = SenSettingsInputTesterColors(
        containerColor = MaterialTheme.colorScheme.surface,
        outlineColor = MaterialTheme.colorScheme.outlineVariant,
    )

    val ContainerShape: Shape = CircleShape

    @Composable
    fun textStyle(): TextStyle = LocalTextStyle.current.copy(fontSize = 20.sp)

    val ContentPadding: PaddingValues = TextFieldDefaults.contentPaddingWithoutLabel(
        top = 22.dp, bottom = 22.dp, start = 20.dp, end = 20.dp,
    )

    val LeadingIconPadding = PaddingValues(start = 12.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenSettingsInputTester(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SenSettingsInputTesterDefaults.textStyle(),
    shape: Shape = SenSettingsInputTesterDefaults.ContainerShape,
    colors: SenSettingsInputTesterColors = SenSettingsInputTesterDefaults.colors(),
    contentPadding: PaddingValues = SenSettingsInputTesterDefaults.ContentPadding,
    leadingIconPadding: PaddingValues = SenSettingsInputTesterDefaults.LeadingIconPadding,
) {
    val context = LocalContext.current
    val imService = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    TextField(
        state = rememberTextFieldState(),
        placeholder = { Text(text = "Nhấp để gõ thử", style = textStyle) },
        leadingIcon = {
            Row(modifier = Modifier.padding(leadingIconPadding)) {
                IconButton(onClick = { imService.showInputMethodPicker() }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Thay đổi phương thức nhập",
                    )
                }
            }
        },
        modifier = modifier.border(width = 1.dp, color = colors.outlineColor, shape = shape),
        textStyle = textStyle,
        contentPadding = contentPadding,
        shape = shape,
        // We don't need other states of the TextInput
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colors.containerColor,
            unfocusedContainerColor = colors.containerColor,
            disabledContainerColor = colors.containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}
