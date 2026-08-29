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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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

@Suppress("UNUSED")
@Composable
fun Modifier.minimumAspectRatioPadding(aspectRatio: Float): Modifier {
    var size by remember { mutableStateOf(Size.Zero) }

    val padding = with(LocalDensity.current) {
        PaddingValues(
            vertical = ((size.height - size.width / aspectRatio) / 2) //
                .coerceAtLeast(0f).toDp(),
        )
    }

    return this
        .onGloballyPositioned { coordinates ->
            size = coordinates.size.toSize()
        }
        .padding(padding)
}

object SenKeyDefaults {
    internal val ContentAlignment = Alignment.Center

    val Padding = PaddingValues(3.dp)
}

@Composable
fun SenKey(
    key: SenLayoutKey,
    style: SenKeyStyle,
    indication: Indication?,
    interactionSource: InteractionSource?,
    modifier: Modifier = Modifier,
    padding: PaddingValues = SenKeyDefaults.Padding,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = SenKeyDefaults.ContentAlignment,
        modifier = modifier
            .fillMaxWidth(key.shapeWidthProportion / key.areaWidthMultiplier)
            .fillMaxHeight()
            //.minimumAspectRatioPadding(0.75f * key.shapeWidthProportion)
            .padding(padding)
            .graphicsLayer {
                shape = style.shape
                shadowElevation = style.shadowElevation
            }
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
