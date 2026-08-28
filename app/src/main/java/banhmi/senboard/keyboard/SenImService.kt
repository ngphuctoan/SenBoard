package banhmi.senboard.keyboard

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
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
import banhmi.senboard.keyboard.model.SenAlt
import banhmi.senboard.keyboard.model.SenKeyHandlerContext
import banhmi.senboard.keyboard.model.senAltNone
import banhmi.senboard.keyboard.state.SenBoardStateDefaults
import banhmi.senboard.keyboard.state.SenBoardStateViewModel
import banhmi.senboard.keyboard.state.ShiftMode
import banhmi.senboard.keyboard.ui.SenAltPopup
import banhmi.senboard.keyboard.ui.SenAltPopupDefaults
import banhmi.senboard.keyboard.ui.SenBoard
import banhmi.senboard.keyboard.ui.SenBoardScaffold
import banhmi.senboard.keyboard.ui.SenBoardScaffoldDefaults
import banhmi.senboard.keyboard.ui.SenDisplay
import banhmi.senboard.keyboard.ui.SenKey
import banhmi.senboard.keyboard.ui.SenKeyIndication
import banhmi.senboard.keyboard.ui.SenKeyIndicationDefaults
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

                var previousComposedText by remember { mutableStateOf("") }
                var alt: SenAlt by remember { mutableStateOf(senAltNone()) }
                var altPopupVisible by remember { mutableStateOf(false) }

                if (uiState.composingText.isBlank()) {
                    val composedText = preferencesState.vietnameseEngine.convertWord(previousComposedText)
                    stateViewModel.updateWordSuggestions(bigramEngine.getBestCandidates<Nothing>(composedText))
                } else {
                    previousComposedText = uiState.composingText
                }

                SenTheme {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        SenBoardScaffold(
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
                                layout = uiState.mode.layout,
                                popup = { topLeftOffset, tapPosition ->
                                    if (alt is SenAlt.Popup && altPopupVisible) {
                                        SenAltPopup(
                                            popup = alt as SenAlt.Popup,
                                            offset = Offset(tapPosition.x, topLeftOffset.y),
                                            shadowElevation = SenAltPopupDefaults.shadowElevation(
                                                preferencesState.keyBackgroundShadowEnabled,
                                            ),
                                        ) { altData ->
                                            SenDisplay(display = altData.display(uiState))
                                        }
                                    }
                                },
                                /* If I extract the context outside, there is a bug in which the long tap
                                for char key handler will just keep repeating itself */
                                onKeyTap = { index ->
                                    val keyData = uiState.mode.keyDatas[index]
                                    val context = SenKeyHandlerContext(
                                        imService = this@SenImService,
                                        uiState = uiState,
                                        preferencesState = preferencesState,
                                        onUpdateModeType = stateViewModel::updateModeType,
                                        onUpdateShiftMode = stateViewModel::updateShiftMode,
                                        onUpdateComposingText = stateViewModel::updateComposingText,
                                        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                    )
                                    keyData.handler.handleTap(context)
                                },
                                onKeyDoubleTap = { index ->
                                    val keyData = uiState.mode.keyDatas[index]
                                    val context = SenKeyHandlerContext(
                                        imService = this@SenImService,
                                        uiState = uiState,
                                        preferencesState = preferencesState,
                                        onUpdateModeType = stateViewModel::updateModeType,
                                        onUpdateShiftMode = stateViewModel::updateShiftMode,
                                        onUpdateComposingText = stateViewModel::updateComposingText,
                                        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                    )
                                    keyData.handler.handleDoubleTap(context)
                                },
                                onKeyLongTap = { index ->
                                    val keyData = uiState.mode.keyDatas[index]

                                    when (val newAlt = keyData.altProvider()) {
                                        is SenAlt.Popup -> {
                                            if (alt != newAlt) alt = newAlt
                                            if (!altPopupVisible) altPopupVisible = true
                                        }

                                        SenAlt.None -> {
                                            val context = SenKeyHandlerContext(
                                                imService = this@SenImService,
                                                uiState = uiState,
                                                preferencesState = preferencesState,
                                                onUpdateModeType = stateViewModel::updateModeType,
                                                onUpdateShiftMode = stateViewModel::updateShiftMode,
                                                onUpdateComposingText = stateViewModel::updateComposingText,
                                                onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
                                            )
                                            keyData.handler.handleLongTap(context)
                                        }
                                    }
                                },
                                onKeyTapUp = {
                                    altPopupVisible = false
                                },
                                onKeyTapCancel = {
                                    altPopupVisible = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                            ) { index, key, interactionSource ->
                                val keyData = uiState.mode.keyDatas[index]

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

                        /* SenBoardScaffold(
                            topBar = {
                                Row(modifier = Modifier.widthIn(max = SenKeyDefaults.ShapeMaxWidth * layout.maxTotalAreaWidthMultipliers)) {
                                    SenEngineSwitcher(
                                        engineType = preferencesState.vietnameseEngineType,
                                        onEngineSwitch = { engineType ->
                                            currentInputConnection.finishComposingText()
                                            preferencesViewModel.updateVietnameseEngineType(engineType)
                                            stateViewModel.updateComposingText(String.EMPTY)
                                        },
                                    ) { engineType ->
                                        SenEngineIcon(engineType)
                                    }

                                    SenSuggestions(
                                        suggestions = uiState.wordSuggestions,
                                        modifier = Modifier.weight(1f),
                                    )

                                    if (preferencesState.easterEggsEnabled) {
                                        IconToggleButton(
                                            checked = mode == SenAaaaaMode,
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
                                                imageVector = if (mode == SenAaaaaMode) {
                                                    Icons.Filled.AutoAwesome
                                                } else {
                                                    Icons.Outlined.AutoAwesome
                                                },
                                                contentDescription = null,
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    dimensions = IntRect(
                                        coordinates.positionInWindow().toIntOffset(),
                                        coordinates.size,
                                    )

                                    /* Tell the input view that the layout has changed, this is for
                                    insets and touchable region to be re-computed */
                                    window.window?.decorView?.requestLayout()
                                },
                        ) {
                            SenBoard(
                                layout = layout,
                                onKeyTap = { index ->
                                    mode.keyDatas[index].handler.handleTap(handlerContext)
                                },
                                onKeyDoubleTap = { index ->
                                    mode.keyDatas[index].handler.handleDoubleTap(handlerContext)
                                },
                                onKeyLongTap = { index ->
                                    mode.keyDatas[index].handler.handleLongTap(handlerContext)
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) { _, index, key, interactionSource ->
                                val keyData = mode.keyDatas[index]
                                val style = keyData.styleProvider(uiState, preferencesState)

                                val overrideState = if (keyData.handler is SenShiftKeyHandler && uiState.shiftMode == ShiftMode.CapsLocked) {
                                    true
                                } else {
                                    null
                                }

                                CompositionLocalProvider(LocalTextStyle provides style.textStyle) {
                                    SenKey(
                                        style = style,
                                        areaWidthMultiplier = key.areaWidthMultiplier,
                                        shapeWidthProportion = key.shapeWidthProportion,
                                        shapeAlignment = key.shapeAlignment,
                                        interactionSource = interactionSource,
                                        indication = SenKeyIndication(
                                            overrideState = overrideState,
                                            index = index,
                                            color = SenKeyIndicationDefaults.color(),
                                            shape = RectangleShape,
                                        ),
                                        modifier = Modifier.padding(3.dp, 6.dp),
                                    ) {
                                        when (val display = keyData.display(uiState)) {
                                            is SenKeyDisplay.Text -> Text(
                                                text = display.text,
                                                autoSize = TextAutoSize.StepBased(maxFontSize = 24.sp),
                                                maxLines = 1,
                                            )

                                            is SenKeyDisplay.Icon -> Icon(
                                                imageVector = display.icon,
                                                contentDescription = display.description,
                                                modifier = Modifier
                                                    .sizeIn(maxWidth = 24.dp, maxHeight = 24.dp)
                                                    .fillMaxSize()
                                                    .rotate(display.transforms.rotation),
                                            )

                                            else -> {}
                                        }
                                    }
                                }
                            }
                        } */
                    }
                }
            }
        }
    }
}
