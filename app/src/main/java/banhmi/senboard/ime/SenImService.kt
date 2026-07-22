package banhmi.senboard.ime

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.outlined.KeyboardHide
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.ime.keyboard.ui.SenBoardRoot
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.data.modes.AaaaaMode
import banhmi.senboard.ime.keyboard.ui.SenBoardContent
import banhmi.senboard.ime.keyboard.ui.SenBoardLayout
import banhmi.senboard.ime.keyboard.ui.SenBoardSurface
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbar
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbarButton
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbarSwitchEngineIcon
import banhmi.senboard.ime.lifecycle.LifecycleImService
import banhmi.senboard.shared.settings.SenSettingsViewModel
import banhmi.senboard.ui.theme.SenBoardTheme
import kotlin.getValue

class SenImService : LifecycleImService() {
    private val settingsViewModel by lazy {
        ViewModelProvider(this, SenSettingsViewModel.Factory)[SenSettingsViewModel::class.java]
    }

    private val context by lazy {
        SenBoardContext(
            im = this,
            inputMethodStateFlow = settingsViewModel.inputMethodState,
            appearanceStateFlow = settingsViewModel.appearanceState,
            soundsAndHapticsStateFlow = settingsViewModel.soundsAndHapticsState,
        )
    }

    val controller by lazy { SenBoardController(context) }

    override fun onStartInput(
        attribute: EditorInfo?,
        restarting: Boolean,
    ) {
        super.onStartInput(attribute, restarting)
        controller.updateShiftModeByContext()
    }

    override fun onUpdateSelection(
        oldSelStart: Int,
        oldSelEnd: Int,
        newSelStart: Int,
        newSelEnd: Int,
        candidatesStart: Int,
        candidatesEnd: Int,
    ) {
        super.onUpdateSelection(
            oldSelStart,
            oldSelEnd,
            newSelStart,
            newSelEnd,
            candidatesStart,
            candidatesEnd,
        )
        controller.updateShiftModeByContext()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                val appearanceState by controller.context.appearanceStateFlow.collectAsStateWithLifecycle()

                SenBoardTheme(oled = appearanceState.oledThemeEnabled) {
                    SenBoardRoot(onDismiss = { requestHideSelf(0) }) {
                        SenBoardSurface {
                            SenBoardContent(controller) {
                                val state = controller.state
                                Column {
                                    SenToolbar {
                                        // TODO: move these buttons somewhere else
                                        val isAaaaaMode = state.mode == AaaaaMode

                                        SenToolbarButton(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Outlined.ArrowForwardIos,
                                                contentDescription = "Hide word predictions",
                                            )
                                        }

                                        SenToolbarButton {
                                            SenToolbarSwitchEngineIcon()
                                        }

                                        SenToolbarButton {
                                            if (isAaaaaMode) Icon(
                                                Icons.Filled.Redeem,
                                                contentDescription = "Turn off aaaaa",
                                            ) else Icon(
                                                Icons.Outlined.Redeem,
                                                contentDescription = "Turn on aaaaa",
                                            )
                                        }

                                        SenToolbarButton {
                                            Icon(
                                                Icons.Outlined.Settings,
                                                contentDescription = "Open settings",
                                            )
                                        }

                                        SenToolbarButton {
                                            Icon(
                                                Icons.Outlined.KeyboardHide,
                                                contentDescription = "Hide keyboard",
                                            )
                                        }
                                    }
                                    SenBoardLayout(
                                        mode = state.mode,
                                        onKeyTap = { controller.handle(it) },
                                        onKeyDoubleTap = { controller.handleDoubleTap(it) },
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
