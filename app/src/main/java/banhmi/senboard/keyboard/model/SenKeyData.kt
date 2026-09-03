package banhmi.senboard.keyboard.model

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.state.SenBoardState
import banhmi.senboard.keyboard.state.ShiftMode

data class SenKeyColors(
    val color: Color,
    val contentColor: Color,
    val supportingContentColor: Color,
)

object SenKeyStyleDefaults {
    val Shape = ShapeDefaults.Medium

    @Composable
    fun textStyle() = MaterialTheme.typography.bodyLargeEmphasized

    @Composable
    fun defaultShadowElevation() = with(LocalDensity.current) {
        0.dp.toPx()
    }

    @Composable
    fun shadowElevation(
        keyBackgroundEnabled: Boolean,
        keyBackgroundShadowEnabled: Boolean,
    ) = with(LocalDensity.current) {
        if (keyBackgroundEnabled && keyBackgroundShadowEnabled) {
            1.dp.toPx()
        } else {
            defaultShadowElevation()
        }
    }
}

data class SenKeyStyle(
    val shape: Shape,
    val colors: SenKeyColors,
    val shadowElevation: Float,
    val textStyle: TextStyle,
)

typealias SenKeyStyleProvider = @Composable (SenBoardState, SenPreferences, Boolean) -> SenKeyStyle

val senNeutralKeyStyle: SenKeyStyleProvider = @Composable { _, preferences, darkTheme ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.keyBackgroundEnabled) {
                if (darkTheme) {
                    MaterialTheme.colorScheme.surfaceContainerHighest
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                }
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onSurface,
            supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        shadowElevation = SenKeyStyleDefaults.shadowElevation(
            preferences.keyBackgroundEnabled,
            preferences.keyBackgroundShadowEnabled,
        ),
    )
}

@Suppress("UNUSED")
val senPrimaryContainerKeyStyle: SenKeyStyleProvider = @Composable { _, preferences, _ ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.keyBackgroundEnabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            supportingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        shadowElevation = SenKeyStyleDefaults.shadowElevation(
            preferences.keyBackgroundEnabled,
            preferences.keyBackgroundShadowEnabled,
        ),
    )
}

val senSecondaryContainerKeyStyle: SenKeyStyleProvider = @Composable { _, preferences, _ ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.keyBackgroundEnabled) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        shadowElevation = SenKeyStyleDefaults.shadowElevation(
            preferences.keyBackgroundEnabled,
            preferences.keyBackgroundShadowEnabled,
        ),
    )
}

val senPrimaryKeyStyle: SenKeyStyleProvider = @Composable { _, preferences, _ ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            supportingContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        shadowElevation = SenKeyStyleDefaults.shadowElevation(
            true, // Primary always has a background
            preferences.keyBackgroundShadowEnabled,
        ),
    )
}

val senTertiaryContainerKeyStyle: SenKeyStyleProvider = @Composable { _, preferences, _ ->
    SenKeyStyle(
        shape = SenKeyStyleDefaults.Shape,
        colors = SenKeyColors(
            color = if (preferences.keyBackgroundEnabled) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                Color.Transparent
            },
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            supportingContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        textStyle = SenKeyStyleDefaults.textStyle(),
        shadowElevation = SenKeyStyleDefaults.shadowElevation(
            preferences.keyBackgroundEnabled,
            preferences.keyBackgroundShadowEnabled,
        ),
    )
}

data class SenKeyIconTransforms(
    val rotation: Float,
)

object SenKeyDisplayDefaults {
    val EmptyIconDescription: String? = null

    val IconTransforms = SenKeyIconTransforms(rotation = 0f)
}

sealed interface SenKeyDisplay {
    /* Default invocation returns itself, thus a "static" display.
    For dynamic ones, override this to access the state */
    operator fun invoke(
        state: SenBoardState,
    ): SenKeyDisplay = this

    object None : SenKeyDisplay

    // For actual character key, please use Char as it also handles uppercasing
    data class Text(
        val text: String,
    ) : SenKeyDisplay

    data class Number(
        val number: Int,
        val t9Chars: String,
    ) : SenKeyDisplay

    data class Icon(
        val icon: ImageVector,
        val description: String? = SenKeyDisplayDefaults.EmptyIconDescription,
        val transforms: SenKeyIconTransforms = SenKeyDisplayDefaults.IconTransforms,
    ) : SenKeyDisplay

    data class Char(
        val char: kotlin.Char,
    ) : SenKeyDisplay {
        override fun invoke(
            state: SenBoardState,
        ) = Text(
            char.toString().run {
                if (state.isShifted) uppercase() else lowercase()
            },
        )
    }

    object ShiftIcon : SenKeyDisplay {
        override fun invoke(
            state: SenBoardState,
        ) = when (state.shiftMode) {
            ShiftMode.Off -> Icon(
                icon = Icons.AutoMirrored.Outlined.Forward,
                transforms = SenKeyIconTransforms(rotation = -90f),
            )

            ShiftMode.Shifted -> Icon(Icons.Outlined.Upload)

            ShiftMode.CapsLocked -> Icon(Icons.Filled.Upload)
        }
    }
}

data class SenKeyData(
    val styleProvider: SenKeyStyleProvider,
    val display: SenKeyDisplay,
    val handler: SenKeyHandler,
    val altProvider: SenAltProvider,
)
