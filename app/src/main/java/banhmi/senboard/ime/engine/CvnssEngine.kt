package banhmi.senboard.ime.engine

object CvnssEngine {

    // Base 56 shortened finals map (from CVNSS 4.0 specification)
    private val vần56 = mapOf(
        "yd" to "uyêt", "yl" to "uyên",
        "id" to "iêt", "if" to "iêp", "is" to "iêc", "il" to "iên", "iv" to "iêm", "iz" to "iêng", "iw" to "iêu",
        "ud" to "uôt", "us" to "uôc", "ul" to "uôn", "uv" to "uôm", "uz" to "uông", "uj" to "uôi",
        "ưd" to "ươt", "ưf" to "ươp", "ưs" to "ươc", "ưl" to "ươn", "ưv" to "ươm", "ưz" to "ương", "ưw" to "ươu", "ưj" to "ươi",
        "âd" to "uât", "âl" to "uân", "âz" to "uâng", "âj" to "uây",
        "ơd" to "uơt", "ơl" to "uơn", "ơj" to "uơi",
        "ăd" to "oăt", "ăf" to "oăp", "ăs" to "oăc", "ăl" to "oăn", "ăv" to "oăm", "ăz" to "oăng",
        "ed" to "oet", "es" to "oec", "el" to "oen", "ev" to "oem", "ez" to "oeng", "ew" to "oeo",
        "od" to "oat", "of" to "oap", "os" to "oac", "ol" to "oan", "ov" to "oam", "oz" to "oang", "ow" to "oao", "oj" to "oai", "aj" to "oay"
    )

    fun convertWord(rawWord: String): String {
        if (rawWord.isEmpty()) return rawWord

        var word = rawWord.lowercase()

        // 1. Check Silent Disambiguation 'P' Suffix
        if (word.endsWith("p") && word.length >= 3) {
            val stem = word.dropLast(1)
            if (stem.endsWith("ag") || stem.endsWith("ah") || stem.endsWith("aj") ||
                stem.endsWith("eg") || stem.endsWith("el") || stem.endsWith("ev") ||
                stem.endsWith("ew") || stem.endsWith("ez") || stem.endsWith("ih") ||
                stem.endsWith("oah") || stem.endsWith("og") || stem.endsWith("oj") ||
                stem.endsWith("ol") || stem.endsWith("ov") || stem.endsWith("ow") ||
                stem.endsWith("oz") || stem.endsWith("ug") || stem.endsWith("yh")
            ) {
                word = stem
            }
        }

        // 2. Identify Tone Symbol (KHD) from Last Character
        var tone = "" // sac, huyen, hoi, nga, nang, none
        var hatGroup = false // â, ê, ô (Group 1: B, D, Q, G, F, Y)
        var moonGroup = false // ă, ơ, ư (Group 2: X, K, V, W, H, O)
        var plainGroup = false // a, e, i, o, u, y (Group 3: J, L, Z, S, R)

        val lastChar = word.last()
        var hasKhd = true

        when (lastChar) {
            // Group 1: Dấu Nón (â, ê, ô)
            'b' -> { tone = "sac"; hatGroup = true }
            'd' -> { tone = "huyen"; hatGroup = true }
            'q' -> { tone = "hoi"; hatGroup = true }
            'g' -> { tone = "nga"; hatGroup = true }
            'f' -> { tone = "nang"; hatGroup = true }
            'y' -> { tone = ""; hatGroup = true }

            // Group 2: Dấu Trăng / Dấu Móc (ă, ơ, ư)
            'x' -> { tone = "sac"; moonGroup = true }
            'k' -> { tone = "huyen"; moonGroup = true }
            'v' -> { tone = "hoi"; moonGroup = true }
            'w' -> { tone = "nga"; moonGroup = true }
            'h' -> { tone = "nang"; moonGroup = true }
            'o' -> { tone = ""; moonGroup = true }

            // Group 3: Không Dấu Phụ (a, e, i, o, u, y)
            'j' -> { tone = "sac"; plainGroup = true }
            'l' -> { tone = "huyen"; plainGroup = true }
            'z' -> { tone = "hoi"; plainGroup = true }
            's' -> { tone = "nga"; plainGroup = true }
            'r' -> { tone = "nang"; plainGroup = true }

            else -> {
                hasKhd = false
            }
        }

        val stem = if (hasKhd) word.dropLast(1) else word

        // 3. Extract Initial Consonant (Phụ âm đầu)
        var initial = ""
        var rest = stem

        if (rest.startsWith("ph") || rest.startsWith("qu") || rest.startsWith("kh") ||
            rest.startsWith("gi") || rest.startsWith("gh") || rest.startsWith("ngh") ||
            rest.startsWith("ng") || rest.startsWith("nh") || rest.startsWith("ch") ||
            rest.startsWith("tr") || rest.startsWith("th")
        ) {
            val len = if (rest.startsWith("ngh")) 3 else 2
            initial = rest.substring(0, len)
            rest = rest.substring(len)
        } else if (rest.isNotEmpty()) {
            val c0 = rest[0]
            val nextChar = if (rest.length > 1) rest[1] else ' '

            when (c0) {
                'f' -> { initial = "ph"; rest = rest.substring(1) }
                'q' -> { initial = "qu"; rest = rest.substring(1) }
                'k' -> { initial = "kh"; rest = rest.substring(1) }
                'z' -> { initial = "d"; rest = rest.substring(1) }
                'd' -> { initial = "đ"; rest = rest.substring(1) }
                'j' -> { initial = "gi"; rest = rest.substring(1) }
                'g' -> {
                    if (isFrontVowel(nextChar)) initial = "gh" else initial = "g"
                    rest = rest.substring(1)
                }
                'c' -> {
                    if (isFrontVowel(nextChar)) initial = "k" else initial = "c"
                    rest = rest.substring(1)
                }
                'w' -> {
                    if (isFrontVowel(nextChar)) initial = "ngh" else initial = "ng"
                    rest = rest.substring(1)
                }
                else -> {
                    if (!isVowel(c0)) {
                        initial = c0.toString()
                        rest = rest.substring(1)
                    }
                }
            }
        }

        // 4. Expand Vowels & Finals according to KHD Group (Group 2 Móc/Trăng vs Group 1 Nón vs Base 56)
        var expandedVowel = rest

        if (moonGroup) {
            // Group 2 (Trăng/Móc): transform plain 'u...' finals to 'ươ...' and plain 'a/o/u' to 'ă/ơ/ư'
            when (rest) {
                "ud" -> expandedVowel = "ươt"
                "us" -> expandedVowel = "ươc"
                "ul" -> expandedVowel = "ươn"
                "uv" -> expandedVowel = "ươm"
                "uz" -> expandedVowel = "ương"
                "uj" -> expandedVowel = "ươi"
                "uw" -> expandedVowel = "ươu"
                "ad" -> expandedVowel = "oăt"
                "af" -> expandedVowel = "oăp"
                "as" -> expandedVowel = "oăc"
                "al" -> expandedVowel = "oăn"
                "av" -> expandedVowel = "oăm"
                "az" -> expandedVowel = "oăng"
                else -> {
                    val matched = vần56[rest]
                    if (matched != null) {
                        expandedVowel = matched
                    } else {
                        expandedVowel = applyMoonToVowel(rest)
                    }
                }
            }
        } else if (hatGroup) {
            // Group 1 (Nón): transform plain vowels to â/ê/ô
            val matched = vần56[rest]
            if (matched != null) {
                expandedVowel = matched
            } else {
                expandedVowel = applyHatToVowel(rest)
            }
        } else {
            // Standard CVN 56 finals or basic final consonant rules
            val matched = vần56[rest]
            if (matched != null) {
                expandedVowel = matched
            } else {
                if (rest.endsWith("oah")) expandedVowel = rest.dropLast(3) + "oanh"
                else if (rest.endsWith("oak")) expandedVowel = rest.dropLast(3) + "oach"
                else if (rest.endsWith("uêh")) expandedVowel = rest.dropLast(3) + "uênh"
                else if (rest.endsWith("uêk")) expandedVowel = rest.dropLast(3) + "uêch"
                else if (rest.endsWith("g") && !rest.endsWith("ng")) expandedVowel = rest.dropLast(1) + "ng"
                else if (rest.endsWith("h") && !rest.endsWith("nh")) expandedVowel = rest.dropLast(1) + "nh"
                else if (rest.endsWith("k") && !rest.endsWith("ch")) expandedVowel = rest.dropLast(1) + "ch"
            }
        }

        // Rule A.1: Auto add sac tone to c, p, t if no KHD specified and ends with c, p, t
        if (!hasKhd && (expandedVowel.endsWith("c") || expandedVowel.endsWith("p") || expandedVowel.endsWith("t"))) {
            tone = "sac"
        }

        var fullWord = initial + expandedVowel

        // Rule: If no initial consonant, iê... at start of word becomes yê... in CQN (e.g. idb -> yết, ily -> yên)
        if (initial.isEmpty() && fullWord.startsWith("iê")) {
            fullWord = "yê" + fullWord.substring(2)
        }

        // 5. Apply Tone Accent (sắc, huyền, hỏi, ngã, nặng)
        if (tone.isNotEmpty()) {
            fullWord = applyToneMark(fullWord, tone)
        }

        // Restore capitalization
        if (rawWord.firstOrNull()?.isUpperCase() == true) {
            fullWord = fullWord.capitalizeFirstLetter()
        }

        return fullWord
    }

    private fun isFrontVowel(c: Char) = "ieêìíỉĩịèéẻẽẹềếểễệ".contains(c.lowercaseChar())
    private fun isVowel(c: Char) = "aeiouyâêôăơư".contains(c.lowercaseChar())

    private fun applyHatToVowel(vowel: String): String {
        return vowel.replace('a', 'â').replace('e', 'ê').replace('o', 'ô')
    }

    private fun applyMoonToVowel(vowel: String): String {
        return vowel.replace('a', 'ă').replace('o', 'ơ').replace('u', 'ư')
    }

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

        val vowelIndices = mutableListOf<Int>()
        for (i in word.indices) {
            if (isVowel(word[i])) vowelIndices.add(i)
        }

        if (vowelIndices.isEmpty()) return word

        var targetIndex = vowelIndices.last()

        // Primary vowels that ALWAYS take priority for tone marks (ơ, ê, ô, â, ă, e)
        val primaryVowels = "êơôâăe"
        var foundPrimary = false

        for (idx in vowelIndices) {
            if (primaryVowels.contains(word[idx])) {
                targetIndex = idx
                foundPrimary = true
                break
            }
        }

        if (!foundPrimary && vowelIndices.size >= 2) {
            // If word ends with consonant (c, p, t, n, m, ng, nh, ch), last vowel gets tone mark (e.g. toán, khoảng, hoặc)
            if (word.endsWith("c") || word.endsWith("p") || word.endsWith("t") || word.endsWith("n") || word.endsWith("m") || word.endsWith("ng") || word.endsWith("nh") || word.endsWith("ch")) {
                targetIndex = vowelIndices[vowelIndices.size - 1]
            } else {
                // Open syllable diphthong (e.g. hoài, xoài, hoay, quỷ, tùy)
                targetIndex = vowelIndices[vowelIndices.size - 2]
            }
        }

        val targetChar = word[targetIndex]
        val markedChar = toneMap[targetChar]?.get(tone) ?: targetChar

        return word.substring(0, targetIndex) + markedChar + word.substring(targetIndex + 1)
    }

    private fun String.capitalizeFirstLetter(): String =
        if (isNotEmpty()) this[0].uppercaseChar() + substring(1) else this
}
