package banhmi.senboard.engine.impl

import banhmi.senboard.engine.VietnameseEngine
import java.text.Normalizer

object CvnssEngine : VietnameseEngine {

    // Base 56 shortened finals map (from CVNSS 4.0 specification)
    private val vần56 = mapOf(
        "yd" to "uyêt",
        "yl" to "uyên",
        "id" to "iêt",
        "if" to "iêp",
        "is" to "iêc",
        "il" to "iên",
        "iv" to "iêm",
        "iz" to "iêng",
        "iw" to "iêu",
        "ud" to "uôt",
        "us" to "uôc",
        "ul" to "uôn",
        "uv" to "uôm",
        "uz" to "uông",
        "uj" to "uôi",
        "ưd" to "ươt",
        "ưf" to "ươp",
        "ưs" to "ươc",
        "ưl" to "ươn",
        "ưv" to "ươm",
        "ưz" to "ương",
        "ưw" to "ươu",
        "ưj" to "ươi",
        "âd" to "uât",
        "âl" to "uân",
        "âz" to "uâng",
        "âj" to "uây",
        "ơd" to "uơt",
        "ơl" to "uơn",
        "ơj" to "uơi",
        "ăd" to "oăt",
        "ăf" to "oăp",
        "ăs" to "oăc",
        "ăl" to "oăn",
        "ăv" to "oăm",
        "ăz" to "oăng",
        "ed" to "oet",
        "es" to "oec",
        "el" to "oen",
        "ev" to "oem",
        "ez" to "oeng",
        "ew" to "oeo",
        "od" to "oat",
        "of" to "oap",
        "os" to "oac",
        "ol" to "oan",
        "ov" to "oam",
        "oz" to "oang",
        "ow" to "oao",
        "oj" to "oai",
        "aj" to "oay"
    )

    private val reverse56 = mapOf(
        "uyêt" to "yd",
        "uyên" to "yl",
        "iêt" to "id",
        "iêp" to "if",
        "iêc" to "is",
        "iên" to "il",
        "iêm" to "iv",
        "iêng" to "iz",
        "iêu" to "iw",
        "yêt" to "id",
        "yêp" to "if",
        "yêc" to "is",
        "yên" to "il",
        "yêm" to "iv",
        "yêng" to "iz",
        "yêu" to "iw",
        "uôt" to "ud",
        "uôc" to "us",
        "uôn" to "ul",
        "uôm" to "uv",
        "uông" to "uz",
        "uôi" to "uj",
        "ươt" to "ud",
        "ươp" to "uf",
        "ươc" to "us",
        "ươn" to "ul",
        "ươm" to "uv",
        "ương" to "uz",
        "ươu" to "uw",
        "ươi" to "uj",
        "uât" to "ad",
        "uân" to "al",
        "uâng" to "az",
        "uây" to "aj",
        "uơt" to "od",
        "uơn" to "ol",
        "uơi" to "oj",
        "oăt" to "ad",
        "oăp" to "af",
        "oăc" to "as",
        "oăn" to "al",
        "oăm" to "av",
        "oăng" to "az",
        "oet" to "ed",
        "oec" to "es",
        "oen" to "el",
        "oem" to "ev",
        "oeng" to "ez",
        "oeo" to "ew",
        "oat" to "od",
        "oap" to "of",
        "oac" to "os",
        "oan" to "ol",
        "oam" to "ov",
        "oang" to "oz",
        "oao" to "ow",
        "oai" to "oj",
        "oay" to "aj"
    )

    override fun convertWord(rawWord: String): String {
        if (rawWord.isEmpty()) return rawWord

        var word = rawWord.lowercase()
        var forceNoKhd = false

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
                forceNoKhd = true
            }
        }

        // 2. Identify Tone Symbol (KHD) from Last Character
        var tone = "" // sac, huyen, hoi, nga, nang, none
        var hatGroup = false // â, ê, ô (Group 1: B, D, Q, G, F, Y)
        var moonGroup = false // ă, ơ, ư (Group 2: X, K, V, W, H, O)
        var plainGroup = false // a, e, i, o, u, y (Group 3: J, L, Z, S, R)

        val lastChar = word.last()
        val stemCandidate = word.dropLast(1)
        val hasVowelInStem = stemCandidate.any { isVowel(it) || it.lowercaseChar() == 'j' }
        var hasKhd = if (forceNoKhd) false else hasVowelInStem

        if (hasKhd) {
            when (lastChar) {
                // Group 1: Dấu Nón (â, ê, ô)
                'b' -> {
                    tone = "sac"; hatGroup = true
                }

                'd' -> {
                    tone = "huyen"; hatGroup = true
                }

                'q' -> {
                    tone = "hoi"; hatGroup = true
                }

                'g' -> {
                    tone = "nga"; hatGroup = true
                }

                'f' -> {
                    tone = "nang"; hatGroup = true
                }

                'y' -> {
                    if (word == "y" || word == "ty" || word == "qy" || word.endsWith("ay") || word.endsWith(
                            "ây"
                        )
                    ) {
                        hasKhd = false
                    } else {
                        tone = ""; hatGroup = true
                    }
                }

                // Group 2: Dấu Trăng / Dấu Móc (ă, ơ, ư)
                'x' -> {
                    tone = "sac"; moonGroup = true
                }

                'k' -> {
                    tone = "huyen"; moonGroup = true
                }

                'v' -> {
                    tone = "hoi"; moonGroup = true
                }

                'w' -> {
                    tone = "nga"; moonGroup = true
                }

                'h' -> {
                    tone = "nang"; moonGroup = true
                }

                'o' -> {
                    val stemBeforeO = word.dropLast(1)
                    val plainWords = setOf(
                        "cho",
                        "do",
                        "co",
                        "to",
                        "ho",
                        "lo",
                        "mo",
                        "no",
                        "po",
                        "ro",
                        "so",
                        "bo",
                        "go",
                        "tho",
                        "kho",
                        "nho",
                        "trho",
                        "theo",
                        "zo",
                        "qo"
                    )
                    if (plainWords.contains(word) || stemBeforeO.isEmpty() || (word.endsWith("ao") && !word.endsWith(
                            "uao"
                        )) || word.endsWith("eo")
                    ) {
                        hasKhd = false
                    } else {
                        tone = ""; moonGroup = true
                    }
                }

                // Group 3: Không Dấu Phụ (a, e, i, o, u, y)
                'j' -> {
                    tone = "sac"; plainGroup = true
                }

                'l' -> {
                    tone = "huyen"; plainGroup = true
                }

                'z' -> {
                    tone = "hoi"; plainGroup = true
                }

                's' -> {
                    tone = "nga"; plainGroup = true
                }

                'r' -> {
                    tone = "nang"; plainGroup = true
                }

                else -> {
                    hasKhd = false
                }
            }
        }

        val stem = if (hasKhd) word.dropLast(1) else word

        // 3. Extract CVNSS Initial Consonant symbol
        var cvnssInitial = ""
        var rest = stem

        if (rest.startsWith("ph") || rest.startsWith("qu") || rest.startsWith("kh") ||
            rest.startsWith("gh") || rest.startsWith("ngh") ||
            rest.startsWith("ng") || rest.startsWith("nh") || rest.startsWith("ch") ||
            rest.startsWith("tr") || rest.startsWith("th")
        ) {
            val len = if (rest.startsWith("ngh")) 3 else 2
            cvnssInitial = rest.substring(0, len)
            rest = rest.substring(len)
        } else if (rest.isNotEmpty() && !isVowel(rest[0])) {
            cvnssInitial = rest[0].toString()
            rest = rest.substring(1)
        }

        // 4. Expand Vowels & Finals according to KHD Group
        var expandedVowel = rest

        if (moonGroup) {
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
                "aj" -> expandedVowel = "oay"
                "ua" -> expandedVowel = "ưa"
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
            when (rest) {
                "ad" -> expandedVowel = "uât"
                "af" -> expandedVowel = "uâp"
                "as" -> expandedVowel = "uâc"
                "al" -> expandedVowel = "uân"
                "av" -> expandedVowel = "uâm"
                "az" -> expandedVowel = "uâng"
                "aj" -> expandedVowel = "uây"
                else -> {
                    val matched = vần56[rest]
                    if (matched != null) {
                        expandedVowel = matched
                    } else {
                        expandedVowel = applyHatToVowel(rest)
                    }
                }
            }
        } else {
            val matched = vần56[rest]
            if (matched != null) {
                expandedVowel = matched
            } else {
                if (rest.endsWith("oah")) expandedVowel = rest.dropLast(3) + "oanh"
                else if (rest.endsWith("oak")) expandedVowel = rest.dropLast(3) + "oach"
                else if (rest.endsWith("uêh")) expandedVowel = rest.dropLast(3) + "uênh"
                else if (rest.endsWith("uêk")) expandedVowel = rest.dropLast(3) + "uêch"
                else if (rest.endsWith("g") && !rest.endsWith("ng")) expandedVowel =
                    rest.dropLast(1) + "ng"
                else if (rest.endsWith("h") && !rest.endsWith("nh")) expandedVowel =
                    rest.dropLast(1) + "nh"
                else if (rest.endsWith("k") && !rest.endsWith("ch")) expandedVowel =
                    rest.dropLast(1) + "ch"
            }
        }

        // Apply final consonant replacements for hat/moon groups (e.g. og -> ong, ah -> anh)
        if (hatGroup || moonGroup) {
            if (expandedVowel.endsWith("g") && !expandedVowel.endsWith("ng")) expandedVowel =
                expandedVowel.dropLast(1) + "ng"
            else if (expandedVowel.endsWith("h") && !expandedVowel.endsWith("nh")) expandedVowel =
                expandedVowel.dropLast(1) + "nh"
            else if (expandedVowel.endsWith("k") && !expandedVowel.endsWith("ch")) expandedVowel =
                expandedVowel.dropLast(1) + "ch"
        }

        // Rule A.1: Auto add sac tone to c, p, t if no KHD specified and ends with c, p, t
        if (!hasKhd && (expandedVowel.endsWith("c") || expandedVowel.endsWith("p") || expandedVowel.endsWith(
                "t"
            ))
        ) {
            tone = "sac"
        }

        // 5. Map CQN Initial consonant based on expandedVowel spelling rules!
        val nextChar = expandedVowel.firstOrNull() ?: ' '
        val initial = when (cvnssInitial) {
            "f" -> "ph"
            "q" -> "qu"
            "k" -> "kh"
            "z" -> "d"
            "d" -> "đ"
            "j" -> "gi"
            "g" -> if (isFrontVowel(nextChar)) "gh" else "g"
            "c" -> if (isFrontVowel(nextChar)) "k" else "c"
            "w" -> if (isFrontVowel(nextChar)) "ngh" else "ng"
            else -> cvnssInitial
        }

        val expandedVowelFixed = if (expandedVowel == "y") "uy" else expandedVowel
        var fullWord = initial + expandedVowelFixed

        if (initial.isEmpty()) {
            if (fullWord == "i") {
                fullWord = "y"
            } else if (fullWord.startsWith("iê")) {
                fullWord = "yê" + fullWord.substring(2)
            }
        }
        // Rule: If initial is qu and vowel starts with iê, change to yê (e.g. qidb -> quyết, qild -> quyền)
        if (initial == "qu" && fullWord.startsWith("quiê")) {
            fullWord = "quyê" + fullWord.substring(4)
        }

        // 6. Apply Tone Accent (sắc, huyền, hỏi, ngã, nặng)
        if (tone.isNotEmpty()) {
            fullWord = applyToneMark(fullWord, tone)
        }

        // Restore capitalization
        val lettersOnly = rawWord.filter { it.isLetter() }
        if (lettersOnly.isNotEmpty() && lettersOnly.all { it.isUpperCase() }) {
            fullWord = fullWord.uppercase()
        } else if (rawWord
                .firstOrNull()
                ?.isUpperCase() == true
        ) {
            fullWord = fullWord.capitalizeFirstLetter()
        }

        return fullWord
    }

    fun encodeWord(cqnWord: String): String {
        if (cqnWord.isEmpty()) return cqnWord

        var text = cqnWord.lowercase()
        val (plainText, tone) = extractTone(text)

        val hasHat = plainText.contains('â') || plainText.contains('ê') || plainText.contains('ô')
        val hasMoon = plainText.contains('ă') || plainText.contains('ơ') || plainText.contains('ư')

        // 1. Initial consonant transformation
        var initial = ""
        var rest = plainText

        if (rest.startsWith("ngh")) {
            initial = "w"; rest = rest.substring(3)
        } else if (rest.startsWith("ng")) {
            initial = "w"; rest = rest.substring(2)
        } else if (rest.startsWith("ph")) {
            initial = "f"; rest = rest.substring(2)
        } else if (rest.startsWith("qu")) {
            initial = "q"; rest = rest.substring(2)
        } else if (rest.startsWith("kh")) {
            initial = "k"; rest = rest.substring(2)
        } else if (rest.startsWith("gi")) {
            initial = "j"; rest = rest.substring(2)
        } else if (rest.startsWith("gh")) {
            initial = "g"; rest = rest.substring(2)
        } else if (rest.startsWith("nh") || rest.startsWith("ch") || rest.startsWith("th") || rest.startsWith(
                "tr"
            )
        ) {
            val len = 2
            initial = rest.substring(0, len)
            rest = rest.substring(len)
        } else if (rest.isNotEmpty()) {
            val c0 = rest[0]
            val nextChar = if (rest.length > 1) rest[1] else ' '
            when (c0) {
                'đ' -> {
                    initial = "d"; rest = rest.substring(1)
                }

                'd' -> {
                    initial = "z"; rest = rest.substring(1)
                }

                'k' -> {
                    if (isFrontVowel(nextChar)) initial = "c" else initial = "k"
                    rest = rest.substring(1)
                }

                'c' -> {
                    if (isFrontVowel(nextChar)) initial = "k" else initial = "c"
                    rest = rest.substring(1)
                }

                else -> {
                    if (!isVowel(c0)) {
                        initial = c0.toString(); rest = rest.substring(1)
                    }
                }
            }
        }

        if (initial.isEmpty() && rest.startsWith("yê")) {
            rest = "iê" + rest.substring(2)
        }

        var shorthand = ""
        val match56 = reverse56[rest]
        if (match56 != null) {
            shorthand = match56
        } else {
            val restFixed = if (rest.contains("uy")) rest.replace("uy", "y") else rest
            if (restFixed.endsWith("oanh")) shorthand = restFixed.dropLast(4) + "oah"
            else if (restFixed.endsWith("oach")) shorthand = restFixed.dropLast(4) + "oak"
            else if (restFixed.endsWith("uênh")) shorthand = restFixed.dropLast(4) + "uêh"
            else if (restFixed.endsWith("uêch")) shorthand = restFixed.dropLast(4) + "uêk"
            else if (restFixed.endsWith("ng")) shorthand = restFixed.dropLast(2) + "g"
            else if (restFixed.endsWith("nh")) shorthand = restFixed.dropLast(2) + "h"
            else if (restFixed.endsWith("ch")) shorthand = restFixed.dropLast(2) + "k"
            else shorthand = restFixed
        }

        // Strip remaining diacritics in shorthand (â,ê,ô,ă,ơ,ư -> a,e,o,a,o,u)
        shorthand = shorthand
            .replace('â', 'a')
            .replace('ê', 'e')
            .replace('ô', 'o')
            .replace('ă', 'a')
            .replace('ơ', 'o')
            .replace('ư', 'u')

        // 3. Determine KHD suffix
        var khd = ""
        if (hasHat) {
            khd = when (tone) {
                "sac" -> "b"
                "huyen" -> "d"
                "hoi" -> "q"
                "nga" -> "g"
                "nang" -> "f"
                else -> "y"
            }
        } else if (hasMoon) {
            khd = when (tone) {
                "sac" -> "x"
                "huyen" -> "k"
                "hoi" -> "v"
                "nga" -> "w"
                "nang" -> "h"
                else -> "o"
            }
        } else {
            khd = when (tone) {
                "sac" -> if (shorthand.endsWith("c") || shorthand.endsWith("p") || shorthand.endsWith(
                        "t"
                    )
                ) "" else "j"

                "huyen" -> "l"
                "hoi" -> "z"
                "nga" -> "s"
                "nang" -> "r"
                else -> ""
            }
        }

        var code = initial + shorthand + khd

        // Check silent P
        if (khd.isEmpty() && !hasHat && !hasMoon) {
            if (code.endsWith("ag") || code.endsWith("ah") || code.endsWith("aj") ||
                code.endsWith("eg") || code.endsWith("el") || code.endsWith("ev") ||
                code.endsWith("ew") || code.endsWith("ez") || code.endsWith("ih") ||
                code.endsWith("oah") || code.endsWith("og") || code.endsWith("oj") ||
                code.endsWith("ol") || code.endsWith("ov") || code.endsWith("ow") ||
                code.endsWith("oz") || code.endsWith("ug") || code.endsWith("yh")
            ) {
                code += "p"
            }
        }

        if (cqnWord
                .firstOrNull()
                ?.isUpperCase() == true
        ) {
            code = code.capitalizeFirstLetter()
        }

        return code
    }

    private fun extractTone(word: String): Pair<String, String> {
        var tone = ""
        val sb = StringBuilder()
        val accentMap = mapOf(
            'á' to Pair('a', "sac"),
            'à' to Pair('a', "huyen"),
            'ả' to Pair('a', "hoi"),
            'ã' to Pair('a', "nga"),
            'ạ' to Pair('a', "nang"),
            'ấ' to Pair('â', "sac"),
            'ầ' to Pair('â', "huyen"),
            'ẩ' to Pair('â', "hoi"),
            'ẫ' to Pair('â', "nga"),
            'ậ' to Pair('â', "nang"),
            'ắ' to Pair('ă', "sac"),
            'ằ' to Pair('ă', "huyen"),
            'ẳ' to Pair('ă', "hoi"),
            'ẵ' to Pair('ă', "nga"),
            'ặ' to Pair('ă', "nang"),
            'é' to Pair('e', "sac"),
            'è' to Pair('e', "huyen"),
            'ẻ' to Pair('e', "hoi"),
            'ẽ' to Pair('e', "nga"),
            'ẹ' to Pair('e', "nang"),
            'ế' to Pair('ê', "sac"),
            'ề' to Pair('ê', "huyen"),
            'ể' to Pair('ê', "hoi"),
            'ễ' to Pair('ê', "nga"),
            'ệ' to Pair('ê', "nang"),
            'í' to Pair('i', "sac"),
            'ì' to Pair('i', "huyen"),
            'ỉ' to Pair('i', "hoi"),
            'ĩ' to Pair('i', "nga"),
            'ị' to Pair('i', "nang"),
            'ó' to Pair('o', "sac"),
            'ò' to Pair('o', "huyen"),
            'ỏ' to Pair('o', "hoi"),
            'õ' to Pair('o', "nga"),
            'ọ' to Pair('o', "nang"),
            'ố' to Pair('ô', "sac"),
            'ồ' to Pair('ô', "huyen"),
            'ổ' to Pair('ô', "hoi"),
            'ỗ' to Pair('ô', "nga"),
            'ộ' to Pair('ô', "nang"),
            'ớ' to Pair('ơ', "sac"),
            'ờ' to Pair('ơ', "huyen"),
            'ở' to Pair('ơ', "hoi"),
            'ỡ' to Pair('ơ', "nga"),
            'ợ' to Pair('ơ', "nang"),
            'ú' to Pair('u', "sac"),
            'ù' to Pair('u', "huyen"),
            'ủ' to Pair('u', "hoi"),
            'ũ' to Pair('u', "nga"),
            'ụ' to Pair('u', "nang"),
            'ứ' to Pair('ư', "sac"),
            'ừ' to Pair('ư', "huyen"),
            'ử' to Pair('ư', "hoi"),
            'ữ' to Pair('ư', "nga"),
            'ự' to Pair('ư', "nang"),
            'ý' to Pair('y', "sac"),
            'ỳ' to Pair('y', "huyen"),
            'ỷ' to Pair('y', "hoi"),
            'ỹ' to Pair('y', "nga"),
            'ỵ' to Pair('y', "nang")
        )

        for (c in word) {
            val mapped = accentMap[c]
            if (mapped != null) {
                sb.append(mapped.first)
                tone = mapped.second
            } else {
                sb.append(c)
            }
        }
        return Pair(sb.toString(), tone)
    }

    private fun isFrontVowel(c: Char) = "ieêìíỉĩịèéẻẽẹềếểễệ".contains(c.lowercaseChar())
    private fun isVowel(c: Char) = "aeiouyâêôăơư".contains(c.lowercaseChar())

    private fun applyHatToVowel(vowel: String): String {
        var v = vowel
        if (v.contains("au")) {
            v = v.replace("au", "âu")
        } else if (v.contains("eu")) {
            v = v.replace("eu", "êu")
        } else {
            v = v
                .replace('a', 'â')
                .replace('e', 'ê')
                .replace('o', 'ô')
        }
        return v
    }

    private fun applyMoonToVowel(vowel: String): String {
        var v = vowel
        if (v.contains("uu")) {
            v = v.replace("uu", "ưu")
        } else {
            v = v.replace('u', 'ư')
        }
        return v
            .replace('a', 'ă')
            .replace('o', 'ơ')
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

        // Ensure word is normalized to NFC before processing
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
                return normWord.substring(
                    0,
                    targetIdx
                ) + markedChar + normWord.substring(targetIdx + 1)
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
                return normWord.substring(
                    0,
                    targetIdx
                ) + markedChar + normWord.substring(targetIdx + 1)
            }
        }

        var targetIndex = vowelIndices.last()

        // Primary vowels that ALWAYS take priority for tone marks (ơ, ê, ô, â, ă, e)
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
            // If word ends with consonant (c, p, t, n, m, ng, nh, ch), last vowel gets tone mark (e.g. toán, khoảng, hoặc)
            if (normWord.endsWith("c") || normWord.endsWith("p") || normWord.endsWith("t") || normWord.endsWith(
                    "n"
                ) || normWord.endsWith("m") || normWord.endsWith("ng") || normWord.endsWith("nh") || normWord.endsWith(
                    "ch"
                )
            ) {
                targetIndex = vowelIndices[vowelIndices.size - 1]
            } else {
                // Open syllable diphthong (e.g. hoài, xoài, hoay, quỷ, tùy)
                val vowelStr = vowelIndices
                    .map { normWord[it] }
                    .joinToString("")
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
