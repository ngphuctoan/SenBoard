package banhmi.senboard.ime.keyboard.ui.scope

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import banhmi.senboard.ime.keyboard.core.SenBoardController

interface SenBoardScope {
    val controller: SenBoardController
    val widthSizeClass: WindowWidthSizeClass
}

class SenBoardScopeImpl(
    override val controller: SenBoardController,
    override val widthSizeClass: WindowWidthSizeClass,
) : SenBoardScope
