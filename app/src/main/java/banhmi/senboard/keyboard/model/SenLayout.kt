package banhmi.senboard.keyboard.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

object SenLayoutKeyDefaults {
    @JvmStatic
    val AreaWidthMultiplier: Float = 1f

    val ShapeAlignment: Alignment.Horizontal = Alignment.Start
}


object SenLayoutRowDefaults {
    @JvmStatic
    val HeightMultiplier: Float = 1f
}

object SenLayoutDefaults {
    val KeySpacing: PaddingValues = PaddingValues(4.dp)
}

data class SenLayoutKey(
    val areaWidthMultiplier: Float,
    val shapeWidthProportion: Float,
    val shapeAlignment: Alignment.Horizontal,
)

data class SenLayoutRow(
    val keys: List<SenLayoutKey>,
    val heightMultiplier: Float,
) {
    val totalAreaWidthMultipliers: Float
        get() = keys.fold(initial = 0f) { widthMultipliers, key ->
            widthMultipliers + key.areaWidthMultiplier
        }
}

data class SenLayout(
    val rows: List<SenLayoutRow>,
    val keySpacing: PaddingValues,
) {
    val maxTotalAreaWidthMultipliers: Float
        get() = rows.minBy { row -> row.totalAreaWidthMultipliers }.totalAreaWidthMultipliers

    val totalHeightMultipliers: Float
        get() = rows.fold(initial = 0f) { heightMultiplier, row ->
            heightMultiplier + row.heightMultiplier
        }
}
