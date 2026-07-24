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

        // Telex Auto-conversion on Space / Punctuation
        if (prefs.typingMode == "telex" && (char == " " || isPunctuation(char))) {
            val wordBefore = getWordBeforeCursor(editor)
            if (wordBefore.isNotEmpty()) {
                val converted = banhmi.senboard.ime.engine.TelexEngine.convertWord(wordBefore)
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

        // On-the-fly Telex conversion for letter keys
        if (prefs.typingMode == "telex" && char.length == 1 && char[0].isLetter()) {
            val wordBefore = getWordBeforeCursor(editor)
            
            // Try Telex double-key escape first
            val escaped = tryTelexEscape(wordBefore, textToCommit)
            if (escaped != null) {
                editor.deleteSurroundingText(wordBefore.length, 0)
                editor.commitText(escaped, 1)
                
                // Reset temporary shift mode
                if (currentShiftMode == ShiftMode.Shifted) {
                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
                }
                return
            }

            val candidateWord = wordBefore + textToCommit
            val converted = banhmi.senboard.ime.engine.TelexEngine.convertWord(candidateWord)
            if (converted != candidateWord) {
                editor.deleteSurroundingText(wordBefore.length, 0)
                editor.commitText(converted, 1)
                
                // Reset temporary shift mode
                if (currentShiftMode == ShiftMode.Shifted) {
                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
                }
                return
            }
        }

        editor.commitText(textToCommit, 1)

        // 3. Reset temporary shift mode
        if (currentShiftMode == ShiftMode.Shifted) {
            context.state = context.state.copy(shiftMode = ShiftMode.Off)
        }
    }

    private fun tryTelexEscape(wordBefore: String, key: String): String? {
        if (wordBefore.isEmpty()) return null
        
        val lastChar = wordBefore.last()
        val keyLower = key.lowercase()

        if (keyLower == "d") {
            if (lastChar == 'đ') {
                return wordBefore.dropLast(1) + "dd"
            }
            if (lastChar == 'Đ') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "DD" else "Dd")
            }
        }
        if (keyLower == "a") {
            if (lastChar == 'â') return wordBefore.dropLast(1) + "aa"
            if (lastChar == 'Â') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "AA" else "Aa")
            }
        }
        if (keyLower == "e") {
            if (lastChar == 'ê') return wordBefore.dropLast(1) + "ee"
            if (lastChar == 'Ê') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "EE" else "Ee")
            }
        }
        if (keyLower == "o") {
            if (lastChar == 'ô') return wordBefore.dropLast(1) + "oo"
            if (lastChar == 'Ô') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "OO" else "Oo")
            }
        }
        if (keyLower == "w") {
            if (wordBefore.endsWith("ươ")) return wordBefore.dropLast(2) + "uow"
            if (wordBefore.endsWith("Ươ")) return wordBefore.dropLast(2) + "Uow"
            if (wordBefore.endsWith("ƯƠ")) return wordBefore.dropLast(2) + "UOW"
            
            if (lastChar == 'ơ') return wordBefore.dropLast(1) + "ow"
            if (lastChar == 'Ơ') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "OW" else "Ow")
            }
            if (lastChar == 'ă') return wordBefore.dropLast(1) + "aw"
            if (lastChar == 'Ă') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "AW" else "Aw")
            }
            if (lastChar == 'ư') return wordBefore.dropLast(1) + "w"
            if (lastChar == 'Ư') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "W" else "W")
            }
        }
        return null
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
