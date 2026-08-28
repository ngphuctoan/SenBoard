package banhmi.senboard.keyboard.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment

class SenLayoutRowScope(private val heightMultiplier: Float) {
    private val keys: MutableList<SenLayoutKey> = mutableListOf()

    fun senKey(
        areaWidthMultiplier: Float = SenLayoutKeyDefaults.AreaWidthMultiplier,
        // By default, the shape is the same width as the area
        shapeWidthProportion: Float = areaWidthMultiplier,
        shapeAlignment: Alignment.Horizontal = SenLayoutKeyDefaults.ShapeAlignment,
    ) = keys.add(
        SenLayoutKey(
            areaWidthMultiplier,
            shapeWidthProportion,
            shapeAlignment,
        ),
    )

    fun build() = SenLayoutRow(
        keys = keys.toList(), // Copy list as read-only
        heightMultiplier = heightMultiplier,
    )
}

class SenLayoutScope(private val keySpacing: PaddingValues) {
    private val rows: MutableList<SenLayoutRow> = mutableListOf()

    fun senRow(
        heightMultiplier: Float = SenLayoutRowDefaults.HeightMultiplier,
        builder: SenLayoutRowScope.() -> Unit,
    ) = rows.add(
        SenLayoutRowScope(heightMultiplier)
            .apply(builder)
            .build(),
    )

    fun build() = SenLayout(
        rows = rows.toList(), // Copy list as read-only
        keySpacing = keySpacing,
    )
}

fun senLayout(
    keySpacing: PaddingValues = SenLayoutDefaults.KeySpacing,
    builder: SenLayoutScope.() -> Unit,
) = SenLayoutScope(keySpacing)
    .apply(builder)
    .build()
