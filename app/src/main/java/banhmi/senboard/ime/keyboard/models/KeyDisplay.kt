package banhmi.senboard.ime.keyboard.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Forward
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.ui.graphics.vector.ImageVector
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardState

sealed interface KeyDisplay {
    sealed interface Static : KeyDisplay

    sealed interface Dynamic : KeyDisplay {
        operator fun invoke(state: SenBoardState): Static
    }

    data object None : Static

    data class Text(
        val label: String,
    ) : Static

    data class Icon(
        val icon: ImageVector,
        val description: String? = null,
        val rotation: Float = 0f,
    ) : Static

    data class Char(
        val char: String,
    ) : Dynamic {
        override operator fun invoke(state: SenBoardState): Text = Text(
            if (state.isShifted) char.uppercase() else char.lowercase()
        )
    }

    data object Shift : Dynamic {
        override operator fun invoke(state: SenBoardState): Icon =
            when (state.shiftMode) {
                ShiftMode.Off -> Icon(
                    icon = Icons.AutoMirrored.Outlined.Forward,
                    rotation = -90f,
                    description = "Shift",
                )

                ShiftMode.Automatic, ShiftMode.Manual -> Icon(
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
