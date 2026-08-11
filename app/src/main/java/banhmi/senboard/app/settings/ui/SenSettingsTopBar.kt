package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.SenBoardTheme

data class SenSettingsNavigationBackButtonColors(
    val containerColor: Color,
    val contentColor: Color,
)

object SenSettingsTopBarDefaults {
    val ContainerHeight: Dp = 56.dp

    val ContentPadding = PaddingValues(horizontal = 4.dp)

    @Composable
    fun containerColor(): Color = MaterialTheme.colorScheme.surfaceContainer
}

object SenSettingsNavigationBackButtonDefaults {
    val InteractionHeight: Dp = 72.dp

    @Composable
    fun colors() = SenSettingsNavigationBackButtonColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun SenSettingsNavigationBackButton(
    onNavigateBack: () -> Unit,
    interactionHeight: Dp = SenSettingsNavigationBackButtonDefaults.InteractionHeight,
    maxHeight: Dp = SenSettingsTopBarDefaults.ContainerHeight,
    colors: SenSettingsNavigationBackButtonColors = SenSettingsNavigationBackButtonDefaults.colors(),
) {
    /* Kinda weird to make the interactive area be larger than the top bar height,
    but that's to (closely) achieve how AOSP Settings look like */
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides interactionHeight) {
        IconButton(
            onClick = onNavigateBack,
            colors = IconButtonColors(
                containerColor = colors.containerColor,
                disabledContainerColor = colors.containerColor,
                contentColor = colors.contentColor,
                disabledContentColor = colors.contentColor,
            ),
            modifier = Modifier.heightIn(max = maxHeight),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Quay lại Cài đặt",
            )
        }
    }
}

@Composable
fun SenSettingsTopBar(
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    containerHeight: Dp = SenSettingsTopBarDefaults.ContainerHeight,
    contentPadding: PaddingValues = SenSettingsTopBarDefaults.ContentPadding,
    containerColor: Color = SenSettingsTopBarDefaults.containerColor(),
    onNavigateBack: (() -> Unit)? = null,
) {
    TopAppBar(
        title = title,
        navigationIcon = {
            if (onNavigateBack != null) {
                SenSettingsNavigationBackButton(onNavigateBack = onNavigateBack)
            }
        },
        actions = actions,
        expandedHeight = containerHeight,
        contentPadding = contentPadding,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),
    )
}

@Composable
@Preview
fun SenSettingsTopBarPreview() {
    SenBoardTheme {
        SenSettingsTopBar(
            title = { SenSettingsTitle("Top bar demo") },
            actions = {
                // Example "Show more" icon "button" (it's just an icon :b)
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Xem thêm",
                )
            },
            onNavigateBack = {}, // Show navigation back icon
        )
    }
}
