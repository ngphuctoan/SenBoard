package banhmi.senboard.keyboard

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntRect
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import banhmi.senboard.data.bigram.UserBigramRepository
import banhmi.senboard.data.bigram.UserBigramViewModel
import banhmi.senboard.data.preferences.SenPreferencesRepository
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.engine.bigram.BigramEngine
import banhmi.senboard.keyboard.impl.handler.SenShiftKeyHandler
import banhmi.senboard.keyboard.lifecycle.SenLifecycleImService
import banhmi.senboard.keyboard.model.SenKeyHandlerContext
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.keyboard.model.provideLayout
import banhmi.senboard.keyboard.model.provideMode
import banhmi.senboard.keyboard.state.SenBoardStateDefaults
import banhmi.senboard.keyboard.state.SenBoardStateViewModel
import banhmi.senboard.keyboard.state.ShiftMode
import banhmi.senboard.keyboard.ui.SenBoard
import banhmi.senboard.keyboard.ui.SenBoardDefaults
import banhmi.senboard.keyboard.ui.SenBoardScaffold
import banhmi.senboard.keyboard.ui.SenBoardScaffoldDefaults
import banhmi.senboard.keyboard.ui.SenDisplay
import banhmi.senboard.keyboard.ui.SenEngineIcon
import banhmi.senboard.keyboard.ui.SenEngineSwitcher
import banhmi.senboard.keyboard.ui.SenKey
import banhmi.senboard.keyboard.ui.SenKeyIndication
import banhmi.senboard.keyboard.ui.SenKeyIndicationDefaults
import banhmi.senboard.keyboard.ui.SenSuggestions
import banhmi.senboard.keyboard.ui.SenToolbar
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.EMPTY
import banhmi.senboard.utils.toIntOffset
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SenImService : SenLifecycleImService() {
    @Inject
    lateinit var preferencesRepository: SenPreferencesRepository

    @Inject
    lateinit var bigramEngine: BigramEngine

    @Inject
    lateinit var userBigramRepository: UserBigramRepository

    /* @HiltViewModel only works with Activity class, so we have to construct the view model manually
    using the Factory. Thankfully, we can use the injected repository to set the key value */
    val preferencesViewModel by lazy {
        val extras = MutableCreationExtras().apply {
            set(SenPreferencesViewModel.REPOSITORY_KEY, preferencesRepository)
        }

        ViewModelProvider(
            store = this.viewModelStore,
            factory = SenPreferencesViewModel.Factory,
            defaultCreationExtras = extras,
        )[SenPreferencesViewModel::class.java]
    }

    val stateViewModel by lazy {
        ViewModelProvider(
            store = this.viewModelStore,
            factory = SenBoardStateViewModel.Factory,
        )[SenBoardStateViewModel::class.java]
    }

    @Suppress("UNUSED")
    val userBigramViewModel by lazy {
        val extras = MutableCreationExtras().apply {
            set(UserBigramViewModel.REPOSITORY_KEY, userBigramRepository)
        }

        ViewModelProvider(
            store = this.viewModelStore,
            factory = UserBigramViewModel.Factory,
            defaultCreationExtras = extras,
        )[UserBigramViewModel::class.java]
    }

    override fun onCreate() {
        super.onCreate()

        // We load the dataset only when the keyboard is created, and never afterward
        bigramEngine.clearDataset().loadDataset()
    }

    // The keyboard's dimensions for setting touchable region (see onComputeInsets)
    private var dimensions = IntRect.Zero

    override fun onComputeInsets(
        outInsets: Insets?,
    ) {
        super.onComputeInsets(outInsets)

        outInsets?.apply {
            contentTopInsets = dimensions.top
            visibleTopInsets = dimensions.top
            touchableInsets = Insets.TOUCHABLE_INSETS_REGION
            touchableRegion.set(dimensions.toAndroidRect())
        }
    }

    override fun onStartInputView(
        editorInfo: EditorInfo?,
        restarting: Boolean,
    ) {
        super.onStartInputView(editorInfo, restarting)

        val preferencesState = preferencesViewModel.preferencesState.value

        currentInputConnection.finishComposingText()

        stateViewModel.updateShiftMode(
            SenBoardStateDefaults.shiftMode(
                preferencesState.autoCapitalizationEnabled,
            ),
        )
        stateViewModel.updateComposingText(String.EMPTY)
        // We don't have a bigram for empty string yet so we clear the suggestion
        stateViewModel.updateWordSuggestions(emptyList())
    }

    override fun onCreateInputView(): View {
        handleLifecycleOnStartEvent()

        return ComposeView(this).apply {
            setViewTreeOwners()

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            setContent {
                window.window?.let { window ->
                    WindowCompat.enableEdgeToEdge(window)
                    WindowCompat.getInsetsController(window, window.decorView) //
                        .isAppearanceLightNavigationBars = !isSystemInDarkTheme()
                }

                val uiState by stateViewModel.uiState.collectAsStateWithLifecycle()
                val preferencesState by preferencesViewModel.preferencesState.collectAsStateWithLifecycle()
                // val userBigramDataset by userBigramViewModel.bigramDataset.collectAsStateWithLifecycle()

                val haptic = LocalHapticFeedback.current

                SenTheme {
                    BoxWithConstraints(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        SenBoardScaffold(
                            topBar = {
                                SenToolbar(
                                    modifier = Modifier
                                        .widthIn(max = SenBoardDefaults.MaxWidth)
                                        .fillMaxWidth(),
                                ) {
                                    SenEngineSwitcher(
                                        engineType = preferencesState.vietnameseEngineType,
                                        onEngineSwitch = preferencesViewModel::updateVietnameseEngineType,
                                    ) {
                                        SenEngineIcon(preferencesState.vietnameseEngineType)
                                    }

                                    // Too lazy to not also update state when this option is disabled :b
                                    if (preferencesState.wordSuggestionsEnabled) {
                                        SenSuggestions(
                                            suggestions = uiState.wordSuggestions,
                                            onSuggestionChoose = { suggestion ->
                                                /* This will also replace composing text, which is intended
                                                when the suggestions are closest words and not bigram candidates
                                                ====================
                                                Additionally, include a whitespace so users don't have to press space
                                                afterward, in other words, basically how any keyboard app works :D */
                                                currentInputConnection.commitText("$suggestion ", 1)

                                                stateViewModel.updateShiftMode(
                                                    when (uiState.shiftMode) {
                                                        ShiftMode.Shifted -> ShiftMode.Off
                                                        else -> uiState.shiftMode
                                                    },
                                                )
                                                stateViewModel.updateComposingText(String.EMPTY)
                                                stateViewModel.updateWordSuggestions(
                                                    bigramEngine.getBestCandidates<Nothing>(suggestion),
                                                )
                                            },
                                            modifier = Modifier.weight(1f),
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }

                                    if ( //
                                        preferencesState.easterEggsEnabled //
                                        && preferencesState.aaaaaModeEnabled
                                    ) {
                                        IconToggleButton(
                                            checked = uiState.modeType == SenModeType.Aaaaa,
                                            onCheckedChange = {
                                                stateViewModel.updateModeType(
                                                    when (uiState.modeType) {
                                                        SenModeType.Aaaaa -> SenModeType.Characters
                                                        else -> SenModeType.Aaaaa
                                                    },
                                                )
                                            },
                                        ) {
                                            Icon(
                                                imageVector = when (uiState.modeType) {
                                                    SenModeType.Aaaaa -> Icons.Filled.AutoAwesome
                                                    else -> Icons.Outlined.AutoAwesome
                                                },
                                                contentDescription = "Bật/tắt chế độ aaaaa",
                                            )
                                        }
                                    }
                                }
                            },
                            bottomBar = {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .navigationBarsPadding(),
                                )
                            },
                            shadowElevation = SenBoardScaffoldDefaults.shadowElevation(
                                preferencesState.keyBackgroundShadowEnabled,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SenBoardDefaults.height(maxHeight))
                                .onGloballyPositioned { coordinates ->
                                    dimensions = IntRect(
                                        offset = coordinates.positionInWindow().toIntOffset(),
                                        size = coordinates.size,
                                    )

                                    /* Tell the input view that the layout has changed,
                                    this is for insets and touchable region to be re-computed */
                                    window.window?.decorView?.requestLayout()
                                },
                        ) {
                            SenBoard(
                                /* I still don't know how to extract mode and layout out without
                                breaking the long tap for the second time, I hate Android development */
                                layout = provideMode(uiState.modeType) //
                                    .invoke(uiState, preferencesState) //
                                    .let { mode ->
                                        provideLayout(mode.layoutType) //
                                            .invoke(uiState, preferencesState)
                                    },
                                popup = {},
                                /* If I extract the context outside, there is a bug in which the long tap
                                for char key handler will just keep repeating itself */
                                onKeyTap = { index ->
                                    val keyData = provideMode(uiState.modeType) //
                                        .invoke(uiState, preferencesState) //
                                        .keyDatas[index]
                                    val context = SenKeyHandlerContext(
                                        imService = this@SenImService,
                                        uiState = uiState,
                                        preferencesState = preferencesState,
                                        onUpdateModeType = stateViewModel::updateModeType,
                                        onUpdateShiftMode = stateViewModel::updateShiftMode,
                                        onUpdateComposingText = stateViewModel::updateComposingText,
                                        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                        onGetClosestWords = { text -> bigramEngine.getClosestWords<Nothing>(text) },
                                        onGetBestCandidates = { entryText -> bigramEngine.getBestCandidates<Nothing>(entryText) },
                                    )
                                    keyData.handler.handleTap(context)
                                },
                                onKeyDoubleTap = { index ->
                                    val keyData = provideMode(uiState.modeType).invoke(uiState, preferencesState).keyDatas[index]
                                    val context = SenKeyHandlerContext(
                                        imService = this@SenImService,
                                        uiState = uiState,
                                        preferencesState = preferencesState,
                                        onUpdateModeType = stateViewModel::updateModeType,
                                        onUpdateShiftMode = stateViewModel::updateShiftMode,
                                        onUpdateComposingText = stateViewModel::updateComposingText,
                                        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                        onGetClosestWords = { text -> bigramEngine.getClosestWords<Nothing>(text) },
                                        onGetBestCandidates = { entryText -> bigramEngine.getBestCandidates<Nothing>(entryText) },
                                    )
                                    keyData.handler.handleDoubleTap(context)
                                },
                                onKeyLongTap = { index ->
                                    val keyData = provideMode(uiState.modeType) //
                                        .invoke(uiState, preferencesState) //
                                        .keyDatas[index]
                                    val context = SenKeyHandlerContext(
                                        imService = this@SenImService,
                                        uiState = uiState,
                                        preferencesState = preferencesState,
                                        onUpdateModeType = stateViewModel::updateModeType,
                                        onUpdateShiftMode = stateViewModel::updateShiftMode,
                                        onUpdateComposingText = stateViewModel::updateComposingText,
                                        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                        onGetClosestWords = { text -> bigramEngine.getClosestWords<Nothing>(text) },
                                        onGetBestCandidates = { entryText -> bigramEngine.getBestCandidates<Nothing>(entryText) },
                                    )
                                    keyData.handler.handleLongTap(context)
                                },
                                onKeyTapDown = {
                                    if (preferencesState.hapticsEnabled) {
                                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) { index, key, interactionSource ->
                                val keyData = provideMode(uiState.modeType) //
                                    .invoke(uiState, preferencesState) //
                                    .keyDatas[index]

                                val shouldOverrideState = keyData.handler is SenShiftKeyHandler //
                                        && uiState.shiftMode == ShiftMode.CapsLocked

                                SenKey(
                                    key = key,
                                    style = keyData.styleProvider(
                                        uiState,
                                        preferencesState,
                                        isSystemInDarkTheme(),
                                    ),
                                    indication = SenKeyIndication(
                                        index = index,
                                        shape = RectangleShape,
                                        color = SenKeyIndicationDefaults.color(),
                                        overrideState = if (shouldOverrideState) true else null,
                                    ),
                                    interactionSource = interactionSource,
                                ) {
                                    SenDisplay(
                                        display = keyData.display(uiState),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
