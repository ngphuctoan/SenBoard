package banhmi.senboard.ime.keyboard.core.handlers

import android.view.inputmethod.InputConnection
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.ShiftMode

class CharKeyHandler(private val char: String) : KeyHandler {
    override fun handle(context: SenBoardContext) {
//        val editor = context.getEditor() ?: return
//        val service = context.im ?: return
//        val prefs = SenBoardPreferences(service)
//
//        // CVNSS 4.0 Auto-conversion on Space / Punctuation
//        if (prefs.typingMode == "cvnss" && (char == " " || isPunctuation(char))) {
//            val wordBefore = getWordBeforeCursor(editor)
//            if (wordBefore.isNotEmpty()) {
//                val converted = CvnssEngine.convertWord(wordBefore)
//                if (converted != wordBefore) {
//                    editor.deleteSurroundingText(wordBefore.length, 0)
//                    editor.commitText(converted, 1)
//                }
//            }
//        }
//
//        // Telex Auto-conversion on Space / Punctuation
//        if (prefs.typingMode == "telex" && (char == " " || isPunctuation(char))) {
//            val wordBefore = getWordBeforeCursor(editor)
//            if (wordBefore.isNotEmpty()) {
//                val converted = banhmi.senboard.ime.engine.TelexEngine.convertWord(wordBefore)
//                if (converted != wordBefore) {
//                    editor.deleteSurroundingText(wordBefore.length, 0)
//                    editor.commitText(converted, 1)
//                }
//            }
//        }
//
//        // VNI Auto-conversion on Space / Punctuation
//        if (prefs.typingMode == "vni" && (char == " " || isPunctuation(char))) {
//            val wordBefore = getWordBeforeCursor(editor)
//            if (wordBefore.isNotEmpty()) {
//                val converted = banhmi.senboard.ime.engine.VniEngine.convertWord(wordBefore)
//                if (converted != wordBefore) {
//                    editor.deleteSurroundingText(wordBefore.length, 0)
//                    editor.commitText(converted, 1)
//                }
//            }
//        }
//
//        // 1. Double space to insert period
//        if (char == " " && prefs.doubleSpacePeriod) {
//            val lastTwo = editor.getTextBeforeCursor(2, 0)
//            if (lastTwo != null && lastTwo.length == 2 && lastTwo[1] == ' ' && lastTwo[0].isLetterOrDigit()) {
//                editor.deleteSurroundingText(1, 0)
//                editor.commitText(". ", 1)
//                return
//            }
//        }
//
//        // 2. Auto-capitalization
//        val currentShiftMode = context.state.shiftMode
//        val shouldAutoCap = prefs.autoCapitalize && isSentenceStart(editor)
//        val textToCommit = if (shouldAutoCap) {
//            char.applyShiftMode(ShiftMode.Shifted)
//        } else {
//            char.applyShiftMode(currentShiftMode)
//        }
//
//        // On-the-fly Telex conversion for letter keys
//        if (prefs.typingMode == "telex" && char.length == 1 && char[0].isLetter()) {
//            val wordBefore = getWordBeforeCursor(editor)
//
//            // Try Telex double-key escape first
//            val escaped = tryTelexEscape(wordBefore, textToCommit)
//            if (escaped != null) {
//                editor.deleteSurroundingText(wordBefore.length, 0)
//                editor.commitText(escaped, 1)
//
//                // Reset temporary shift mode
//                if (currentShiftMode == ShiftMode.Shifted) {
//                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
//                }
//                return
//            }
//
//            val candidateWord = wordBefore + textToCommit
//            val converted = banhmi.senboard.ime.engine.TelexEngine.convertWord(candidateWord)
//            if (converted != candidateWord) {
//                editor.deleteSurroundingText(wordBefore.length, 0)
//                editor.commitText(converted, 1)
//
//                // Reset temporary shift mode
//                if (currentShiftMode == ShiftMode.Shifted) {
//                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
//                }
//                return
//            }
//        }
//
//        // On-the-fly VNI conversion for digit keys
//        if (prefs.typingMode == "vni" && char.length == 1 && char[0].isDigit()) {
//            val wordBefore = getWordBeforeCursor(editor)
//
//            // Try VNI escape first
//            val escaped = tryVniEscape(wordBefore, textToCommit)
//            if (escaped != null) {
//                editor.deleteSurroundingText(wordBefore.length, 0)
//                editor.commitText(escaped, 1)
//
//                // Reset temporary shift mode
//                if (currentShiftMode == ShiftMode.Shifted) {
//                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
//                }
//                return
//            }
//
//            val candidateWord = wordBefore + textToCommit
//            val converted = banhmi.senboard.ime.engine.VniEngine.convertWord(candidateWord)
//            if (converted != candidateWord) {
//                editor.deleteSurroundingText(wordBefore.length, 0)
//                editor.commitText(converted, 1)
//
//                // Reset temporary shift mode
//                if (currentShiftMode == ShiftMode.Shifted) {
//                    context.state = context.state.copy(shiftMode = ShiftMode.Off)
//                }
//                return
//            }
//        }
//
//        editor.commitText(textToCommit, 1)
//
//        // 3. Record user bigram after space
//        if (char == " ") {
//            val twoWords = getLastTwoWordsBeforeCursor(editor)
//            if (twoWords != null) {
//                UserBigramStore.recordBigram(context.im, twoWords.first, twoWords.second)
//            }
//        }
//
//        // 4. Reset temporary shift mode
//        if (currentShiftMode == ShiftMode.Shifted) {
//            context.state = context.state.copy(shiftMode = ShiftMode.Off)
//        }
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

        val toneKeyMap = mapOf(
            "s" to "sac",
            "f" to "huyen",
            "r" to "hoi",
            "x" to "nga",
            "j" to "nang"
        )
        val targetTone = toneKeyMap[keyLower]
        if (targetTone != null) {
            val (strippedWord, currentTone) = stripToneFromWord(wordBefore)
            if (currentTone == targetTone) {
                return strippedWord + keyLower
            }
        }

        return null
    }

    private fun stripToneFromWord(word: String): Pair<String, String> {
        val accentMap = mapOf(
            'á' to Pair('a', "sac"), 'à' to Pair('a', "huyen"), 'ả' to Pair('a', "hoi"), 'ã' to Pair('a', "nga"), 'ạ' to Pair('a', "nang"),
            'ấ' to Pair('â', "sac"), 'ầ' to Pair('â', "huyen"), 'ẩ' to Pair('â', "hoi"), 'ẫ' to Pair('â', "nga"), 'ậ' to Pair('â', "nang"),
            'ắ' to Pair('ă', "sac"), 'ằ' to Pair('ă', "huyen"), 'ẳ' to Pair('ă', "hoi"), 'ẵ' to Pair('ă', "nga"), 'ặ' to Pair('ă', "nang"),
            'é' to Pair('e', "sac"), 'è' to Pair('e', "huyen"), 'ẻ' to Pair('e', "hoi"), 'ẽ' to Pair('e', "nga"), 'ẹ' to Pair('e', "nang"),
            'ế' to Pair('ê', "sac"), 'ề' to Pair('ê', "huyen"), 'ể' to Pair('ê', "hoi"), 'ễ' to Pair('ê', "nga"), 'ệ' to Pair('ê', "nang"),
            'í' to Pair('i', "sac"), 'ì' to Pair('i', "huyen"), 'ỉ' to Pair('i', "hoi"), 'ĩ' to Pair('i', "nga"), 'ị' to Pair('i', "nang"),
            'ó' to Pair('o', "sac"), 'ò' to Pair('o', "huyen"), 'ỏ' to Pair('o', "hoi"), 'õ' to Pair('o', "nga"), 'ọ' to Pair('o', "nang"),
            'ố' to Pair('ô', "sac"), 'ồ' to Pair('ô', "huyen"), 'ổ' to Pair('ô', "hoi"), 'ỗ' to Pair('ô', "nga"), 'ộ' to Pair('ô', "nang"),
            'ớ' to Pair('ơ', "sac"), 'ờ' to Pair('ơ', "huyen"), 'ở' to Pair('ơ', "hoi"), 'ỡ' to Pair('ơ', "nga"), 'ợ' to Pair('ơ', "nang"),
            'ú' to Pair('u', "sac"), 'ù' to Pair('u', "huyen"), 'ủ' to Pair('u', "hoi"), 'ũ' to Pair('u', "nga"), 'ụ' to Pair('u', "nang"),
            'ứ' to Pair('ư', "sac"), 'ừ' to Pair('ư', "huyen"), 'ử' to Pair('ư', "hoi"), 'ữ' to Pair('ư', "nga"), 'ự' to Pair('ư', "nang"),
            'ý' to Pair('y', "sac"), 'ỳ' to Pair('y', "huyen"), 'ỷ' to Pair('y', "hoi"), 'ỹ' to Pair('y', "nga"), 'ỵ' to Pair('y', "nang")
        )

        val norm = java.text.Normalizer.normalize(word, java.text.Normalizer.Form.NFC)
        val sb = StringBuilder()
        var currentTone = ""
        for (c in norm) {
            val mapped = accentMap[c]
            if (mapped != null) {
                sb.append(mapped.first)
                currentTone = mapped.second
            } else {
                sb.append(c)
            }
        }
        return Pair(sb.toString(), currentTone)
    }

    private fun tryVniEscape(wordBefore: String, key: String): String? {
        if (wordBefore.isEmpty()) return null
        val lastChar = wordBefore.last()
        val keyChar = key.firstOrNull() ?: return null

        if (keyChar == '9') {
            if (lastChar == 'đ') return wordBefore.dropLast(1) + "d9"
            if (lastChar == 'Đ') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "D9" else "D9")
            }
        }
        if (keyChar == '6') {
            if (lastChar == 'â') return wordBefore.dropLast(1) + "a6"
            if (lastChar == 'Â') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "A6" else "A6")
            }
            if (lastChar == 'ê') return wordBefore.dropLast(1) + "e6"
            if (lastChar == 'Ê') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "E6" else "E6")
            }
            if (lastChar == 'ô') return wordBefore.dropLast(1) + "o6"
            if (lastChar == 'Ô') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "O6" else "O6")
            }
        }
        if (keyChar == '8') {
            if (lastChar == 'ă') return wordBefore.dropLast(1) + "a8"
            if (lastChar == 'Ă') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "A8" else "A8")
            }
        }
        if (keyChar == '7') {
            if (wordBefore.endsWith("ươ")) return wordBefore.dropLast(2) + "uo7"
            if (wordBefore.endsWith("Ươ")) return wordBefore.dropLast(2) + "Uo7"
            if (wordBefore.endsWith("ƯƠ")) return wordBefore.dropLast(2) + "UO7"
            if (lastChar == 'ư') return wordBefore.dropLast(1) + "u7"
            if (lastChar == 'Ư') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "U7" else "U7")
            }
            if (lastChar == 'ơ') return wordBefore.dropLast(1) + "o7"
            if (lastChar == 'Ơ') {
                val isAllUpper = wordBefore.dropLast(1).all { it.isUpperCase() }
                return wordBefore.dropLast(1) + (if (isAllUpper) "O7" else "O7")
            }
        }

        // Tone escape mapping for keys 1-5 (only escape when key matches the tone type)
        val toneEscapes = mapOf(
            '1' to mapOf(
                'á' to "a1", 'ấ' to "â1", 'ắ' to "ă1", 'é' to "e1", 'ế' to "ê1", 'í' to "i1",
                'ó' to "o1", 'ố' to "ô1", 'ớ' to "ơ1", 'ú' to "u1", 'ứ' to "ư1", 'ý' to "y1"
            ),
            '2' to mapOf(
                'à' to "a2", 'ầ' to "â2", 'ằ' to "ă2", 'è' to "e2", 'ề' to "ê2", 'ì' to "i2",
                'ò' to "o2", 'ồ' to "ô2", 'ờ' to "ơ2", 'ù' to "u2", 'ừ' to "ư2", 'ỳ' to "y2"
            ),
            '3' to mapOf(
                'ả' to "a3", 'ẩ' to "â3", 'ẳ' to "ă3", 'ẻ' to "e3", 'ể' to "ê3", 'ỉ' to "i3",
                'ỏ' to "o3", 'ổ' to "ô3", 'ở' to "ơ3", 'ủ' to "u3", 'ử' to "ư3", 'ỷ' to "y3"
            ),
            '4' to mapOf(
                'ã' to "a4", 'ẫ' to "â4", 'ẵ' to "ă4", 'ẽ' to "e4", 'ễ' to "ê4", 'ĩ' to "i4",
                'õ' to "o4", 'ỗ' to "ô4", 'ỡ' to "ơ4", 'ũ' to "u4", 'ữ' to "ư4", 'ỹ' to "y4"
            ),
            '5' to mapOf(
                'ạ' to "a5", 'ậ' to "â5", 'ặ' to "ă5", 'ẹ' to "e5", 'ệ' to "ê5", 'ị' to "i5",
                'ọ' to "o5", 'ộ' to "ô5", 'ợ' to "ơ5", 'ụ' to "u5", 'ự' to "ư5", 'ỵ' to "y5"
            )
        )

        val escMap = toneEscapes[keyChar]
        if (escMap != null) {
            val lowerChar = lastChar.lowercaseChar()
            val replacement = escMap[lowerChar]
            if (replacement != null) {
                val isUpper = lastChar.isUpperCase()
                val finalReplacement = if (isUpper) replacement.uppercase() else replacement
                return wordBefore.dropLast(1) + finalReplacement
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

    private fun getLastTwoWordsBeforeCursor(editor: InputConnection): Pair<String, String>? {
        val before = editor.getTextBeforeCursor(60, 0)?.toString() ?: ""
        if (before.isEmpty()) return null
        val parts = before.trim().split(Regex("[\\s.,!?:;()\"']+")).filter { it.isNotEmpty() }
        if (parts.size >= 2) {
            return Pair(parts[parts.size - 2], parts[parts.size - 1])
        }
        return null
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
