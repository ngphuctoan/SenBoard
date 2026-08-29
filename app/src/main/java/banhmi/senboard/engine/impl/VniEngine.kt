package banhmi.senboard.engine.impl

import banhmi.senboard.engine.VietnameseEngine
import java.text.Normalizer

object VniEngine : VietnameseEngine {

    override fun convertWord(rawWord: String): String {
        if (rawWord.isEmpty()) return rawWord

        val normalizedRaw = Normalizer.normalize(rawWord, Normalizer.Form.NFC)
        var word = normalizedRaw.lowercase()

        // Handle repeated digit escape (e.g. a11 -> a1, a111 -> a11, a1111 -> a111, a666 -> a66, d999 -> d99)
        val digits = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
        for (digit in digits) {
            var maxRepeat = 0
            var tempWord = word
            while (tempWord.endsWith(digit.toString())) {
                maxRepeat++
                tempWord = tempWord.dropLast(1)
            }
            if (maxRepeat >= 2 && tempWord.isNotEmpty()) {
                val stem = tempWord
                val cleanStem = convertWord(stem)
                val (pureStem, _) = stripTone(cleanStem)
                val unaccentedStem = stripAccents(pureStem)
                val escapedDigits = digit.toString().repeat(maxRepeat - 1)
                return restoreCapitalization(rawWord, unaccentedStem + escapedDigits)
            }
        }

        // Handle single digit escape on already accented word (e.g. á1 -> a1, à2 -> a2, â6 -> a6, đ9 -> d9)
        if (word.length >= 2 && word.last().isDigit()) {
            val lastDigit = word.last()
            val stem = word.dropLast(1)
            val (cleanStem, existingTone) = stripTone(stem)
            val hasAccents = stem != cleanStem || cleanStem != stripAccents(cleanStem)
            
            // Map digits to their corresponding accent/tone
            val toneForDigit = mapOf(
                '1' to "sac", '2' to "huyen", '3' to "hoi", '4' to "nga", '5' to "nang"
            )
            
            if (hasAccents) {
                // If pressing same tone digit again on already accented stem (e.g. á + 1 -> a1, â + 6 -> a6)
                val isToneDigit = toneForDigit[lastDigit] == existingTone
                val isModifierDigit = (lastDigit == '6' && stem.containsAny("â", "ê", "ô")) ||
                                      (lastDigit == '7' && stem.containsAny("ơ", "ư")) ||
                                      (lastDigit == '8' && stem.containsAny("ă")) ||
                                      (lastDigit == '9' && stem.contains("đ"))
                if (isToneDigit || isModifierDigit) {
                    val unaccentedStem = stripAccents(cleanStem)
                    return restoreCapitalization(rawWord, unaccentedStem + lastDigit)
                }
            }
        }

        val (cleanWord, existingTone) = stripTone(word)
        word = cleanWord

        // Extract VNI strokes (digits 0-9)
        val modifiers = mutableSetOf<Char>()
        var newTone = ""
        val cleanedWordBuilder = StringBuilder()

        for (c in word) {
            if (c.isDigit()) {
                when (c) {
                    '1' -> newTone = "sac"
                    '2' -> newTone = "huyen"
                    '3' -> newTone = "hoi"
                    '4' -> newTone = "nga"
                    '5' -> newTone = "nang"
                    '0' -> newTone = "none"
                    else -> modifiers.add(c)
                }
            } else {
                cleanedWordBuilder.append(c)
            }
        }
        val cleanStem = cleanedWordBuilder.toString()

        // If a digit is followed by letters (e.g. d9uong72, mp3player, page1a), check if cleanStem is valid Vietnamese
        val lastDigitIndex = word.indexOfLast { it.isDigit() }
        if (lastDigitIndex != -1 && lastDigitIndex < word.length - 1) {
            val hasLettersAfterDigit = word.substring(lastDigitIndex + 1).any { it.isLetter() }
            if (hasLettersAfterDigit && !isValidSyllableStem(cleanStem)) {
                return rawWord
            }
        }

        word = cleanStem

        // 1. Consonant modifier (9 -> đ)
        if (modifiers.contains('9')) {
            word = word.replace("d", "đ")
        }

        // 2. Vowel modifier (6 -> â, ê, ô)
        if (modifiers.contains('6')) {
            word = word.replace("a", "â")
                       .replace("e", "ê")
                       .replace("o", "ô")
        }

        // 3. Vowel modifier (8 -> ă)
        if (modifiers.contains('8')) {
            word = word.replace("a", "ă")
        }

        // 4. Vowel modifier (7 -> ơ, ư, ươ)
        if (modifiers.contains('7')) {
            if (word.contains("u") && word.contains("o")) {
                word = word.replace("uo", "ươ")
            } else {
                word = word.replace("u", "ư")
                           .replace("o", "ơ")
            }
        }

        // 5. Apply Tone Accent if present
        var tone = existingTone
        if (newTone.isNotEmpty()) {
            if (existingTone.isNotEmpty() && existingTone == newTone && newTone != "none") {
                // Pressing same digit tone key again cancels tone
                tone = "none"
            } else {
                tone = newTone
            }
        }

        if (tone.isNotEmpty() && tone != "none") {
            word = applyToneMark(word, tone)
        }

        return restoreCapitalization(rawWord, word)
    }

    private fun isValidSyllableStem(stem: String): Boolean {
        if (stem.isEmpty()) return false
        val lower = Normalizer.normalize(stem.lowercase(), Normalizer.Form.NFC)

        val validInitialConsonants = listOf(
            "ngh", "ch", "tr", "nh", "ng", "ph", "th", "kh", "gh", "gi", "qu", "đ",
            "b", "c", "d", "g", "h", "k", "l", "m", "n", "p", "r", "s", "t", "v", "x"
        )
        var initialLen = 0
        if (!isVowel(lower[0])) {
            val matchedInitial = validInitialConsonants.firstOrNull { lower.startsWith(it) }
            if (matchedInitial == null) return false
            initialLen = matchedInitial.length
        }

        val rest = lower.substring(initialLen)
        if (rest.isEmpty() || !isVowel(rest[0])) return false

        val validFinalConsonants = listOf("ng", "nh", "ch", "c", "m", "n", "p", "t")
        val validVowelChars = setOf('a', 'e', 'o', 'u', 'i', 'y', 'w', 'â', 'ê', 'ô', 'ă', 'ơ', 'ư')

        var workRest = rest
        for (fc in validFinalConsonants) {
            if (workRest.contains(fc)) {
                workRest = workRest.replaceFirst(fc, "")
                break
            }
        }

        if (workRest.isEmpty()) return false
        for (c in workRest) {
            if (!validVowelChars.contains(c)) return false
        }

        return true
    }

    private fun stripAccents(str: String): String {
        return str.replace("â", "a").replace("ă", "a")
                  .replace("ê", "e")
                  .replace("ô", "o").replace("ơ", "o")
                  .replace("ư", "u")
                  .replace("đ", "d")
                  .replace("Â", "A").replace("Ă", "A")
                  .replace("Ê", "E")
                  .replace("Ô", "O").replace("Ơ", "O")
                  .replace("Ư", "U")
                  .replace("Đ", "D")
    }

    private fun String.containsAny(vararg vowels: String): Boolean {
        return vowels.any { this.contains(it) }
    }

    private fun restoreCapitalization(rawWord: String, convertedWord: String): String {
        val lettersOnly = rawWord.filter { it.isLetter() }
        return if (lettersOnly.isNotEmpty() && lettersOnly.all { it.isUpperCase() }) {
            convertedWord.uppercase()
        } else if (rawWord.firstOrNull()?.isUpperCase() == true) {
            convertedWord.capitalizeFirstLetter()
        } else {
            convertedWord
        }
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

        val primaryVowelsPriority = listOf('ê', 'ơ', 'ô', 'â', 'ă', 'e', 'ư')

        // Special handling for gi + vowel (e.g. giá, giao, gián) -> tone goes to vowel after i
        if (normWord.startsWith("gi") && normWord.length > 2 && isVowel(normWord[2])) {
            val vIndices = vowelIndices.filter { it >= 2 }
            if (vIndices.isNotEmpty()) {
                var targetIdx = vIndices.first()
                for (pChar in primaryVowelsPriority) {
                    val idx = vIndices.firstOrNull { normWord[it] == pChar }
                    if (idx != null) {
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
                for (pChar in primaryVowelsPriority) {
                    val idx = vIndices.firstOrNull { normWord[it] == pChar }
                    if (idx != null) {
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
        var foundPrimary = false

        for (pChar in primaryVowelsPriority) {
            val idx = vowelIndices.firstOrNull { normWord[it] == pChar }
            if (idx != null) {
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

    private fun stripTone(word: String): Pair<String, String> {
        val toneMap = mapOf(
            'á' to ('a' to "sac"), 'à' to ('a' to "huyen"), 'ả' to ('a' to "hoi"), 'ã' to ('a' to "nga"), 'ạ' to ('a' to "nang"),
            'ấ' to ('â' to "sac"), 'ầ' to ('â' to "huyen"), 'ẩ' to ('â' to "hoi"), 'ẫ' to ('â' to "nga"), 'ậ' to ('â' to "nang"),
            'ắ' to ('ă' to "sac"), 'ằ' to ('ă' to "huyen"), 'ẳ' to ('ă' to "hoi"), 'ẵ' to ('ă' to "nga"), 'ặ' to ('ă' to "nang"),
            'é' to ('e' to "sac"), 'è' to ('e' to "huyen"), 'ẻ' to ('e' to "hoi"), 'ẽ' to ('e' to "nga"), 'ẹ' to ('e' to "nang"),
            'ế' to ('ê' to "sac"), 'ề' to ('ê' to "huyen"), 'ể' to ('ê' to "hoi"), 'ễ' to ('ê' to "nga"), 'ệ' to ('ê' to "nang"),
            'í' to ('i' to "sac"), 'ì' to ('i' to "huyen"), 'ỉ' to ('i' to "hoi"), 'ĩ' to ('i' to "nga"), 'ị' to ('i' to "nang"),
            'ó' to ('o' to "sac"), 'ò' to ('o' to "huyen"), 'ỏ' to ('o' to "hoi"), 'õ' to ('o' to "nga"), 'ọ' to ('o' to "nang"),
            'ố' to ('ô' to "sac"), 'ồ' to ('ô' to "huyen"), 'ổ' to ('ô' to "hoi"), 'ỗ' to ('ô' to "nga"), 'ộ' to ('ô' to "nang"),
            'ớ' to ('ơ' to "sac"), 'ờ' to ('ơ' to "huyen"), 'ở' to ('ơ' to "hoi"), 'ỡ' to ('ơ' to "nga"), 'ợ' to ('ơ' to "nang"),
            'ú' to ('u' to "sac"), 'ù' to ('u' to "huyen"), 'ủ' to ('u' to "hoi"), 'ũ' to ('u' to "nga"), 'ụ' to ('u' to "nang"),
            'ứ' to ('ư' to "sac"), 'ừ' to ('ư' to "huyen"), 'ử' to ('ư' to "hoi"), 'ữ' to ('ư' to "nga"), 'ự' to ('ư' to "nang"),
            'ý' to ('y' to "sac"), 'ỳ' to ('y' to "huyen"), 'ỷ' to ('y' to "hoi"), 'ỹ' to ('y' to "nang"), 'ỵ' to ('y' to "nang")
        )

        val norm = Normalizer.normalize(word, Normalizer.Form.NFC)
        val sb = StringBuilder()
        var extractedTone = ""
        for (c in norm) {
            val mapping = toneMap[c]
            if (mapping != null) {
                sb.append(mapping.first)
                extractedTone = mapping.second
            } else {
                sb.append(c)
            }
        }
        return Pair(sb.toString(), extractedTone)
    }

    private fun String.capitalizeFirstLetter(): String =
        if (isNotEmpty()) this[0].uppercaseChar() + substring(1) else this
}
