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
import banhmi.senboard.keyboard.model.senShiftKey
import banhmi.senboard.keyboard.model.senSpaceKey
import banhmi.senboard.keyboard.model.senTextKey

val senCharactersMode: SenModeProvider = { _, preferences ->
    senMode(SenLayoutType.Standard) {
        // First (extra) row (the number row)
        if (preferences.numberRowEnabled) {
            listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0').forEach { char ->
                senCharKey(char)
            }
        }

        // Second row
        listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p').forEach { char ->
            senCharKey(char)
        }

        // Third row
        listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l').forEach { char ->
            senCharKey(char)
        }

        // Fourth row
        senShiftKey()
        listOf('z', 'x', 'c', 'v', 'b', 'n', 'm').forEach { char ->
            senCharKey(char)
        }
        senBackSpaceKey()

        // Fifth row
        senModeSwitcherKey(SenModeType.Numbers, "123")
        listOf(",", ".").forEach { text ->
            senTextKey(text, styleProvider = senSecondaryContainerKeyStyle)
        }
        senSpaceKey()
        senTextKey("?", styleProvider = senSecondaryContainerKeyStyle)
        senReturnKey()
    }
}
