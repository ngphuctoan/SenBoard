package banhmi.senboard.ime.keyboard.ui.scope

import androidx.compose.foundation.interaction.MutableInteractionSource
import banhmi.senboard.ime.keyboard.models.Key

interface SenBoardKeyScope {
    val key: Key
    val onTap: () -> Unit
    val onDoubleTap: () -> Unit
    val source: MutableInteractionSource
}

class SenBoardKeyScopeImpl(
    override val key: Key,
    override val onTap: () -> Unit,
    override val onDoubleTap: () -> Unit,
    override val source: MutableInteractionSource,
) : SenBoardKeyScope
