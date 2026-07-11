package banhmi.senboard.ime.keyboard.ui.scope

import banhmi.senboard.ime.keyboard.core.SenBoardController

interface SenBoardScope {
    val controller: SenBoardController
}

class SenBoardScopeImpl(
    override val controller: SenBoardController,
) : SenBoardScope
