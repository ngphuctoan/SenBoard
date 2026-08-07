package banhmi.senboard.ime.keyboard.dsl

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.outlined.SpaceBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import banhmi.senboard.ime.keyboard.core.handlers.BackSpaceKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.CharKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.NoOpKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ReturnKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.ShiftKeyHandler
import banhmi.senboard.ime.keyboard.core.handlers.SwitchModeHandler
import banhmi.senboard.ime.keyboard.data.modes.ModeRegistry
import banhmi.senboard.ime.keyboard.models.Key
import banhmi.senboard.ime.keyboard.models.KeyData
import banhmi.senboard.ime.keyboard.models.KeyDisplay
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.KeyMargins
import banhmi.senboard.ime.keyboard.models.KeyRow
import banhmi.senboard.ime.keyboard.models.KeyVariant
import banhmi.senboard.ime.keyboard.models.Layout
import banhmi.senboard.ime.keyboard.models.Mode

@DslMarker
annotation class KeyboardLayoutDslMarker

@DslMarker
annotation class KeyboardModeDslMarker

/**
 * Builds a [Layout] instance using Kotlin DSL.
 */
fun layout(
    name: String,
    builder: LayoutBuilder.() -> Unit
): Layout = LayoutBuilder(name).apply(builder).build()

@KeyboardLayoutDslMarker
class LayoutBuilder(private val name: String) {
    private var marginsProvider: ((screenWidth: Dp) -> KeyMargins)? = null
    private val keyRows = mutableListOf<KeyRow>()

    fun keyMargins(provider: (screenWidth: Dp) -> KeyMargins) {
        this.marginsProvider = provider
    }

    fun row(
        heightWeight: Float = 1f,
        builder: KeyRowBuilder.() -> Unit
    ) {
        val rowBuilder = KeyRowBuilder(heightWeight).apply(builder)
        keyRows.add(rowBuilder.build())
    }

    fun build(): Layout {
        return if (marginsProvider != null) {
            Layout(name = name, keyRows = keyRows, keyMargins = marginsProvider!!)
        } else {
            Layout(name = name, keyRows = keyRows)
        }
    }
}

@KeyboardLayoutDslMarker
class KeyRowBuilder(private val heightWeight: Float) {
    private val keys = mutableListOf<Key>()

    fun key(
        areaWeight: Float = 1f,
        shapeWeight: Float = areaWeight,
        shapeAlignment: Alignment = Alignment.CenterStart,
        variant: KeyVariant = KeyVariant.Neutral,
        forceHighlightState: Boolean? = null,
    ) {
        keys.add(
            Key(
                areaWeight = areaWeight,
                shapeWeight = shapeWeight,
                shapeAlignment = shapeAlignment,
                variant = variant,
                forceHighlightState = forceHighlightState
            )
        )
    }

    fun keys(
        count: Int,
        areaWeight: Float = 1f,
        shapeWeight: Float = areaWeight,
        shapeAlignment: Alignment = Alignment.CenterStart,
        variant: KeyVariant = KeyVariant.Neutral,
        forceHighlightState: Boolean? = null,
    ) {
        repeat(count) {
            key(
                areaWeight = areaWeight,
                shapeWeight = shapeWeight,
                shapeAlignment = shapeAlignment,
                variant = variant,
                forceHighlightState = forceHighlightState
            )
        }
    }

    fun build(): KeyRow = KeyRow(keys = keys, heightWeight = heightWeight)
}

/**
 * Builds a [Mode] instance using Kotlin DSL.
 */
fun mode(
    name: String,
    layout: Layout,
    builder: ModeBuilder.() -> Unit
): Mode = ModeBuilder(name, layout).apply(builder).build()

@KeyboardModeDslMarker
class ModeBuilder(
    private val name: String,
    private val layout: Layout
) {
    private val slots = mutableListOf<KeyData>()

    fun slot(display: KeyDisplay = KeyDisplay.Char(""), handler: KeyHandler = NoOpKeyHandler) {
        slots.add(KeyData(display = display, handler = handler))
    }

    fun charKey(char: String) {
        slots.add(KeyData(display = KeyDisplay.Char(char), handler = CharKeyHandler(char)))
    }

    fun textKey(text: String, charToCommit: String = text) {
        slots.add(KeyData(display = KeyDisplay.Text(text), handler = CharKeyHandler(charToCommit)))
    }

    fun iconKey(
        icon: ImageVector,
        handler: KeyHandler,
        description: String? = null,
        rotation: Float = 0f
    ) {
        slots.add(KeyData(display = KeyDisplay.Icon(icon = icon, description = description, rotation = rotation), handler = handler))
    }

    fun shiftKey() {
        slots.add(KeyData(display = KeyDisplay.Shift, handler = ShiftKeyHandler))
    }

    fun backspaceKey() {
        slots.add(KeyData(display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace), handler = BackSpaceKeyHandler))
    }

    fun returnKey() {
        slots.add(KeyData(display = KeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn), handler = ReturnKeyHandler))
    }

    fun spaceKey(char: String = " ") {
        slots.add(KeyData(display = KeyDisplay.Icon(Icons.Outlined.SpaceBar), handler = CharKeyHandler(char)))
    }

    fun switchModeKey(targetMode: ModeRegistry, displayLabel: String? = null) {
        val label = displayLabel ?: when (targetMode) {
            ModeRegistry.Numerics -> "?123"
            ModeRegistry.Characters -> "ABC"
            ModeRegistry.Symbolics -> "=\\"
        }
        slots.add(KeyData(display = KeyDisplay.Text(label), handler = SwitchModeHandler(targetMode)))
    }

    fun build(): Mode = Mode(name = name, layout = layout, slots = slots)
}
