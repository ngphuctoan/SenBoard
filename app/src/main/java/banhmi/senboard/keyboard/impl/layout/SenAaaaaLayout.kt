package banhmi.senboard.keyboard.impl.layout

import banhmi.senboard.keyboard.model.SenLayoutProvider
import banhmi.senboard.keyboard.model.senLayout

/* Easter egg inspired by "aaaaa" by dkter
App repo: https://github.com/dkter/aaaaa */
val senAaaaaLayout: SenLayoutProvider = { _, _ ->
    senLayout {
        senRow(3f) {
            senKey(10f)
        }
        senRow {
            senKey(1.5f)
            senKey(7f)
            senKey(1.5f)
        }
    }
}
