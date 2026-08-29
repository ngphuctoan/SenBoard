@file:Suppress("UNUSED")

package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import banhmi.senboard.keyboard.model.SenAlt
import banhmi.senboard.keyboard.model.SenAltData

data class SenAltPopupColors(
    val containerColor: Color,
    val color: Color,
    val activeColor: Color,
    val contentColor: Color,
    val activeContentColor: Color,
)

object SenAltPopupDefaults {
    @Composable
    fun colors() = SenAltPopupColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        color = Color.Transparent,
        activeColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onSurface,
        activeContentColor = MaterialTheme.colorScheme.onPrimary,
    )

    @Composable
    fun defaultShadowElevation() = with(LocalDensity.current) {
        0.dp.toPx()
    }

    @Composable
    fun shadowElevation(
        keyBackgroundShadowEnabled: Boolean,
    ) = with(LocalDensity.current) {
        if (keyBackgroundShadowEnabled) 2.dp.toPx() else defaultShadowElevation()
    }

    val Shape = ShapeDefaults.Small

    val ItemSizes = DpSize(
        width = 48.dp,
        height = 64.dp,
    )

    internal val ItemContentAlignment = Alignment.Center
}

@Composable
fun SenAltPopup(
    popup: SenAlt.Popup,
    modifier: Modifier = Modifier,
    shape: Shape = SenAltPopupDefaults.Shape,
    itemSizes: DpSize = SenAltPopupDefaults.ItemSizes,
    colors: SenAltPopupColors = SenAltPopupDefaults.colors(),
    shadowElevation: Float = SenBoardScaffoldDefaults.defaultShadowElevation(),
    content: @Composable (SenAltData) -> Unit,
) {
    Column(
        modifier = modifier
            .graphicsLayer {
                this.shape = shape
                this.shadowElevation = shadowElevation
            }
            .background(
                shape = shape,
                color = colors.containerColor,
            ),
    ) {
        popup.rows.forEach { row ->
            Row {
                row.datas.forEach { data ->
                    Box(
                        contentAlignment = SenAltPopupDefaults.ItemContentAlignment,
                        modifier = Modifier
                            .size(itemSizes)
                            .background(
                                shape = shape,
                                color = colors.color,
                            ),
                    ) {
                        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                            content(data)
                        }
                    }
                }
            }
        }
    }
}
