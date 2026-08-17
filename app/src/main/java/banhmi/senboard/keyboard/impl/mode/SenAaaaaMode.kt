package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.impl.layout.SenAaaaaLayout
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senCharKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSpaceKey

val SenAaaaaMode = senMode(SenAaaaaLayout) {
    // First row
    senCharKey('a')

    // Second row
    senBackSpaceKey()
    senSpaceKey()
    senReturnKey()
}
