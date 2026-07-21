package banhmi.senboard.ime.keyboard.core.handlers

import android.view.inputmethod.InputConnection
import banhmi.senboard.app.settings.SenBoardPreferences
import banhmi.senboard.ime.engine.CvnssEngine
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode

class CharKeyHandler(private val char: String) : KeyHandler {
    override fun handle(context: SenBoardContext) {
        val editor = context.getEditor() ?: return
        val service = context.im ?: return
        val prefs = SenBoardPreferences(service)

        // CVNSS 4.0 Auto-conversion on Space / Punctuation
        if (prefs.typingMode == "cvnss" && (char == " " || isPunctuation(char))) {
            val wordBefore = getWordBeforeCursor(editor)
            if (wordBefore.isNotEmpty()) {
                val converted = CvnssEngine.convertWord(wordBefore)
                if (converted != wordBefore) {
                    editor.deleteSurroundingText(wordBefore.length, 0)
                    editor.commitText(converted, 1)
                }
            }
        }

        // 1. Double space to insert period
        if (char == " " && prefs.doubleSpacePeriod) {
            val lastTwo = editor.getTextBeforeCursor(2, 0)
            if (lastTwo != null && lastTwo.length == 2 && lastTwo[1] == ' ' && lastTwo[0].isLetterOrDigit()) {
                editor.deleteSurroundingText(1, 0)
                editor.commitText(". ", 1)
                return
            }
        }

        // 2. Auto-capitalization
        val currentShiftMode = context.state.shiftMode
        val shouldAutoCap = prefs.autoCapitalize && isSentenceStart(editor)
        val textToCommit = if (shouldAutoCap) {
            char.applyShiftMode(ShiftMode.Shifted)
        } else {
            char.applyShiftMode(currentShiftMode)
        }

        editor.commitText(textToCommit, 1)

        // 3. Reset temporary shift mode
        if (currentShiftMode == ShiftMode.Shifted) {
            context.state = context.state.copy(shiftMode = ShiftMode.Off)
        }
    }

    private fun getWordBeforeCursor(editor: InputConnection): String {
        val before = editor.getTextBeforeCursor(30, 0)?.toString() ?: ""
        if (before.isEmpty()) return ""
        val parts = before.split(Regex("[\\s.,!?:;()\"']"))
        return parts.lastOrNull() ?: ""
    }

    private fun isPunctuation(c: String): Boolean {
        return c == "." || c == "," || c == "!" || c == "?" || c == ";" || c == ":"
    }

    private fun String.applyShiftMode(shiftMode: ShiftMode): String =
        if (shiftMode == ShiftMode.Off) lowercase() else uppercase()

    private fun isSentenceStart(editor: InputConnection): Boolean {
        val before = editor.getTextBeforeCursor(40, 0)?.toString() ?: ""
        if (before.isEmpty()) return true

        val trimmed = before.trimEnd()
        if (trimmed.isEmpty()) return true

        val lastChar = trimmed.last()
        if (lastChar == '.' || lastChar == '?' || lastChar == '!' || lastChar == '\n') {
            val rest = before.substring(trimmed.length)
            if (rest.contains(' ') || rest.contains('\n') || before.endsWith('\n')) {
                return true
            }
        }
        return false
    }
}
