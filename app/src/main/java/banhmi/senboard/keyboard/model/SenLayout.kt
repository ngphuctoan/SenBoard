package banhmi.senboard.keyboard.model

import androidx.compose.ui.Alignment
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.impl.layout.senAaaaaLayout
import banhmi.senboard.keyboard.impl.layout.senStandardLayout
import banhmi.senboard.keyboard.state.SenBoardState

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
)

data class SenLayout(
    val rows: List<SenLayoutRow>,
)

// Register the layouts as enums here first before implementing them!
enum class SenLayoutType {
    Standard, Aaaaa;
}

fun provideLayout(layoutType: SenLayoutType) = when (layoutType) {
    SenLayoutType.Standard -> senStandardLayout
    SenLayoutType.Aaaaa -> senAaaaaLayout
}

typealias SenLayoutProvider = (SenBoardState, SenPreferences) -> SenLayout
