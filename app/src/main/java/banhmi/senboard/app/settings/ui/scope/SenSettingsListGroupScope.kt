package banhmi.senboard.app.settings.ui.scope

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface SenSettingsListGroupScope {
    val horizontalMargin: Dp
    val verticalMargin: Dp
    val horizontalPadding: Dp
    val verticalPadding: Dp
    val showIcons: Boolean
    val iconSize: Dp
    val iconShape: Shape
    val iconShapeSize: Dp
    val containerRadius: Dp
}

class SenSettingsListGroupScopeImpl(
    val scope: ColumnScope,
    override val horizontalMargin: Dp = 12.dp,
    override val verticalMargin: Dp = 12.dp,
    override val horizontalPadding: Dp = 6.dp,
    override val verticalPadding: Dp = 0.dp,
    override val showIcons: Boolean = true,
    override val iconSize: Dp = 24.dp,
    override val iconShape: Shape = CircleShape,
    override val iconShapeSize: Dp = 34.dp,
    override val containerRadius: Dp = 24.dp,
) : SenSettingsListGroupScope, ColumnScope by scope
