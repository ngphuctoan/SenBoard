package banhmi.senboard.ime.keyboard.ui.indications

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class KeyHighlightIndication(
    private val color: Color,
    private val shape: Shape,
    private val forceState: Boolean? = null,
) : IndicationNodeFactory {
    override fun create(
        interactionSource: InteractionSource,
    ): DelegatableNode {
        return KeyHighlightNode(
            interactionSource = interactionSource,
            color = color,
            shape = shape,
            forceState = forceState,
        )
    }
}

private class KeyHighlightNode(
    private val interactionSource: InteractionSource,
    private val color: Color,
    private val shape: Shape,
    private val forceState: Boolean? = null,
) : Modifier.Node(), DrawModifierNode {
    private val alpha = Animatable(0f)

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> alpha.snapTo(1f)
                    is PressInteraction.Release, is PressInteraction.Cancel -> alpha.animateTo(0f)
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        val outline = shape.createOutline(
            size = size,
            layoutDirection = layoutDirection,
            density = this,
        )

        drawOutline(
            outline = outline,
            color = color,
            alpha = when (forceState) {
                true -> 1f
                false -> 0f
                else -> alpha.value
            },
        )
    }
}