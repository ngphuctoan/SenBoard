package banhmi.senboard.app.ui

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max

val LocalAppIcon: ProvidableCompositionLocal<Drawable?> = compositionLocalOf { null }

@RequiresApi(Build.VERSION_CODES.O)
private class AdaptiveDrawableShape(
    private val drawable: AdaptiveIconDrawable,
    private var matrix: Matrix = Matrix(),
) : Shape {
    private var path = Path()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val originalPath = drawable.iconMask.asComposePath()
        val bounds = originalPath.getBounds()
        val maxDimension = max(bounds.width, bounds.height)

        path.rewind()
        path = originalPath.copy()

        matrix.reset()
        matrix.scale(size.width / maxDimension, size.height / maxDimension)
        matrix.translate(bounds.left, bounds.top)

        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Composable
fun appIconShape(): Shape {
    val appIcon = LocalAppIcon.current ?: return CircleShape
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AdaptiveDrawableShape(appIcon as AdaptiveIconDrawable)
    } else {
        CircleShape
    }
}
