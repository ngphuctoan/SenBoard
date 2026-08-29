package banhmi.senboard.keyboard

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import banhmi.senboard.SenActivity
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesRepository
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.engine.provideVietnameseEngine
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.data.SenBoardStateViewModel
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.impl.handler.SenShiftKeyHandler
import banhmi.senboard.keyboard.impl.mode.SenAaaaaMode
import banhmi.senboard.keyboard.impl.mode.SenCharactersMode
import banhmi.senboard.keyboard.impl.mode.SenCharactersModeWithNumberRow
import banhmi.senboard.keyboard.lifecycle.SenLifecycleImService
import banhmi.senboard.keyboard.model.SenKeyDisplay
import banhmi.senboard.keyboard.proxy.SenImServiceProxy
import banhmi.senboard.keyboard.ui.SenBoard
import banhmi.senboard.keyboard.ui.SenBoardScaffold
import banhmi.senboard.keyboard.ui.SenBoardScaffoldDefaults
import banhmi.senboard.keyboard.ui.SenEngineIcon
import banhmi.senboard.keyboard.ui.SenEngineSwitcher
import banhmi.senboard.keyboard.ui.SenKey
import banhmi.senboard.keyboard.ui.SenKeyDefaults
import banhmi.senboard.keyboard.ui.SenKeyIndication
import banhmi.senboard.keyboard.ui.SenKeyIndicationDefaults
import banhmi.senboard.keyboard.ui.SenSuggestions
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.toIntOffset
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SenImService : SenLifecycleImService() {
    @Inject
    lateinit var preferencesRepository: SenPreferencesRepository

    /* @HiltViewModel only works with Activity class, so we have to construct the view model manually
    using the Factory. Thankfully, we can use the injected repository to set the key value */
    private val preferencesViewModel: SenPreferencesViewModel by lazy {
        val extras = MutableCreationExtras().apply {
            set(SenPreferencesViewModel.REPOSITORY_KEY, preferencesRepository)
        }

        ViewModelProvider(
            store = this.viewModelStore,
            factory = SenPreferencesViewModel.Factory,
            defaultCreationExtras = extras,
        )[SenPreferencesViewModel::class.java]
    }

    private val stateViewModel: SenBoardStateViewModel by lazy {
        ViewModelProvider(
            store = this.viewModelStore,
            factory = SenBoardStateViewModel.Factory,
        )[SenBoardStateViewModel::class.java]
    }

    // The keyboard's dimensions for setting touchable region (see onComputeInsets)
    private var dimensions: IntRect = IntRect.Zero

    override fun onComputeInsets(outInsets: Insets?) {
        super.onComputeInsets(outInsets)

        outInsets?.apply {
            contentTopInsets = dimensions.top
            visibleTopInsets = dimensions.top
            touchableInsets = Insets.TOUCHABLE_INSETS_REGION
            touchableRegion.set(dimensions.toAndroidRect())
        }
    }

    // =========================================================================
    // FOCUS & BLUR LIFECYCLE MANAGEMENT
    // =========================================================================

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        handleInputFocusIn(attribute, restarting)
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        handleInputViewFocusActive(editorInfo, restarting)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        handleInputViewBlur(finishingInput)
    }

    override fun onFinishInput() {
        super.onFinishInput()
        handleInputUnfocusBlur()
    }

    override fun onWindowHidden() {
        super.onWindowHidden()
        handleWindowHiddenBlur()
    }

    override fun onUpdateSelection(
        oldSelStart: Int,
        oldSelEnd: Int,
        newSelStart: Int,
        newSelEnd: Int,
        candidatesStart: Int,
        candidatesEnd: Int,
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        handleSelectionChangeBlur(newSelStart, newSelEnd, candidatesStart, candidatesEnd)
    }

    // -------------------------------------------------------------------------
    // CLEAN HELPER FUNCTIONS FOR EACH FOCUS / BLUR CASE
    // -------------------------------------------------------------------------

    private fun commitAndFinishComposingText() {
        val state = stateViewModel.uiState.value
        val currentConnection = currentInputConnection
        if (currentConnection != null && state.composingText.isNotEmpty()) {
            val preferences = preferencesViewModel.preferences.value
            val engine = provideVietnameseEngine(preferences.vietnameseEngineType)
            val converted = engine.convertWord(state.composingText)
            currentConnection.commitText(converted, 1)
            currentConnection.finishComposingText()
        } else {
            currentConnection?.finishComposingText()
        }
    }

    private fun handleInputFocusIn(attribute: EditorInfo?, restarting: Boolean) {
        // Initialize or prepare keyboard state when an input field gains focus
    }

    private fun handleInputViewFocusActive(editorInfo: EditorInfo?, restarting: Boolean) {
        commitAndFinishComposingText()

        val state = stateViewModel.uiState.value
        val preferences = preferencesViewModel.preferences.value

        stateViewModel.setState(
            state.copy(
                shiftMode = SenBoardStateDefaults.shiftMode(preferences.autoCapitalizationEnabled),
                composingText = SenBoardStateDefaults.EmptyComposingText,
            ),
        )
    }

    private fun handleInputViewBlur(finishingInput: Boolean) {
        commitAndFinishComposingText()

        val state = stateViewModel.uiState.value
        stateViewModel.setState(
            state.copy(
                composingText = SenBoardStateDefaults.EmptyComposingText,
            ),
        )
    }

    private fun handleInputUnfocusBlur() {
        commitAndFinishComposingText()

        val state = stateViewModel.uiState.value
        stateViewModel.setState(
            state.copy(
                composingText = SenBoardStateDefaults.EmptyComposingText,
            ),
        )
    }

    private fun handleWindowHiddenBlur() {
        commitAndFinishComposingText()

        val state = stateViewModel.uiState.value
        stateViewModel.setState(
            state.copy(
                composingText = SenBoardStateDefaults.EmptyComposingText,
            ),
        )
    }

    private fun handleSelectionChangeBlur(
        newSelStart: Int,
        newSelEnd: Int,
        candidatesStart: Int,
        candidatesEnd: Int,
    ) {
        val state = stateViewModel.uiState.value
        if (state.composingText.isEmpty()) return

        val isOutsideCandidates = candidatesStart >= 0 && candidatesEnd >= 0 &&
            (newSelStart < candidatesStart || newSelStart > candidatesEnd)
        val isCursorShifted = newSelStart != newSelEnd

        if (isOutsideCandidates || isCursorShifted) {
            commitAndFinishComposingText()
            stateViewModel.setState(
                state.copy(
                    composingText = SenBoardStateDefaults.EmptyComposingText,
                ),
            )
        }
    }

    override fun onCreateInputView(): View {
        val imService = SenImServiceProxy(this)

        handleLifecycleOnStartEvent()

        return ComposeView(this).apply {
            setViewTreeOwners()

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            window.window?.let { window ->
                WindowCompat.enableEdgeToEdge(window)
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                )
            }

            setContent {
                val preferences: SenPreferences by preferencesViewModel.preferences.collectAsStateWithLifecycle()
                val state: SenBoardState by stateViewModel.uiState.collectAsStateWithLifecycle()

                // Hardware-accelerated deep window backdrop blur (120px heavy blur) for Android 12+ (API 31+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    window.window?.let { win ->
                        win.setBackgroundBlurRadius(120)
                    }
                }

                val isCapsLocked = state.shiftMode == ShiftMode.CapsLocked

                val mode = if ((state.mode == SenCharactersMode || state.mode == SenCharactersModeWithNumberRow) && preferences.numberRowEnabled) {
                    SenCharactersModeWithNumberRow
                } else if (state.mode == SenCharactersModeWithNumberRow) {
                    SenCharactersMode
                } else {
                    state.mode
                }

                val layout = mode.layout

                SenTheme {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        SenBoardScaffold(
                            topBar = {
                                Row(modifier = Modifier.widthIn(max = SenKeyDefaults.ShapeMaxWidth * layout.maxTotalAreaWidthMultipliers)) {
                                    SenEngineSwitcher(
                                        engineType = preferences.vietnameseEngineType,
                                        onEngineSwitch = { engineType ->
                                            preferencesViewModel.updateVietnameseEngineType(
                                                engineType,
                                            )
                                            commitAndFinishComposingText()
                                            stateViewModel.setState(state.copy(composingText = SenBoardStateDefaults.EmptyComposingText))
                                        },
                                    ) { engineType ->
                                        SenEngineIcon(engineType)
                                    }

                                    if (preferences.easterEggsEnabled) {
                                        IconToggleButton(
                                            checked = state.mode == SenAaaaaMode,
                                            onCheckedChange = {
                                                stateViewModel.setState(
                                                    state.copy(
                                                        mode = if (state.mode == SenAaaaaMode) {
                                                            SenCharactersMode
                                                        } else {
                                                            SenAaaaaMode
                                                        },
                                                    ),
                                                )
                                            },
                                        ) {
                                            Icon(
                                                imageVector = if (state.mode == SenAaaaaMode) {
                                                    Icons.Filled.AutoAwesome
                                                } else {
                                                    Icons.Outlined.AutoAwesome
                                                },
                                                contentDescription = null,
                                            )
                                        }
                                    }

                                    val suggestions = if (state.composingText.isNotEmpty()) {
                                        val engine = provideVietnameseEngine(preferences.vietnameseEngineType)
                                        listOf(engine.convertWord(state.composingText))
                                    } else {
                                        emptyList()
                                    }

                                    SenSuggestions(
                                        suggestions = suggestions,
                                        modifier = Modifier.weight(1f),
                                    )

                                    IconButton(
                                        onClick = {
                                            startActivity(
                                                Intent(
                                                    this@SenImService, SenActivity::class.java,
                                                ).apply {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                },
                                            )
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Settings,
                                            contentDescription = null,
                                        )
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
                            colors = SenBoardScaffoldDefaults.colors(),
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
                                    mode.keyDatas[index].handler.handleTap(
                                        state = state,
                                        onSetState = stateViewModel::setState,
                                        preferences = preferences,
                                        imService = imService,
                                        onSaveBigram = { _, _ -> },
                                    )
                                },
                                onKeyDoubleTap = { index ->
                                    mode.keyDatas[index].handler.handleDoubleTap(
                                        state = state,
                                        onSetState = stateViewModel::setState,
                                        preferences = preferences,
                                        imService = imService,
                                        onSaveBigram = { _, _ -> },
                                    )
                                },
                                onKeyLongTap = { index ->
                                    mode.keyDatas[index].handler.handleLongTap(
                                        state = state,
                                        onSetState = stateViewModel::setState,
                                        preferences = preferences,
                                        imService = imService,
                                        onSaveBigram = { _, _ -> },
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) { _, index, key, interactionSource ->
                                val keyData = mode.keyDatas[index]

                                val style = keyData.styleProvider(state, preferences)

                                val overrideState =
                                    if (keyData.handler is SenShiftKeyHandler && isCapsLocked) {
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
                                        when (val display = keyData.display(state)) {
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
                        }
                    }
                }
            }
        }
    }
}
