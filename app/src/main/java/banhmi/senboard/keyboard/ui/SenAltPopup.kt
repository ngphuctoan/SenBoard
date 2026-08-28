package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import banhmi.senboard.keyboard.model.SenAlt
import banhmi.senboard.keyboard.model.SenAltData
import banhmi.senboard.utils.toIntOffset

data class SenAltPopupColors(
    val containerColor: Color,
    val color: Color,
    val activeColor: Color,
    val contentColor: Color,
    val activeContentColor: Color,
)

object SenAltPopupDefaults {
    internal val PopupAlignment = Alignment.TopStart

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
}

@Composable
fun SenAltPopup(
    popup: SenAlt.Popup,
    offset: Offset,
    modifier: Modifier = Modifier,
    colors: SenAltPopupColors = SenAltPopupDefaults.colors(),
    shadowElevation: Float = SenBoardScaffoldDefaults.defaultShadowElevation(),
    content: @Composable (SenAltData) -> Unit,
) {
    var popupHeight by remember { mutableIntStateOf(0) }

    Popup(
        alignment = SenAltPopupDefaults.PopupAlignment,
        offset = IntOffset(0, -popupHeight) + offset.toIntOffset(),
    ) {
        Column(
            modifier = modifier
                .graphicsLayer {
                    this.shadowElevation = shadowElevation
                }
                .background(color = colors.containerColor)
                .onGloballyPositioned { coordinates ->
                    popupHeight = coordinates.size.height
                },
        ) {
            popup.rows.forEach { row ->
                Row {
                    row.datas.forEach { data ->
                        Box(modifier = Modifier.background(color = colors.color)) {
                            CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                                content(data)
                            }
                        }
                    }
                }
            }
        }
    }
}
