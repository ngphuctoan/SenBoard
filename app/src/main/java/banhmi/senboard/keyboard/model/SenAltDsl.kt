package banhmi.senboard.keyboard.model

import banhmi.senboard.keyboard.impl.handler.SenCharKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenTextKeyHandler

class SenAltRowScope {
    private val datas: MutableList<SenAltData> = mutableListOf()

    fun senAlt(
        display: SenKeyDisplay,
        handler: SenKeyHandler,
    ) = datas.add(
        SenAltData(
            display,
            handler,
        ),
    )

    fun build() = SenAltRow(datas.toList())
}

// Extensions for alt keys
fun SenAltRowScope.senAltCharKey(
    char: Char,
) = senAlt(
    display = SenKeyDisplay.Char(char),
    handler = SenCharKeyHandler(char),
)

fun SenAltRowScope.senAltTextKey(
    text: String,
) = senAlt(
    display = SenKeyDisplay.Text(text),
    handler = SenTextKeyHandler(text),
)

interface SenAltScope {
    fun build(): SenAlt
}

class SenAltNoneScope : SenAltScope {
    override fun build() = SenAlt.None
}

fun senAltNone() = SenAltNoneScope().build()

class SenAltPopupScope(
    private val anchor: SenAltPopupAnchor = SenAltPopupDefaults.Anchor,
) : SenAltScope {
    private val rows: MutableList<SenAltRow> = mutableListOf()

    fun senAltRow(
        builder: SenAltRowScope.() -> Unit,
    ) = rows.add(SenAltRowScope().apply(builder).build())

    override fun build() = SenAlt.Popup(
        anchor,
        rows.toList(),
    )
}

fun senAltPopup(
    anchor: SenAltPopupAnchor = SenAltPopupDefaults.Anchor,
    builder: SenAltPopupScope.() -> Unit,
) = SenAltPopupScope(anchor).apply(builder).build()
