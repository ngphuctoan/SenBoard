package banhmi.senboard.engine.impl

import banhmi.senboard.engine.VietnameseEngine
import java.text.Normalizer

object TelexEngine : VietnameseEngine {

    override fun convertWord(rawWord: String): String {
        if (rawWord.isEmpty()) return rawWord

        val normalizedRaw = Normalizer.normalize(rawWord, Normalizer.Form.NFC)
        var word = normalizedRaw.lowercase()

        // Handle double tone key escape anywhere in word (e.g. "ess" -> "es", "essc" -> "esc", "esscape" -> "escape")
        val doubleToneKeys = mapOf("ss" to 's', "ff" to 'f', "rr" to 'r', "xx" to 'x', "jj" to 'j')
        for ((doubleKey, singleKey) in doubleToneKeys) {
            if (word.contains(doubleKey)) {
                val collapsed = word.replace(doubleKey, singleKey.toString())
                if (isEnglishWord(collapsed)) {
                    return restoreCapitalization(rawWord, collapsed)
                }
                if (word.endsWith(doubleKey) && word.length >= 3) {
                    val baseStem = word.dropLast(2)
                    val (cleanBase, baseTone) = stripTone(baseStem)
                    if (baseTone.isNotEmpty() || isValidSingleVietnameseSyllableStem(cleanBase) || isValidFlexibleSyllableStem(cleanBase)) {
                        val convertedBase = convertWord(baseStem)
                        val (pureBase, _) = stripTone(convertedBase)
                        val result = pureBase + singleKey
                        return restoreCapitalization(rawWord, result)
                    }
                }
            }
        }

        if (isEnglishWord(word)) {
            // Check if word contains a tone key whose removal leaves a valid Vietnamese stem (e.g. chuyeern -> chuyeen, soosng -> soong)
            var isVietnameseWithTone = false
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

            val toneKeys = setOf('s', 'f', 'r', 'x', 'j', 'z')
            for (i in word.length - 1 downTo initialLen) {
                if (toneKeys.contains(word[i])) {
                    if ((i > 0 && word[i - 1] == word[i]) || (i < word.length - 1 && word[i + 1] == word[i])) {
                        continue
                    }
                    val stemWithoutTone = word.substring(0, i) + word.substring(i + 1)
                    if (isValidSingleVietnameseSyllableStem(stemWithoutTone) || isValidFlexibleSyllableStem(stemWithoutTone)) {
                        isVietnameseWithTone = true
                        break
                    }
                }
            }

            if (!isVietnameseWithTone) {
                return rawWord
            }
        }

        val (cleanWord, existingTone) = stripTone(word)
        word = cleanWord
        
        // 1. Process initial consonant and double-d (including flexible non-adjacent d's: dedem -> đeem, duongdf -> đươngf)
        val dCount = word.count { it == 'd' }
        if (dCount >= 2) {
            val firstD = word.indexOf('d')
            val sb = StringBuilder()
            var seenD = false
            for (i in word.indices) {
                if (word[i] == 'd') {
                    if (!seenD) {
                        sb.append('đ')
                        seenD = true
                    }
                } else {
                    sb.append(word[i])
                }
            }
            word = sb.toString()
        } else {
            word = word.replace("dd", "đ").replace("Dd", "Đ").replace("DD", "Đ")
        }

        // 2. Extract tone mark from the word (supporting flexible tone key position e.g. sosong, sosngo, songos, duongwfd, kierue)
        var newTone = ""
        var toneCharIdx = -1
        
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
                // Skip adjacent doubled tone keys (e.g. 'ff' in confflict, 'rr' in merrge, 'ss' in cvnss)
                if ((i > 0 && word[i - 1] == word[i]) || (i < word.length - 1 && word[i + 1] == word[i])) {
                    continue
                }

                val stemWithoutTone = word.substring(0, i) + word.substring(i + 1)
                if (isValidSingleVietnameseSyllableStem(stemWithoutTone) || isValidFlexibleSyllableStem(stemWithoutTone)) {
                    newTone = when (word[i]) {
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
        }

        if (toneCharIdx != -1) {
            word = word.substring(0, toneCharIdx) + word.substring(toneCharIdx + 1)
        }

        // 3. Process universal flexible Telex vowel modifications
        word = applyFlexibleVowelModifications(word)

        // 4. Apply Tone Accent if present
        var tone = existingTone
        var isEscapingTone = false

        if (newTone.isNotEmpty()) {
            if (existingTone.isNotEmpty() && existingTone == newTone && newTone != "none") {
                // Pressing same tone key again (e.g. é + s -> es, sống + s -> sôngs) cancels tone
                tone = "none"
                isEscapingTone = true
            } else {
                tone = newTone
            }
        }

        if (tone.isNotEmpty() && tone != "none") {
            word = applyToneMark(word, tone)
        }

        if (isEscapingTone && toneCharIdx != -1) {
            val escapedChar = normalizedRaw.lowercase()[toneCharIdx]
            word += escapedChar
        }

        return restoreCapitalization(rawWord, word)
    }

    private fun applyFlexibleVowelModifications(rawWord: String): String {
        var word = rawWord

        // 1. Handle u, o, w combinations for ươ (e.g. uow, uwow, u...o...w, u...w...o, o...w...u)
        if (word.contains("w") && (word.contains("uo") || (word.contains("u") && word.contains("o")))) {
            word = word.replace("uow", "ươ")
                       .replace("uwow", "ươ")
                       .replace("owu", "ươ")
                       .replace("uwo", "ươ")
                       .replace("wuo", "ươ")
                       .replace("wou", "ươ")
            if (word.contains("w")) {
                word = word.replace("uo", "ươ").replace("w", "")
            }
        }

        // 2. Double vowel modifiers across vowels (e.g. kieue -> kiêu, chuyeerne -> chuyển, uoio -> uôi)
        // Only combine double vowels if all characters between them are ONLY vowels!
        if (word.contains("ee") || (word.contains("e") && word.count { it == 'e' } >= 2)) {
            word = word.replace("ee", "ê")
            if (word.contains("e") && !word.contains("ê")) {
                val firstE = word.indexOf('e')
                val lastE = word.lastIndexOf('e')
                val between = word.substring(firstE + 1, lastE)
                if (between.isEmpty() || between.all { isVowel(it) }) {
                    word = word.substring(0, firstE) + 'ê' + word.substring(firstE + 1).replaceFirst("e", "")
                }
            }
            if (word.contains("ê") && word.endsWith("e")) {
                word = word.dropLast(1)
            }
        }

        if (word.contains("oo") || (word.contains("o") && word.count { it == 'o' } >= 2)) {
            word = word.replace("oo", "ô")
            if (word.contains("o") && !word.contains("ô")) {
                val firstO = word.indexOf('o')
                val lastO = word.lastIndexOf('o')
                val between = word.substring(firstO + 1, lastO)
                if (between.isEmpty() || between.all { isVowel(it) }) {
                    word = word.substring(0, firstO) + 'ô' + word.substring(firstO + 1).replaceFirst("o", "")
                }
            }
            if (word.contains("ô") && word.endsWith("o")) {
                word = word.dropLast(1)
            }
        }

        if (word.contains("aa") || (word.contains("a") && word.count { it == 'a' } >= 2)) {
            word = word.replace("aa", "â")
            if (word.contains("a") && !word.contains("â")) {
                val firstA = word.indexOf('a')
                val lastA = word.lastIndexOf('a')
                val between = word.substring(firstA + 1, lastA)
                if (between.isEmpty() || between.all { isVowel(it) }) {
                    word = word.substring(0, firstA) + 'â' + word.substring(firstA + 1).replaceFirst("a", "")
                }
            }
            if (word.contains("â") && word.endsWith("a")) {
                word = word.dropLast(1)
            }
        }

        // 3. Single modifications with 'w'
        if (word.contains("aw")) word = word.replace("aw", "ă")
        if (word.contains("uw")) word = word.replace("uw", "ư")
        if (word.contains("ow")) word = word.replace("ow", "ơ")

        // Collapse extra modifier keystrokes (e.g. ddeeem -> đêem -> đêm)
        while (word.contains("êe")) word = word.replace("êe", "ê")
        while (word.contains("eê")) word = word.replace("eê", "ê")
        while (word.contains("ôo")) word = word.replace("ôo", "ô")
        while (word.contains("oô")) word = word.replace("oô", "ô")
        while (word.contains("âa")) word = word.replace("âa", "â")
        while (word.contains("aâ")) word = word.replace("aâ", "â")

        // 4. Delayed modifier vowel (gõ dấu cuối từ: homo -> hôm, tama -> tâm, deme -> đêm, songo -> sông)
        if (word.length >= 4) {
            val lastChar = word.last()
            if (lastChar == 'o' || lastChar == 'a' || lastChar == 'e') {
                val stem = word.dropLast(1)
                val lastVowelIdx = stem.indexOfLast { it == lastChar }
                if (lastVowelIdx != -1) {
                    val afterVowel = stem.substring(lastVowelIdx + 1)
                    val validMiddleConsonants = setOf("m", "n", "ng", "nh", "p", "t", "c", "ch")
                    if (validMiddleConsonants.contains(afterVowel)) {
                        val modifiedVowel = when (lastChar) {
                            'o' -> "ô"
                            'a' -> "â"
                            'e' -> "ê"
                            else -> lastChar.toString()
                        }
                        word = stem.substring(0, lastVowelIdx) + modifiedVowel + afterVowel
                    }
                }
            }
        }

        // 5. Handle 'w' acting as 'ư' after initial consonants or standalone
        if (word.contains("w")) {
            val sb = StringBuilder()
            var hasU = false
            var hasO = false
            for (c in word) {
                if (c == 'u') hasU = true
                if (c == 'o') hasO = true
            }
            if (!hasU && !hasO) {
                word = word.replace("w", "ư")
            } else {
                for (c in word) {
                    if (c == 'w') continue
                    if (c == 'u' && !word.contains("uo")) {
                        sb.append('ư')
                    } else if (c == 'o' && !word.contains("uo")) {
                        sb.append('ơ')
                    } else {
                        sb.append(c)
                    }
                }
                word = sb.toString()
                if (hasU && hasO) {
                    word = word.replace("uo", "ươ")
                }
            }
        }

        return word
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

    private fun isValidFlexibleSyllableStem(stem: String): Boolean {
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

    private fun isValidSingleVietnameseSyllableStem(stem: String): Boolean {
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

        val vowelIndices = mutableListOf<Int>()
        for (i in rest.indices) {
            if (isVowel(rest[i])) vowelIndices.add(i)
        }
        if (vowelIndices.isEmpty()) return false

        for (i in 0 until vowelIndices.size - 1) {
            val gap = vowelIndices[i + 1] - vowelIndices[i]
            if (gap > 1) {
                val sub = rest.substring(vowelIndices[i], vowelIndices[i + 1] + 1)
                if (sub != "gi" && sub != "qu" && !rest.startsWith("gi") && !rest.startsWith("qu")) {
                    return false
                }
            }
        }

        val lastVowelIdx = vowelIndices.last()
        val ending = rest.substring(lastVowelIdx + 1)
        if (ending.isNotEmpty()) {
            val validFinals = setOf("c", "ch", "m", "n", "ng", "nh", "p", "t")
            if (!validFinals.contains(ending)) return false
        }

        return true
    }

    private fun isEnglishWord(word: String): Boolean {
        val lower = word.lowercase()
        return isEnglishPrefix(lower) || hasEnglishConsonantCluster(lower)
    }

    private fun hasEnglishConsonantCluster(word: String): Boolean {
        val lower = word.lowercase()
        // Double non-tone letters
        if (lower.contains("bb") || lower.contains("cc") || lower.contains("gg") || 
            lower.contains("hh") || lower.contains("kk") || lower.contains("ll") || 
            lower.contains("mm") || lower.contains("nn") || lower.contains("pp") || 
            lower.contains("qq") || lower.contains("tt") || lower.contains("vv")) {
            return true
        }

        val englishClusters = listOf(
            "sc", "sk", "sp", "st", "sm", "sn",
            "fl", "fr", "gl", "gr", "pl", "pr", "cl", "cr", "dr", "br", "bl",
            "ct", "ft", "lt", "mp", "nd", "nt", "pt",
            "rk", "rd", "rm", "rn", "rt", "rg", "rb", "rv", "rs", "rc", "rf", "rp",
            "ld", "lk", "lm", "lp"
        )
        return englishClusters.any { lower.contains(it) }
    }

    private fun isEnglishPrefix(word: String): Boolean {
        val lower = word.lowercase()
        val englishPrefixes = listOf(
            "bl", "br", "cl", "cr", "dr", "fl", "fr", "gl", "gr", "pl", "pr",
            "sc", "sk", "sl", "sm", "sn", "sp", "st", "str", "sh", "wh",
            "esc", "esp", "est", "eng", "app", "int", "imp", "exp", "ext", "inc", "inf", "ins",
            "com", "con", "dis", "sub", "trans", "auto", "anti", "multi", "super", "micro", "inter"
        )
        return englishPrefixes.any { lower.startsWith(it) }
    }

    private fun isVowel(c: Char) = "aeiouyâêôăơưw".contains(c.lowercaseChar())

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
        val allVowelIndices = mutableListOf<Int>()
        for (i in normWord.indices) {
            if (isVowel(normWord[i])) allVowelIndices.add(i)
        }

        if (allVowelIndices.isEmpty()) return normWord

        // Extract the last contiguous cluster of vowels (final syllable's vowels)
        val vowelIndices = mutableListOf<Int>()
        vowelIndices.add(allVowelIndices.last())
        for (i in allVowelIndices.size - 2 downTo 0) {
            if (allVowelIndices[i] == allVowelIndices[i + 1] - 1) {
                vowelIndices.add(0, allVowelIndices[i])
            } else {
                break
            }
        }

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
