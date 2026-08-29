package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.impl.layout.SenStandardLayout
import banhmi.senboard.keyboard.impl.layout.SenStandardLayoutWithNumberRow
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.senAltCharKey
import banhmi.senboard.keyboard.model.senAltPopup
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
    senCharKey('q') {
        senAltPopup {
            senAltRow {
                senAltCharKey('1', anchor = true)
            }
        }
    }
    senCharKey('w') {
        senAltPopup {
            senAltRow {
                senAltCharKey('2', anchor = true)
            }
        }
    }
    senCharKey('e') {
        senAltPopup {
            senAltRow {
                senAltCharKey('ê', anchor = true)
                senAltCharKey('ế')
                senAltCharKey('ề')
                senAltCharKey('ể')
                senAltCharKey('ễ')
                senAltCharKey('ệ')
            }
            senAltRow {
                senAltCharKey('3')
                senAltCharKey('é')
                senAltCharKey('è')
                senAltCharKey('ẻ')
                senAltCharKey('ẽ')
                senAltCharKey('ẹ')
            }
        }
    }
    listOf('r', 't', 'y', 'u', 'i', 'o', 'p').forEach { char -> senCharKey(char) }

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

val SenCharactersModeWithNumberRow = senMode(SenStandardLayoutWithNumberRow) {
    // Number Row (Row 0) - matches letter key style & color
    listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0').forEach { char ->
        senCharKey(char)
    }

    // First letter row (Row 1)
    senCharKey('q') {
        senAltPopup {
            senAltRow {
                senAltCharKey('1', anchor = true)
            }
        }
    }
    senCharKey('w') {
        senAltPopup {
            senAltRow {
                senAltCharKey('2', anchor = true)
            }
        }
    }
    senCharKey('e') {
        senAltPopup {
            senAltRow {
                senAltCharKey('ê', anchor = true)
                senAltCharKey('ế')
                senAltCharKey('ề')
                senAltCharKey('ể')
                senAltCharKey('ễ')
                senAltCharKey('ệ')
            }
            senAltRow {
                senAltCharKey('3')
                senAltCharKey('é')
                senAltCharKey('è')
                senAltCharKey('ẻ')
                senAltCharKey('ẽ')
                senAltCharKey('ẹ')
            }
        }
    }
    listOf('r', 't', 'y', 'u', 'i', 'o', 'p').forEach { char -> senCharKey(char) }

    // Second row (Row 2)
    listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l').forEach { char -> senCharKey(char) }

    // Third row (Row 3)
    senShiftKey()
    listOf('z', 'x', 'c', 'v', 'b', 'n', 'm').forEach { char -> senCharKey(char) }
    senBackSpaceKey()

    // Fourth row (Row 4)
    senModeSwitcherKey(SenModeType.Numbers, "123")
    listOf(",", ".").forEach { text -> senTextKey(text, styleProvider = senSecondaryKeyStyle) }
    senSpaceKey()
    senTextKey("?", styleProvider = senSecondaryKeyStyle)
    senReturnKey()
}
