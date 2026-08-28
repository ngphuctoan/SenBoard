package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenDescription
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenMenuHasActionTrailingContent
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenSwitch
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.lastSegmentedPadding
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.outOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenEasterEggs

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenEasterEggsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenEasterEggs> {
            SenEasterEggsScreen(navigator)
        }
    }
}

@Composable
fun SenEasterEggsScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferencesState.collectAsStateWithLifecycle()

    SenEasterEggsContent(
        onNavigate = navigator::goTo,
        onNavigateBack = navigator::goBack,
        easterEggsEnabled = preferences.easterEggsEnabled,
        aaaaaModeEnabled = preferences.aaaaaModeEnabled,
        onEasterEggsEnabledUpdate = preferencesViewModel::updateEasterEggsEnabled,
        onAaaaaModeEnableUpdate = preferencesViewModel::updateAaaaaModeEnabled,
    )
}

@Composable
fun SenEasterEggsContent(
    onNavigate: (Any) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    easterEggsEnabled: Boolean,
    aaaaaModeEnabled: Boolean,
    onEasterEggsEnabledUpdate: (Boolean) -> Unit,
    onAaaaaModeEnableUpdate: (Boolean) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Easter egg") },
                navigationIcon = { SenTopBarBackButton(onClick = onNavigateBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item {
                SenMenu(
                    checked = easterEggsEnabled,
                    onCheckedChange = { onEasterEggsEnabledUpdate(!easterEggsEnabled) },
                    shapes = SenMenuDefaults.circleShapes(),
                    trailingContent = {
                        SenSwitch(
                            checked = easterEggsEnabled,
                            onCheckedChange = null,
                        )
                    },
                    contentPadding = SenMenuDefaults.CircleContentPadding,
                    colors = SenMenuDefaults.primaryColors(),
                    modifier = Modifier
                        .padding(SenMenuDefaults.CirclePadding)
                        .lastSegmentedPadding(),
                ) {
                    Text("Bật Easter egg")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                    supportingContent = { Text("Credit: dkter\nẤn để tải ứng dụng qua F-Droid!") },
                    trailingContent = {
                        SenMenuHasActionTrailingContent(actionDescription = "Đến trang Giới thiệu ứng dụng") {
                            SenSwitch(
                                checked = aaaaaModeEnabled,
                                onCheckedChange = { onAaaaaModeEnableUpdate(!aaaaaModeEnabled) },
                            )
                        }
                    },
                    // TODO: Add F-Droid link for the aaaaa app
                    onClick = {},
                    modifier = Modifier.lastSegmentedPadding(),
                ) {
                    Text("Chế độ aaaaa")
                }
            }

            item {
                val goToAboutLinkListener = LinkInteractionListener {
                    onNavigate(SenAbout)
                }

                val goToAboutLink = LinkAnnotation.Clickable(
                    tag = "GO_TO_ABOUT",
                    linkInteractionListener = goToAboutLinkListener,
                )

                SenDescription {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Thông tin về Easter egg",
                    )
                    Text(
                        buildAnnotatedString {
                            append("Bạn có thể bật lại Easter egg tại ")
                            withLink(link = goToAboutLink) { append("Giới thiệu ứng dụng") }
                        },
                    )
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenEasterEggsScreenPreview() {
    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        SenEasterEggsContent(
            easterEggsEnabled = preferences.easterEggsEnabled,
            aaaaaModeEnabled = preferences.aaaaaModeEnabled,
            onEasterEggsEnabledUpdate = { easterEggsEnabled ->
                preferences = preferences.copy(easterEggsEnabled = easterEggsEnabled)
            },
            onAaaaaModeEnableUpdate = { aaaaaModeEnabled ->
                preferences = preferences.copy(aaaaaModeEnabled = aaaaaModeEnabled)
            },
        )
    }
}
