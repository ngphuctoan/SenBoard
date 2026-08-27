package banhmi.senboard.keyboard.model

/* "Alternative" means a key that can be accessed by long tapping the primary key
and drag the cursor position to the position of that alternative key */
object SenAltDataDefaults {
    @JvmStatic
    val Anchor = false
}

data class SenAltData(
    val display: SenKeyDisplay,
    val handler: SenKeyHandler,
    /* Set the alternative key as the anchor point
    (i.e. the popup's x position is the center x position of this key)
    ====================
    Note to rows:
    - If there are more than one anchor keys, the UI will pick the first one in the loop
    - Likewise, if there is no anchor key, the UI will pick the first one of the last row */
    val anchor: Boolean,
)

data class SenAltDataRow(
    val data: List<SenAltData>,
)

sealed interface SenAlt {
    object None : SenAlt

    data class Popup(
        val rows: List<SenAltDataRow>,
    ) : SenAlt
}
