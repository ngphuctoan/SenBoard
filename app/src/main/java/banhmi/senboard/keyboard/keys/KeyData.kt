package banhmi.senboard.keyboard.keys

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface KeyDisplay {
    data class Text(val label: String) : KeyDisplay
    data class Icon(val icon: ImageVector) : KeyDisplay
    data object None : KeyDisplay
}

data class KeyData(
    val display: KeyDisplay = KeyDisplay.None,
    val supportDisplay: KeyDisplay = KeyDisplay.None,
)
