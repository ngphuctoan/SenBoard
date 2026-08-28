package banhmi.senboard.keyboard.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import banhmi.senboard.keyboard.model.SenKeyStyle
import banhmi.senboard.keyboard.model.SenLayoutKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object SenKeyIndicationDefaults {
    @Composable
    fun color(darkTheme: Boolean = isSystemInDarkTheme()) =
        (if (darkTheme) Color.White else Color.Black).copy(alpha = 0.1f)
}

data class SenKeyIndication(
    private val index: Int,
    private val color: Color,
    private val shape: Shape,
    private val overrideState: Boolean? = null,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        SenKeyIndicationNode(
            index = index,
            color = color,
            shape = shape,
            overrideState = overrideState,
            interactionSource = interactionSource,
        )
}

private class SenKeyIndicationNode(
    private val index: Int,
    private val color: Color,
    private val shape: Shape,
    private val overrideState: Boolean?,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode {
    private val alphaProgress = Animatable(0f)

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is SenKeyPressInteraction if interaction.context?.selectedIndex == index -> {
                        alphaProgress.snapTo(1f)
                    }

                    is PressInteraction.Release, is PressInteraction.Cancel -> {
                        alphaProgress.animateTo(0f)
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        val outline = shape.createOutline(size, layoutDirection, this)
        val alpha = alphaProgress.value

        drawOutline(
            outline = outline,
            color = color,
            alpha = when (overrideState) {
                true -> 1f
                false -> 0f
                else -> alpha
            },
        )
    }
}

object SenKeyDefaults {
    internal val ContentAlignment = Alignment.Center
}

@Composable
fun SenKey(
    key: SenLayoutKey,
    style: SenKeyStyle,
    indication: Indication?,
    interactionSource: InteractionSource?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = SenKeyDefaults.ContentAlignment,
        modifier = modifier
            .graphicsLayer {
                shape = style.shape
                shadowElevation = style.shadowElevation
            }
            .fillMaxWidth(key.shapeWidthProportion / key.areaWidthMultiplier)
            .fillMaxHeight()
            .background(
                color = style.colors.color,
                shape = style.shape,
            )
            .clip(shape = style.shape)
            .then(
                if (interactionSource != null) {
                    Modifier.indication(
                        indication = indication,
                        interactionSource = interactionSource,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        CompositionLocalProvider(LocalContentColor provides style.colors.contentColor) {
            content()
        }
    }
}

//object SenKeyDefaults {
//    val AreaHeight: Dp = 64.dp
//
//    val ShapeMaxWidth: Dp = 80.dp
//
//    @Composable
//    internal fun elevation(): Float = with(LocalDensity.current) { 1.dp.toPx() }
//
//    fun maxBoardWidth(layout: SenLayout): Dp =
//        ShapeMaxWidth * layout.rows.minBy { row -> row.totalAreaWidthMultipliers }.totalAreaWidthMultipliers
//}
//
//@Composable
//fun RowScope.SenKey(
//    style: SenKeyStyle,
//    areaWidthMultiplier: Float,
//    shapeWidthProportion: Float,
//    shapeAlignment: Alignment.Horizontal,
//    modifier: Modifier = Modifier,
//    interactionSource: InteractionSource?,
//    indication: Indication?,
//    content: @Composable () -> Unit,
//) {
//    Box(
//        contentAlignment = shapeAlignment + Alignment.CenterVertically,
//        modifier = modifier.weight(areaWidthMultiplier),
//    ) {
//        val elevation = SenKeyDefaults.elevation()
//
//        Box(
//            contentAlignment = Alignment.CenterHorizontally + Alignment.CenterVertically,
//            modifier = Modifier
//                .graphicsLayer {
//                    shape = style.shape
//                    if (style.addShadow) shadowElevation = elevation
//                }
//                .fillMaxWidth(shapeWidthProportion / areaWidthMultiplier)
//                .fillMaxHeight()
//                .background(color = style.colors.color, shape = style.shape)
//                .clip(shape = style.shape)
//                .then(
//                    if (interactionSource != null) {
//                        Modifier.indication(
//                            indication = indication,
//                            interactionSource = interactionSource,
//                        )
//                    } else {
//                        Modifier
//                    },
//                ),
//        ) {
//            CompositionLocalProvider(LocalContentColor provides style.colors.contentColor) {
//                content()
//            }
//        }
//    }
//}
//
//@Suppress("UNUSED")
//@Composable
//fun RowScope.SenKey(
//    style: SenKeyStyle,
//    areaWidthMultiplier: Float,
//    shapeWidthProportion: Float,
//    shapeAlignment: Alignment.Horizontal,
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit,
//) {
//    SenKey(
//        style = style,
//        areaWidthMultiplier = areaWidthMultiplier,
//        shapeWidthProportion = shapeWidthProportion,
//        shapeAlignment = shapeAlignment,
//        modifier = modifier,
//        interactionSource = null,
//        indication = null,
//        content = content,
//    )
//}