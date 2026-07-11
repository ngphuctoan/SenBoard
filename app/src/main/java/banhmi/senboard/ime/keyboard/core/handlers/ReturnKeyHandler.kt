package banhmi.senboard.ime.keyboard.core.handlers

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler

data object ReturnKeyHandler : KeyHandler {
    override fun handle(context: SenBoardContext) {
        context.getEditor()?.sendKeyEvent(
            android.view.KeyEvent(
                android.view.KeyEvent.ACTION_DOWN,
                android.view.KeyEvent.KEYCODE_ENTER,
            )
        )
    }
}
