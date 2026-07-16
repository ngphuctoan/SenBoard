package banhmi.senboard.ime.keyboard.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Layout(
    val name: String,
    val keyRows: List<KeyRow>,
    val keyMargins: (screenWidth: Dp) -> KeyMargins = { screenWidth ->
        when {
            screenWidth > 600.dp -> KeyMargins(4.dp)
            else -> KeyMargins(4.dp, 8.dp)
        }
    },
)
