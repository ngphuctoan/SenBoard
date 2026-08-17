package banhmi.senboard.app.icon

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

sealed interface SenAppIconResult {
    object NotLoaded : SenAppIconResult
    object Failed : SenAppIconResult
    data class Success(val drawable: Drawable) : SenAppIconResult
}

val LocalSenAppIcon: ProvidableCompositionLocal<SenAppIconResult> = compositionLocalOf {
    SenAppIconResult.NotLoaded
}

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
        val drawablePath = drawable.iconMask.asComposePath()
        val bounds = drawablePath.getBounds()
        val maxDimension = max(bounds.width, bounds.height)

        path.rewind()
        /* Copy to prevent mutations on the original drawable path.
        This solves the issues of the shape resizing on navigation transition */
        path = drawablePath.copy()

        matrix.reset()
        matrix.scale(size.width / maxDimension, size.height / maxDimension)
        matrix.translate(bounds.left, bounds.top)

        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Composable
fun senAppIconShape(fallbackShape: Shape = CircleShape): Shape {
    val appIcon = LocalSenAppIcon.current

    val isOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    val isOneUi = Build.BRAND.lowercase() == "samsung"

    // One UI's squircle shape is off-centered, so disable app icon shape for Samsung devices
    return if (appIcon is SenAppIconResult.Success && isOreo && !isOneUi) {
        AdaptiveDrawableShape(appIcon.drawable as AdaptiveIconDrawable)
    } else {
        fallbackShape
    }
}
