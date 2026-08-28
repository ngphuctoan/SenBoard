package banhmi.senboard.keyboard.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

object SenLayoutKeyDefaults {
    @JvmStatic
    val AreaWidthMultiplier = 1f

    val ShapeAlignment = Alignment.Start
}

data class SenLayoutKey(
    val areaWidthMultiplier: Float,
    val shapeWidthProportion: Float,
    val shapeAlignment: Alignment.Horizontal,
)


object SenLayoutRowDefaults {
    @JvmStatic
    val HeightMultiplier = 1f
}

data class SenLayoutRow(
    val keys: List<SenLayoutKey>,
    val heightMultiplier: Float,
) {
    //val totalAreaWidthMultipliers
    //    get() = keys.fold(initial = 0f) { widthMultipliers, key ->
    //        widthMultipliers + key.areaWidthMultiplier
    //    }
}

object SenLayoutDefaults {
    val KeySpacing = PaddingValues(4.dp)
}

data class SenLayout(
    val rows: List<SenLayoutRow>,
    val keySpacing: PaddingValues,
) {
    //val maxTotalAreaWidthMultipliers
    //    get() = rows.minBy { row -> row.totalAreaWidthMultipliers }.totalAreaWidthMultipliers
    //
    //val totalHeightMultipliers
    //    get() = rows.fold(initial = 0f) { heightMultiplier, row ->
    //        heightMultiplier + row.heightMultiplier
    //    }
}
