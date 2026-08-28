package banhmi.senboard.keyboard.model

import banhmi.senboard.keyboard.impl.handler.SenCharKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenTextKeyHandler

class SenAltRowScope {
    private val datas: MutableList<SenAltData> = mutableListOf()

    fun senAlt(
        display: SenKeyDisplay,
        handler: SenKeyHandler,
        anchor: Boolean,
    ) = datas.add(
        SenAltData(
            display,
            handler,
            anchor,
        ),
    )

    fun build() = SenAltDataRow(datas.toList())
}

interface SenAltScope {
    fun build(): SenAlt
}

class SenAltPopupScope : SenAltScope {
    private val rows: MutableList<SenAltDataRow> = mutableListOf()

    fun senAltRow(
        builder: SenAltRowScope.() -> Unit,
    ) = rows.add(SenAltRowScope().apply(builder).build())

    override fun build() = SenAlt.Popup(rows.toList())
}

// Extensions for alt keys
fun SenAltRowScope.senAltCharKey(
    char: Char,
    anchor: Boolean = SenAltDataDefaults.Anchor,
) = senAlt(
    display = SenKeyDisplay.Char(char),
    handler = SenCharKeyHandler(char),
    anchor = anchor,
)

fun SenAltRowScope.senAltTextKey(
    text: String,
    anchor: Boolean = SenAltDataDefaults.Anchor,
) = senAlt(
    display = SenKeyDisplay.Text(text),
    handler = SenTextKeyHandler(text),
    anchor = anchor,
)

fun senAltPopup(
    builder: SenAltPopupScope.() -> Unit,
) = SenAltPopupScope().apply(builder).build()
