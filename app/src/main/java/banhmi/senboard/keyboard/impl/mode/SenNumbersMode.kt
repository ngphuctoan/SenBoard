package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.impl.layout.SenStandardLayout
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senCharKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senModeSwitcherKey
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSecondaryKeyStyle
import banhmi.senboard.keyboard.model.senSpaceKey
import banhmi.senboard.keyboard.model.senTextKey

val SenNumbersMode = senMode(SenStandardLayout) {
    // First row
    listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0').forEach { char -> senCharKey(char) }

    // Second row
    listOf("@", "#", "$", "%", "&", "+", "-", "(", ")").forEach { text -> senTextKey(text) }

    // Third row
    senModeSwitcherKey(SenModeType.ExtraCharacters, "=\\<", styleProvider = senSecondaryKeyStyle)
    listOf("*", "/", "_", ":", ";", "'", "\"").forEach { text -> senTextKey(text) }
    senBackSpaceKey()

    // Fourth row
    senModeSwitcherKey(SenModeType.Characters, "ABC")
    listOf(",", ".").forEach { text -> senTextKey(text, styleProvider = senSecondaryKeyStyle) }
    senSpaceKey()
    senTextKey("?", styleProvider = senSecondaryKeyStyle)
    senReturnKey()
}
