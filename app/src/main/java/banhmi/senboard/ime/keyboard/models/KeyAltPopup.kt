package banhmi.senboard.ime.keyboard.models

import androidx.compose.ui.unit.Dp

data class KeyAltPopup(
    val rows: List<KeyAltRow>,
    val keyWidth: Dp? = null,
    val keyHeight: Dp? = null,
    val alignment: PopupAlignment = PopupAlignment.Start,
)

enum class PopupAlignment {
    Start,
    Center,
    End
}
