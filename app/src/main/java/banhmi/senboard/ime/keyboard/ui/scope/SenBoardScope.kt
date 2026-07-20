package banhmi.senboard.ime.keyboard.ui.scope

import androidx.compose.ui.unit.Dp
import banhmi.senboard.ime.keyboard.core.SenBoardController

interface SenBoardScope {
    val controller: SenBoardController
    val screenWidth: Dp
    val screenHeight: Dp
}

class SenBoardScopeImpl(
    override val controller: SenBoardController,
    override val screenWidth: Dp,
    override val screenHeight: Dp,
) : SenBoardScope
