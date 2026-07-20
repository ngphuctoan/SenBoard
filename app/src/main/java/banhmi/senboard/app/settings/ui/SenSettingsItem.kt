package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.settings.models.SenSettingsItemAction
import banhmi.senboard.app.settings.ui.scope.SenSettingsListGroupScope

@Composable
private fun SenSettingsIcon(
    icon: ImageVector,
    description: String? = null,
    color: Color,
    contentColor: Color,
    size: Dp,
    shape: Shape,
    shapeSize: Dp,
    rightMargin: Dp,
) {
    Box(
        modifier = Modifier
            .padding(end = rightMargin)
            .size(shapeSize)
            .background(color, shape = shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = contentColor,
            modifier = Modifier.size(size),
        )
    }
}

@Composable
private fun SenSettingsItemBottomAction(action: SenSettingsItemAction = SenSettingsItemAction.None) {
    if (action is SenSettingsItemAction.Bottom) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 12.dp),
        ) {
            action.content()
        }
    }
}

@Composable
fun SenSettingsListGroupScope.SenSettingsItem(
    label: String,
    supportingLabels: List<String> = emptyList(),
    icon: ImageVector? = null,
    iconDescription: String? = null,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    action: SenSettingsItemAction = SenSettingsItemAction.None,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    val supportingContent = if (supportingLabels.isNotEmpty()) {
        @Composable { Text(supportingLabels.joinToString(" \u2022 ")) }
    } else {
        null
    }

    val trailingContent = if (action is SenSettingsItemAction.Trailing) {
        @Composable { action.content() }
    } else {
        null
    }

    Column(modifier = clickableModifier) {
        ListItem(
            modifier = Modifier
                .padding(horizontal = horizontalMargin)
                .padding(horizontalPadding, verticalPadding),
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = { Text(label) },
            supportingContent = supportingContent,
            leadingContent = {
                if (showIcons && icon != null) SenSettingsIcon(
                    icon, iconDescription, color, contentColor,
                    iconSize, iconShape, iconShapeSize, horizontalPadding,
                )
            },
            trailingContent = trailingContent,
        )
        SenSettingsItemBottomAction(action)
    }
}
