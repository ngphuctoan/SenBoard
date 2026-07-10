package banhmi.senboard.keyboard

import banhmi.senboard.keyboard.keys.KeyAction

class SenBoardManager(val context: SenBoardContext) {
    fun handle(action: KeyAction) {
        when (action) {
            is KeyAction.Character -> {
                commitCharacter(action.raw)
            }

            KeyAction.Space -> {
                context.im.currentInputConnection.commitText(" ", 1)
            }

            KeyAction.Backspace -> {
                val selectedText = context.im.currentInputConnection.getSelectedText(0)
                if (!selectedText.isNullOrEmpty()) {
                    context.im.currentInputConnection.commitText("", 1)
                } else {
                    context.im.currentInputConnection.deleteSurroundingText(1, 0)
                }
            }

            KeyAction.Shift -> {
                context.state.shiftMode =
                    when (context.state.shiftMode) {
                        ShiftMode.Off -> ShiftMode.Shifted
                        ShiftMode.Shifted, ShiftMode.CapsLocked -> ShiftMode.Off
                    }
            }

            KeyAction.Enter -> {
                context.im.currentInputConnection.sendKeyEvent(
                    android.view.KeyEvent(
                        android.view.KeyEvent.ACTION_DOWN,
                        android.view.KeyEvent.KEYCODE_ENTER,
                    )
                )
            }

            is KeyAction.Custom -> {
                action.handler(context)
            }

            KeyAction.None -> {}
        }
    }

    fun handleDoubleTap(action: KeyAction) {
        when (action) {
            KeyAction.Shift -> {
                context.state.shiftMode = ShiftMode.CapsLocked
            }

            else -> handle(action)
        }
    }

    private fun commitCharacter(raw: String) {
        val text = if (context.state.isShifted) raw.uppercase() else raw.lowercase()
        context.im.currentInputConnection?.commitText(text, 1)
        if (context.state.shiftMode == ShiftMode.Shifted) {
            context.state.shiftMode = ShiftMode.Off
        }
    }
}
