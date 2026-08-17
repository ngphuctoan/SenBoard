package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.impl.layout.SenStandardLayout
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senCharKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senModeSwitcherKey
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSecondaryKeyStyle
import banhmi.senboard.keyboard.model.senShiftKey
import banhmi.senboard.keyboard.model.senSpaceKey
import banhmi.senboard.keyboard.model.senTextKey

val SenCharactersMode = senMode(SenStandardLayout) {
    // First row
    listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p').forEach { char -> senCharKey(char) }

    // Second row
    listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l').forEach { char -> senCharKey(char) }

    // Third row
    senShiftKey()
    listOf('z', 'x', 'c', 'v', 'b', 'n', 'm').forEach { char -> senCharKey(char) }
    senBackSpaceKey()

    // Fourth row
    senModeSwitcherKey(SenModeType.Numbers, "123")
    listOf(",", ".").forEach { text -> senTextKey(text, styleProvider = senSecondaryKeyStyle) }
    senSpaceKey()
    senTextKey("?", styleProvider = senSecondaryKeyStyle)
    senReturnKey()
}
