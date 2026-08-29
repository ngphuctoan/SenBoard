@file:Suppress("UNUSED")

package banhmi.senboard.keyboard.model

/* "Alternative" means a key that can be accessed by long tapping the primary key
and drag the cursor position to the position of that alternative key */
data class SenAltData(
    val display: SenKeyDisplay,
    val handler: SenKeyHandler,
)

data class SenAltRow(
    val datas: List<SenAltData>,
)

// The anchor is the row index of which the entire popup will be anchored to, and positioned by cursor position
sealed interface SenAltPopupAnchor {
    fun provideRowIndex(rows: List<SenAltRow>) =
        _provideRowIndex(rows.map { row -> row.datas.size }.sorted())

    // Implementation should only worry about getting the correct index
    @Suppress("FunctionName")
    fun _provideRowIndex(sortedRowSizes: List<Int>): Int

    // For more fine-grained control over the anchor position
    data class Row(val index: Int) : SenAltPopupAnchor {
        override fun _provideRowIndex(sortedRowSizes: List<Int>) = index
    }

    data object RowStart : SenAltPopupAnchor {
        override fun _provideRowIndex(sortedRowSizes: List<Int>) = 0
    }

    data object RowCenter : SenAltPopupAnchor {
        override fun _provideRowIndex(sortedRowSizes: List<Int>) =
            sortedRowSizes[sortedRowSizes.size / 2]
    }

    data object RowEnd : SenAltPopupAnchor {
        override fun _provideRowIndex(sortedRowSizes: List<Int>) = sortedRowSizes.last()
    }
}

object SenAltPopupDefaults {
    val Anchor = SenAltPopupAnchor.RowStart
}

sealed interface SenAlt {
    object None : SenAlt

    data class Popup(
        val anchor: SenAltPopupAnchor,
        val rows: List<SenAltRow>,
    ) : SenAlt
}

typealias SenAltProvider = () -> SenAlt
