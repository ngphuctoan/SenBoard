package banhmi.senboard.ime.keyboard.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Forward
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface KeyDisplay {
    data class Text(val label: String) : KeyDisplay

    data class Icon(
        val icon: ImageVector,
        val description: String? = null,
        val rotation: Float = 0f,
    ) : KeyDisplay

    data object None : KeyDisplay

    data object Shift : KeyDisplay {
        operator fun invoke(shiftMode: ShiftMode): Icon = when (shiftMode) {
            ShiftMode.Off -> Icon(
                icon = Icons.AutoMirrored.Outlined.Forward,
                rotation = -90f,
                description = "Shift",
            )

            ShiftMode.Shifted -> Icon(
                icon = Icons.Outlined.Upload,
                description = "Shift enabled",
            )

            ShiftMode.CapsLocked -> Icon(
                icon = Icons.Outlined.Upload,
                description = "Caps lock enabled",
            )
        }
    }
}
