package banhmi.senboard.keyboard.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Forward
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.SenImServiceFacade
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.ShiftMode

data class SenKeyColors(
    val color: Color,
    val contentColor: Color,
)

object SenKeyStyleDefaults {
    val Shape: Shape = ShapeDefaults.Medium

    @Composable
    fun textStyle(): TextStyle = MaterialTheme.typography.bodyLargeEmphasized
}

data class SenKeyStyle(
    val shape: Shape,
    val colors: SenKeyColors,
    val addShadow: Boolean,
    val textStyle: TextStyle,
)

typealias SenKeyStyleProvider = @Composable (SenBoardState, SenPreferences) -> SenKeyStyle

val senNeutralKeyStyle: SenKeyStyleProvider = @Composable { _, preferences ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.showKeyBackground) {
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.surfaceContainerHighest
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                }
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        addShadow = preferences.showKeyBackground,
    )
}

val senSecondaryKeyStyle: SenKeyStyleProvider = @Composable { _, preferences ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.showKeyBackground) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        addShadow = preferences.showKeyBackground,
    )
}

val senPrimaryKeyStyle: SenKeyStyleProvider = @Composable { _, _ ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        addShadow = true,
    )
}

val senTertiaryKeyStyle: SenKeyStyleProvider = @Composable { _, preferences ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.showKeyBackground) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        addShadow = preferences.showKeyBackground,
    )
}

data class SenKeyIconTransforms(
    val rotation: Float,
)

object SenKeyDisplayDefaults {
    val EmptyIconDescription: String? = null

    val IconTransforms: SenKeyIconTransforms = SenKeyIconTransforms(rotation = 0f)
}

sealed interface SenKeyDisplay {
    /* Default invocation returns itself, thus a "static" display.
    For dynamic ones, override this to access the state */
    operator fun invoke(state: SenBoardState): SenKeyDisplay = this

    object None : SenKeyDisplay

    // For actual character key, please use Char as it also handles uppercasing
    data class Text(val text: String) : SenKeyDisplay

    data class Icon(
        val icon: ImageVector,
        val description: String? = SenKeyDisplayDefaults.EmptyIconDescription,
        val transforms: SenKeyIconTransforms = SenKeyDisplayDefaults.IconTransforms,
    ) : SenKeyDisplay

    data class Char(val char: kotlin.Char) : SenKeyDisplay {
        override fun invoke(state: SenBoardState): SenKeyDisplay =
            Text(char.toString().run { if (state.isShifted) uppercase() else lowercase() })
    }

    object ShiftIcon : SenKeyDisplay {
        override fun invoke(state: SenBoardState): SenKeyDisplay = when (state.shiftMode) {
            ShiftMode.Off -> Icon(
                icon = Icons.AutoMirrored.Outlined.Forward,
                transforms = SenKeyIconTransforms(rotation = -90f),
            )

            ShiftMode.Shifted -> Icon(Icons.Outlined.Upload)

            ShiftMode.CapsLocked -> Icon(Icons.Filled.Upload)
        }
    }
}

// By default, these handlers do nothing, so that implementations don't need to override all of them
interface SenKeyHandler {
    fun handleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) {
    }

    fun handleDoubleTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) {
    }

    fun handleLongTap(
        state: SenBoardState,
        onSetState: (SenBoardState) -> Unit,
        preferences: SenPreferences,
        imService: SenImServiceFacade,
    ) {
    }
}

data class SenKeyData(
    val styleProvider: SenKeyStyleProvider,
    val display: SenKeyDisplay,
    val handler: SenKeyHandler,
)
