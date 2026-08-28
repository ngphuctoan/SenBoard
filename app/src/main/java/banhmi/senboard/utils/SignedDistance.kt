package banhmi.senboard.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// Useful for calculating the distance between a point and a specific shape
interface SignedDistance {
    fun distanceToPoint(
        pointPosition: Offset,
    ): Float
}

class RectangleSDF(
    private val rectangle: Rect,
) : SignedDistance {
    override fun distanceToPoint(
        pointPosition: Offset,
    ): Float {
        val normalizedPosition = Offset(abs(pointPosition.x - rectangle.center.x), abs(pointPosition.y - rectangle.center.y))
        val oppositePosition = normalizedPosition - Offset(rectangle.width / 2, rectangle.height / 2)

        val outerDistance = Offset(max(oppositePosition.x, 0f), max(oppositePosition.y, 0f)).getDistance()
        val innerDistance = min(max(oppositePosition.x, oppositePosition.y), 0f)

        return outerDistance + innerDistance
    }

}
