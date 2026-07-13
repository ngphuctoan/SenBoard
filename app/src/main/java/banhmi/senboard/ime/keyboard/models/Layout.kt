package banhmi.senboard.ime.keyboard.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Layout(
    val name: String,
    val keyRows: List<KeyRow>,
    val horizontalPadding: Dp = 4.dp,
    val verticalPadding: Dp = 8.dp,
)
