package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.model.SenLayoutType
import banhmi.senboard.keyboard.model.SenModeProvider
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senCharKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senModeSwitcherKey
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSecondaryContainerKeyStyle
import banhmi.senboard.keyboard.model.senSpaceKey
import banhmi.senboard.keyboard.model.senTextKey

val senExtraCharactersMode: SenModeProvider = { _, preferences ->
    senMode(SenLayoutType.Standard) {
        // First row
        listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0').forEach { char ->
            senCharKey(char)
        }

        // Second (extra) row (I am out of idea so here is an emoticon row for fun :b)
        if (preferences.numberRowEnabled) {
            listOf(":)", ":(", ":|", ":b", ":>", ":O", ":D", "D:", "^^", "._.").forEach { text ->
                senTextKey(text)
            }
        }

        // Third row
        listOf("[", "]", "{", "}", "~", "^", "=", "<", ">").forEach { text ->
            senTextKey(text)
        }

        // Fourth row
        senModeSwitcherKey(SenModeType.Numbers, "123", styleProvider = senSecondaryContainerKeyStyle)
        listOf("`", "•", "‣", "–", "—", "≤", "≥").forEach { text ->
            senTextKey(text)
        }
        senBackSpaceKey()

        // Fifth row
        senModeSwitcherKey(SenModeType.Characters, "ABC")
        listOf("\\", "|").forEach { text ->
            senTextKey(text, styleProvider = senSecondaryContainerKeyStyle)
        }
        senSpaceKey()
        senTextKey("!", styleProvider = senSecondaryContainerKeyStyle)
        senReturnKey()
    }
}
