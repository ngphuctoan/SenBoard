package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.SenTheme

object SenTopBarDefaults {
    @Composable
    fun colors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )

    // Use MIN_VALUE because 0f is hardcoded so that expanded fraction is always 0f
    @JvmStatic
    val initialStateHeightOffsetLimit: Float = -Float.MIN_VALUE

    // Assuming the distance to scroll is equal to the expanded height
    @Composable
    fun initialStateHeightOffset(): Float = with(LocalDensity.current) {
        initialStateHeightOffsetLimit - TopAppBarDefaults.LargeAppBarExpandedHeight.toPx()
    }
}

object SenTopBarBackButtonDefaults {
    internal val Width: Dp = 76.dp

    internal val Padding: PaddingValues = PaddingValues(start = 20.dp, end = 16.dp)
}

// Make height offset a lot higher than its limit so that the top bar is collapsed at the beginning until we scroll back up
@Composable
fun rememberSenTopBarState(): TopAppBarState = rememberTopAppBarState(
    initialHeightOffsetLimit = SenTopBarDefaults.initialStateHeightOffsetLimit,
    initialHeightOffset = SenTopBarDefaults.initialStateHeightOffset(),
)

@Composable
fun SenTopBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier
            .width(SenTopBarBackButtonDefaults.Width)
            .padding(SenTopBarBackButtonDefaults.Padding),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Quay lại Cài đặt",
        )
    }
}

@Composable
fun SenTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = SenTopBarDefaults.colors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    LargeTopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
@Preview
fun SenTopBarPreview() {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenTheme {
        SenTopBar(
            title = { Text("Top bar demo") },
            navigationIcon = { SenTopBarBackButton(onClick = {}) },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Xem thêm",
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}
