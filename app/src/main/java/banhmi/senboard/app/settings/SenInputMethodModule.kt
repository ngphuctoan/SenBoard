package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenDescription
import banhmi.senboard.app.ui.SenHeader
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenMenuHasActionTrailingContent
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenSwitch
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.lastSegmentedPadding
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.app.ui.segmentedPadding
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.engine.VietnameseEngineType

import banhmi.senboard.shared.utils.outOf
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenInputMethod

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenInputMethodModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenInputMethod> {
            SenInputMethodScreen(navigator)
        }
    }
}

@Composable
fun SenInputMethodScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferences.collectAsState()

    SenInputMethodContent(
        onNavigate = navigator::goTo,
        onNavigateBack = navigator::goBack,
        vietnameseEngineType = preferences.vietnameseEngineType,
        autoCapitalizationEnabled = preferences.autoCapitalizationEnabled,
        spaceBarShortcutEnabled = preferences.spaceBarShortcutEnabled,
        easterEggEnabled = preferences.easterEggEnabled,
        onVietnameseEngineTypeUpdate = preferencesViewModel::updateVietnameseEngineType,
        onAutoCapitalizationEnabledUpdate = preferencesViewModel::updateAutoCapitalizationEnabled,
        onSpaceBarShortcutEnabledUpdate = preferencesViewModel::updateSpaceBarShortcutEnabled,
        onEasterEggEnabledUpdate = preferencesViewModel::updateEasterEggEnabled,
    )
}

@Composable
fun SenInputMethodContent(
    onNavigate: (Any) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    vietnameseEngineType: VietnameseEngineType,
    autoCapitalizationEnabled: Boolean,
    spaceBarShortcutEnabled: Boolean,
    easterEggEnabled: Boolean,
    onVietnameseEngineTypeUpdate: (VietnameseEngineType) -> Unit,
    onAutoCapitalizationEnabledUpdate: (Boolean) -> Unit,
    onSpaceBarShortcutEnabledUpdate: (Boolean) -> Unit,
    onEasterEggEnabledUpdate: (Boolean) -> Unit,
) {
    // Persist the Easter egg options even after disabling the Easter egg
    var showEasterEggOptions by remember { mutableStateOf<Boolean?>(null) }

    // Kinda not need to launch an effect
    if (showEasterEggOptions == null) showEasterEggOptions = easterEggEnabled

    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Phương thức nhập") },
                navigationIcon = { SenTopBarBackButton(onClick = onNavigateBack) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item { SenHeader("Phương thức gõ") }

            itemsIndexed(
                items = VietnameseEngineType.entries,
                key = { _, engine -> engine.id },
            ) { index, engine ->
                val indexCount = index outOf VietnameseEngineType.entries.size
                val selected = vietnameseEngineType == engine

                SenMenu(
                    selected = selected,
                    shapes = SenMenuDefaults.segmentedShapes(indexCount),
                    leadingContent = { RadioButton(selected = selected, onClick = null) },
                    onClick = { onVietnameseEngineTypeUpdate(engine) },
                    modifier = if (indexCount.isLast()) {
                        Modifier.lastSegmentedPadding()
                    } else {
                        Modifier.segmentedPadding()
                    },
                ) {
                    Text(engine.description)
                }
            }

            item {
                SenDescription(modifier = Modifier.lastSegmentedPadding()) {
                    Text("Bạn có thể chuyển nhanh phương thức nhập tại thanh công cụ trên bàn phím")
                }
            }

            item { SenHeader("Gõ nhanh") }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 3),
                    trailingContent = {
                        SenSwitch(checked = autoCapitalizationEnabled, onCheckedChange = null)
                    },
                    onClick = { onAutoCapitalizationEnabledUpdate(!autoCapitalizationEnabled) },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Tự động viết hoa")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(1 outOf 3),
                    supportingContent = { Text("Ấn dấu cách hai lần sẽ thêm một dấu chấm") },
                    trailingContent = {
                        SenSwitch(checked = spaceBarShortcutEnabled, onCheckedChange = null)
                    },
                    onClick = { onSpaceBarShortcutEnabledUpdate(!spaceBarShortcutEnabled) },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Phím tắt \".\"")
                }
            }

            item {
                SenMenu(
                    enabled = false,
                    shapes = SenMenuDefaults.segmentedShapes(2 outOf 3),
                    supportingContent = { Text("Tính năng chưa được hỗ trợ") },
                    trailingContent = {
                        SenSwitch(enabled = false, checked = false, onCheckedChange = null)
                    },
                    modifier = Modifier.lastSegmentedPadding(),
                ) {
                    Text("Gợi ý từ kế tiếp")
                }
            }

            if (showEasterEggOptions == true) {
                item { SenHeader("Easter egg") }

                item {
                    SenMenu(
                        shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                        supportingContent = { Text("Credit: dkter\nẤn để tải ứng dụng qua F-Droid!") },
                        trailingContent = {
                            SenMenuHasActionTrailingContent(actionDescription = "Đến trang Giới thiệu ứng dụng") {
                                SenSwitch(
                                    checked = easterEggEnabled,
                                    onCheckedChange = { onEasterEggEnabledUpdate(!easterEggEnabled) },
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
                        Text(buildAnnotatedString {
                            append("Bạn có thể bật lại Easter egg tại ")
                            withLink(link = goToAboutLink) { append("Giới thiệu ứng dụng") }
                        })
                    }
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenInputMethodScreenPreview() {
    // Enable the Easter egg to show all options in preview
    var preferences by remember { mutableStateOf(SenPreferences(easterEggEnabled = true)) }

    SenTheme {
        SenInputMethodContent(
            vietnameseEngineType = preferences.vietnameseEngineType,
            autoCapitalizationEnabled = preferences.autoCapitalizationEnabled,
            spaceBarShortcutEnabled = preferences.spaceBarShortcutEnabled,
            easterEggEnabled = preferences.easterEggEnabled,
            onVietnameseEngineTypeUpdate = { vietnameseEngine ->
                preferences = preferences.copy(
                    vietnameseEngineType = vietnameseEngine,
                )
            },
            onAutoCapitalizationEnabledUpdate = { autoCapitalizationEnabled ->
                preferences = preferences.copy(
                    autoCapitalizationEnabled = autoCapitalizationEnabled,
                )
            },
            onSpaceBarShortcutEnabledUpdate = { spaceBarShortcutEnabled ->
                preferences = preferences.copy(
                    spaceBarShortcutEnabled = spaceBarShortcutEnabled,
                )
            },
            onEasterEggEnabledUpdate = { easterEggEnabled ->
                preferences = preferences.copy(
                    easterEggEnabled = easterEggEnabled,
                )
            },
        )
    }
}
