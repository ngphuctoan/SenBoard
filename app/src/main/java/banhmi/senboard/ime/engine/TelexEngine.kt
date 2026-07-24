package banhmi.senboard.ime.engine

import java.text.Normalizer

object TelexEngine {

    fun convertWord(rawWord: String): String {
        if (rawWord.isEmpty()) return rawWord

        var word = rawWord.lowercase()
        
        // 1. Process initial consonant and double-d
        word = word.replace("dd", "đ").replace("Dd", "Đ").replace("DD", "Đ")

        // 2. Extract tone mark from the word (after the initial consonant)
        var tone = ""
        var toneCharIdx = -1
        
        // Find initial consonant length
        var initialLen = 0
        val lower = word.lowercase()
        if (lower.startsWith("ngh")) initialLen = 3
        else if (lower.startsWith("ch") || lower.startsWith("tr") || lower.startsWith("nh") ||
                 lower.startsWith("ng") || lower.startsWith("ph") || lower.startsWith("th") ||
                 lower.startsWith("kh") || lower.startsWith("gh") || lower.startsWith("gi") ||
                 lower.startsWith("qu")) {
            initialLen = 2
        } else if (lower.isNotEmpty() && !isVowel(lower[0])) {
            initialLen = 1
        }

        // Scan from the end for tone keys
        val toneKeys = setOf('s', 'f', 'r', 'x', 'j', 'z')
        for (i in word.length - 1 downTo initialLen) {
            if (toneKeys.contains(word[i])) {
                tone = when (word[i]) {
                    's' -> "sac"
                    'f' -> "huyen"
                    'r' -> "hoi"
                    'x' -> "nga"
                    'j' -> "nang"
                    'z' -> "none"
                    else -> ""
                }
                toneCharIdx = i
                break
            }
        }

        if (toneCharIdx != -1) {
            word = word.substring(0, toneCharIdx) + word.substring(toneCharIdx + 1)
        }

        // 3. Process Telex vowel modifications
        // Try replacing combined modifiers first
        if (word.contains("uow")) word = word.replace("uow", "ươ")
        if (word.contains("uwow")) word = word.replace("uwow", "ươ")

        // Single modifications
        word = word.replace("aa", "â")
        word = word.replace("ee", "ê")
        word = word.replace("oo", "ô")
        word = word.replace("aw", "ă")
        word = word.replace("uw", "ư")
        word = word.replace("ow", "ơ")

        // Handle standalone 'w' at the end or acting on preceding vowels
        if (word.contains("w")) {
            val sb = StringBuilder()
            var hasU = false
            var hasO = false
            for (c in word) {
                if (c == 'u') hasU = true
                if (c == 'o') hasO = true
            }
            for (c in word) {
                if (c == 'w') {
                    // If no u/o, 'w' is a standalone vowel 'ư'
                    if (!hasU && !hasO) {
                        sb.append('ư')
                    }
                    continue
                }
                if (c == 'u' && !word.contains("uo")) {
                    sb.append('ư')
                } else if (c == 'o' && !word.contains("uo")) {
                    sb.append('ơ')
                } else {
                    sb.append(c)
                }
            }
            word = sb.toString()
            // If it had uo and was followed by w, it should be ươ
            if (hasU && hasO) {
                word = word.replace("uo", "ươ")
            }
        }

        // 4. Apply Tone Accent if present
        if (tone.isNotEmpty() && tone != "none") {
            word = applyToneMark(word, tone)
        }

        // Restore capitalization
        if (rawWord.firstOrNull()?.isUpperCase() == true) {
            word = word.capitalizeFirstLetter()
        }

        return word
    }

    private fun isVowel(c: Char) = "aeiouyâêôăơư".contains(c.lowercaseChar())

    private fun applyToneMark(word: String, tone: String): String {
        val toneMap = mapOf(
            'a' to mapOf("sac" to 'á', "huyen" to 'à', "hoi" to 'ả', "nga" to 'ã', "nang" to 'ạ'),
            'â' to mapOf("sac" to 'ấ', "huyen" to 'ầ', "hoi" to 'ẩ', "nga" to 'ẫ', "nang" to 'ậ'),
            'ă' to mapOf("sac" to 'ắ', "huyen" to 'ằ', "hoi" to 'ẳ', "nga" to 'ẵ', "nang" to 'ặ'),
            'e' to mapOf("sac" to 'é', "huyen" to 'è', "hoi" to 'ẻ', "nga" to 'ẽ', "nang" to 'ẹ'),
            'ê' to mapOf("sac" to 'ế', "huyen" to 'ề', "hoi" to 'ể', "nga" to 'ễ', "nang" to 'ệ'),
            'i' to mapOf("sac" to 'í', "huyen" to 'ì', "hoi" to 'ỉ', "nga" to 'ĩ', "nang" to 'ị'),
            'o' to mapOf("sac" to 'ó', "huyen" to 'ò', "hoi" to 'ỏ', "nga" to 'õ', "nang" to 'ọ'),
            'ô' to mapOf("sac" to 'ố', "huyen" to 'ồ', "hoi" to 'ổ', "nga" to 'ỗ', "nang" to 'ộ'),
            'ơ' to mapOf("sac" to 'ớ', "huyen" to 'ờ', "hoi" to 'ở', "nga" to 'ỡ', "nang" to 'ợ'),
            'u' to mapOf("sac" to 'ú', "huyen" to 'ù', "hoi" to 'ủ', "nga" to 'ũ', "nang" to 'ụ'),
            'ư' to mapOf("sac" to 'ứ', "huyen" to 'ừ', "hoi" to 'ử', "nga" to 'ữ', "nang" to 'ự'),
            'y' to mapOf("sac" to 'ý', "huyen" to 'ỳ', "hoi" to 'ỷ', "nga" to 'ỹ', "nang" to 'ỵ')
        )

        val normWord = Normalizer.normalize(word, Normalizer.Form.NFC)
        val vowelIndices = mutableListOf<Int>()
        for (i in normWord.indices) {
            if (isVowel(normWord[i])) vowelIndices.add(i)
        }

        if (vowelIndices.isEmpty()) return normWord

        // Special handling for gi + vowel (e.g. giá, giao, gián) -> tone goes to vowel after i
        if (normWord.startsWith("gi") && normWord.length > 2 && isVowel(normWord[2])) {
            val vIndices = vowelIndices.filter { it >= 2 }
            if (vIndices.isNotEmpty()) {
                var targetIdx = vIndices.first()
                val primaryVowels = "êơôâăe"
                for (idx in vIndices) {
                    if (primaryVowels.contains(normWord[idx])) {
                        targetIdx = idx
                        break
                    }
                }
                val targetChar = normWord[targetIdx]
                val markedChar = toneMap[targetChar]?.get(tone) ?: targetChar
                return normWord.substring(0, targetIdx) + markedChar + normWord.substring(targetIdx + 1)
            }
        }

        // Special handling for qu + vowel (e.g. quả, quá, quyên, quan) -> tone goes to vowel after u
        if (normWord.startsWith("qu") && normWord.length > 2 && isVowel(normWord[2])) {
            val vIndices = vowelIndices.filter { it >= 2 }
            if (vIndices.isNotEmpty()) {
                var targetIdx = vIndices.first()
                val primaryVowels = "êơôâăe"
                for (idx in vIndices) {
                    if (primaryVowels.contains(normWord[idx])) {
                        targetIdx = idx
                        break
                    }
                }
                val targetChar = normWord[targetIdx]
                val markedChar = toneMap[targetChar]?.get(tone) ?: targetChar
                return normWord.substring(0, targetIdx) + markedChar + normWord.substring(targetIdx + 1)
            }
        }

        var targetIndex = vowelIndices.last()
        val primaryVowels = "êơôâăe"
        var foundPrimary = false

        for (idx in vowelIndices) {
            if (primaryVowels.contains(normWord[idx])) {
                targetIndex = idx
                foundPrimary = true
                break
            }
        }

        if (!foundPrimary && vowelIndices.size >= 2) {
            if (normWord.endsWith("c") || normWord.endsWith("p") || normWord.endsWith("t") || normWord.endsWith("n") || normWord.endsWith("m") || normWord.endsWith("ng") || normWord.endsWith("nh") || normWord.endsWith("ch")) {
                targetIndex = vowelIndices[vowelIndices.size - 1]
            } else {
                val vowelStr = vowelIndices.map { normWord[it] }.joinToString("")
                if (vowelStr == "oa" || vowelStr == "uy") {
                    targetIndex = vowelIndices[vowelIndices.size - 1]
                } else {
                    targetIndex = vowelIndices[vowelIndices.size - 2]
                }
            }
        }

        val targetChar = normWord[targetIndex]
        val markedChar = toneMap[targetChar]?.get(tone) ?: targetChar

        return normWord.substring(0, targetIndex) + markedChar + normWord.substring(targetIndex + 1)
    }

    private fun String.capitalizeFirstLetter(): String =
        if (isNotEmpty()) this[0].uppercaseChar() + substring(1) else this
}
