package banhmi.senboard.keyboard.keys

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Forward
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.ui.graphics.vector.ImageVector
import banhmi.senboard.keyboard.ShiftMode

data class IconDisplay(
    val icon: ImageVector,
    val rotation: Float = 0f,
)

sealed interface KeyDisplay {
    data class Text(val label: String) : KeyDisplay
    data class Icon(val icon: ImageVector) : KeyDisplay
    data object None : KeyDisplay
    data class Shift(
        private val icons: HashMap<ShiftMode, IconDisplay> = hashMapOf(
            ShiftMode.Off to IconDisplay(
                Icons.AutoMirrored.Outlined.Forward,
                rotation = -90f,
            ),
            ShiftMode.Shifted to IconDisplay(Icons.Outlined.Upload),
            ShiftMode.CapsLocked to IconDisplay(Icons.Outlined.Upload),
        ),
        private val descriptions: HashMap<ShiftMode, String> = hashMapOf(
            ShiftMode.Off to "Shift",
            ShiftMode.Shifted to "Shift enabled",
            ShiftMode.CapsLocked to "Caps lock enabled",
        ),
    ) : KeyDisplay {
        fun getIcon(mode: ShiftMode) = icons[mode]
        fun getDescription(mode: ShiftMode) = descriptions[mode]
    }
}

data class KeyData(
    val display: KeyDisplay = KeyDisplay.None,
    val supportDisplay: KeyDisplay = KeyDisplay.None,
    val action: KeyAction = KeyAction.None,
)
