package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.isLast
import banhmi.senboard.app.ui.lastMenuPadding
import banhmi.senboard.app.ui.outOf
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ime.engine.VietnameseEngine
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import kotlin.math.pow

object SenInputMethod

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenInputMethodModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenInputMethod> {
            SenInputMethodScreen(
                onNavigate = navigator::goTo,
                onNavigateBack = navigator::goBack,
                preferencesViewModel = hiltViewModel(),
            )
        }
    }
}

@Composable
fun SenInputMethodScreen(
    onNavigate: (Any) -> Unit,
    onNavigateBack: () -> Unit,
    preferencesViewModel: SenPreferencesViewModel,
) {
    val preferences by preferencesViewModel.preferences.collectAsState()
    SenInputMethodContent(
        preferences = preferences,
        onNavigate = onNavigate,
        onNavigateBack = onNavigateBack,
        onVietnameseEngineUpdate = preferencesViewModel::updateVietnameseEngine,
        onAutoCapitalizationEnabledUpdate = preferencesViewModel::updateAutoCapitalizationEnabled,
        onSpaceBarShortcutEnabledUpdate = preferencesViewModel::updateSpaceBarShortcutEnabled,
        onEasterEggEnabledUpdate = preferencesViewModel::updateEasterEggEnabled,
    )
}

@Composable
fun SenInputMethodContent(
    preferences: SenPreferences,
    onNavigate: (Any) -> Unit,
    onNavigateBack: () -> Unit,
    onVietnameseEngineUpdate: (VietnameseEngine) -> Unit,
    onAutoCapitalizationEnabledUpdate: (Boolean) -> Unit,
    onSpaceBarShortcutEnabledUpdate: (Boolean) -> Unit,
    onEasterEggEnabledUpdate: (Boolean) -> Unit,
) {
    // Persist the Easter egg options even after disabling the Easter egg
    var showEasterEggOptions by remember { mutableStateOf<Boolean?>(null) }

    // Kinda not need to launch an effect
    if (showEasterEggOptions == null) showEasterEggOptions = preferences.easterEggEnabled

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
                items = VietnameseEngine.entries,
                key = { _, engine -> engine.dataStoreValue },
            ) { index, engine ->
                val indexCount = index outOf VietnameseEngine.entries.size
                val selected = preferences.vietnameseEngine == engine.dataStoreValue

                SenMenu(
                    selected = selected,
                    shapes = SenMenuDefaults.segmentedShapes(indexCount),
                    leadingContent = { RadioButton(selected = selected, onClick = null) },
                    onClick = { onVietnameseEngineUpdate(engine) },
                    modifier = if (indexCount.isLast()) {
                        Modifier.lastMenuPadding()
                    } else {
                        Modifier
                    },
                ) {
                    Text(engine.engineName)
                }
            }

            item { SenHeader("Gõ nhanh") }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 3),
                    trailingContent = {
                        Switch(
                            checked = preferences.autoCapitalizationEnabled,
                            onCheckedChange = null,
                        )
                    },
                    onClick = {
                        onAutoCapitalizationEnabledUpdate(
                            !preferences.autoCapitalizationEnabled
                        )
                    },
                ) {
                    Text("Tự động viết hoa")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(1 outOf 3),
                    supportingContent = { Text("Ấn dấu cách hai lần sẽ thêm một dấu chấm") },
                    trailingContent = {
                        Switch(
                            checked = preferences.spaceBarShortcutEnabled,
                            onCheckedChange = null,
                        )
                    },
                    onClick = {
                        onSpaceBarShortcutEnabledUpdate(
                            !preferences.spaceBarShortcutEnabled
                        )
                    },
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
                        Switch(
                            enabled = false,
                            checked = false,
                            onCheckedChange = null,
                        )
                    },
                    modifier = Modifier.lastMenuPadding(),
                ) {
                    Text("Gợi ý từ kế tiếp")
                }
            }

            if (showEasterEggOptions == true) {
                item { SenHeader("Easter egg") }

                item {
                    SenMenu(
                        shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                        supportingContent = { Text("Credit: dkter\nẤn để tải ứng dụng thông qua F-Droid!") },
                        trailingContent = {
                            SenMenuHasActionTrailingContent(actionDescription = "Đến trang Giới thiệu ứng dụng") {
                                Switch(
                                    checked = preferences.easterEggEnabled,
                                    onCheckedChange = {
                                        onEasterEggEnabledUpdate(
                                            !preferences.easterEggEnabled
                                        )
                                    },
                                )
                            }
                        },
                        // TODO: Add F-Droid link for the aaaaa app
                        onClick = {},
                        modifier = Modifier.lastMenuPadding(),
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
    SenTheme {
        SenInputMethodContent(
            // Enable the Easter egg to show all options in preview
            preferences = SenPreferences(
                easterEggEnabled = true,
            ),
            onNavigate = {},
            onNavigateBack = {},
            onVietnameseEngineUpdate = {},
            onAutoCapitalizationEnabledUpdate = {},
            onSpaceBarShortcutEnabledUpdate = {},
            onEasterEggEnabledUpdate = {},
        )
    }
}
