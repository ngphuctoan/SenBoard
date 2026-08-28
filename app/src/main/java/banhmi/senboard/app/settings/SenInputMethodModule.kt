package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenDescription
import banhmi.senboard.app.ui.SenHeader
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
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
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.outOf
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
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
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
    val preferences by preferencesViewModel.preferencesState.collectAsStateWithLifecycle()

    SenInputMethodContent(
        onNavigateBack = navigator::goBack,
        vietnameseEngineType = preferences.vietnameseEngineType,
        autoCapitalizationEnabled = preferences.autoCapitalizationEnabled,
        spaceBarShortcutEnabled = preferences.spaceBarShortcutEnabled,
        wordSuggestionsEnabled = preferences.wordSuggestionsEnabled,
        onVietnameseEngineTypeUpdate = preferencesViewModel::updateVietnameseEngineType,
        onAutoCapitalizationEnabledUpdate = preferencesViewModel::updateAutoCapitalizationEnabled,
        onSpaceBarShortcutEnabledUpdate = preferencesViewModel::updateSpaceBarShortcutEnabled,
        onWordSuggestionsEnabledUpdate = preferencesViewModel::updateWordSuggestionsEnabled,
    )
}

@Composable
fun SenInputMethodContent(
    onNavigateBack: () -> Unit = {},
    vietnameseEngineType: VietnameseEngineType,
    autoCapitalizationEnabled: Boolean,
    spaceBarShortcutEnabled: Boolean,
    wordSuggestionsEnabled: Boolean,
    onVietnameseEngineTypeUpdate: (VietnameseEngineType) -> Unit,
    onAutoCapitalizationEnabledUpdate: (Boolean) -> Unit,
    onSpaceBarShortcutEnabledUpdate: (Boolean) -> Unit,
    onWordSuggestionsEnabledUpdate: (Boolean) -> Unit,
) {
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
                key = { _, engineType -> engineType.id },
            ) { index, engineType ->
                val indexCount = index outOf VietnameseEngineType.entries.size
                val selected = vietnameseEngineType == engineType

                SenMenu(
                    selected = selected,
                    shapes = SenMenuDefaults.segmentedShapes(indexCount),
                    leadingContent = {
                        RadioButton(
                            selected = selected,
                            onClick = null,
                        )
                    },
                    onClick = { onVietnameseEngineTypeUpdate(engineType) },
                    modifier = if (indexCount.isLast()) {
                        Modifier.lastSegmentedPadding()
                    } else {
                        Modifier.segmentedPadding()
                    },
                ) {
                    Text(engineType.description)
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
                        SenSwitch(
                            checked = autoCapitalizationEnabled,
                            onCheckedChange = null,
                        )
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
                        SenSwitch(
                            checked = spaceBarShortcutEnabled,
                            onCheckedChange = null,
                        )
                    },
                    onClick = { onSpaceBarShortcutEnabledUpdate(!spaceBarShortcutEnabled) },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Phím tắt \".\"")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(2 outOf 3),
                    trailingContent = {
                        SenSwitch(
                            checked = wordSuggestionsEnabled,
                            onCheckedChange = null,
                        )
                    },
                    onClick = { onWordSuggestionsEnabledUpdate(!wordSuggestionsEnabled) },
                    modifier = Modifier.lastSegmentedPadding(),
                ) {
                    Text("Gợi ý từ kế tiếp")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenInputMethodScreenPreview() {
    var preferences by remember {
        mutableStateOf(SenPreferences())
    }

    SenTheme {
        SenInputMethodContent(
            vietnameseEngineType = preferences.vietnameseEngineType,
            autoCapitalizationEnabled = preferences.autoCapitalizationEnabled,
            spaceBarShortcutEnabled = preferences.spaceBarShortcutEnabled,
            wordSuggestionsEnabled = preferences.wordSuggestionsEnabled,
            onVietnameseEngineTypeUpdate = { vietnameseEngine ->
                preferences = preferences.copy(vietnameseEngineType = vietnameseEngine)
            },
            onAutoCapitalizationEnabledUpdate = { autoCapitalizationEnabled ->
                preferences = preferences.copy(autoCapitalizationEnabled = autoCapitalizationEnabled)
            },
            onSpaceBarShortcutEnabledUpdate = { spaceBarShortcutEnabled ->
                preferences = preferences.copy(spaceBarShortcutEnabled = spaceBarShortcutEnabled)
            },
            onWordSuggestionsEnabledUpdate = { wordSuggestionsEnabled ->
                preferences = preferences.copy(wordSuggestionsEnabled = wordSuggestionsEnabled)
            },
        )
    }
}
