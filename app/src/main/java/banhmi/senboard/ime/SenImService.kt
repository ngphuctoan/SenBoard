package banhmi.senboard.ime

import android.content.Intent
import android.view.View
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import banhmi.senboard.SenActivity
import banhmi.senboard.ime.engine.PredictionEngine
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.data.modes.AaaaaMode
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.ui.SenBoardContent
import banhmi.senboard.ime.keyboard.ui.SenBoardLayout
import banhmi.senboard.ime.keyboard.ui.SenBoardRoot
import banhmi.senboard.ime.keyboard.ui.SenBoardSurface
import banhmi.senboard.ime.keyboard.ui.toolbar.Toolbar
import banhmi.senboard.ime.lifecycle.LifecycleImService
import banhmi.senboard.shared.utils.isAppInDarkTheme
import banhmi.senboard.ui.theme.SenBoardTheme

class SenImService : LifecycleImService() {
    private val context by lazy { SenBoardContext(im = this) }

    val controller by lazy { SenBoardController(context) }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateInputView(): View {
        val context = this.context

        // Load bigrams for next-word prediction
        PredictionEngine.loadBigrams(this)

        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                SenBoardTheme(darkTheme = isAppInDarkTheme()) {
                    SenBoardRoot(onDismiss = { requestHideSelf(0) }) {
                        SenBoardSurface {
                            SenBoardContent(controller) {
                                val state = controller.state
//                                val prefs = remember { banhmi.senboard.app.settings.SenBoardPreferences(this@SenImService) }
//                                var typingMode by remember { mutableStateOf(prefs.typingMode) }

                                Column {
                                    Toolbar {
                                        val isAaaaaMode = state.mode == AaaaaMode

                                        TooltipBox(
                                            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                                TooltipAnchorPosition.Above
                                            ),
                                            tooltip = {
                                                PlainTooltip(
                                                    modifier = Modifier.semantics {
                                                        liveRegion = LiveRegionMode.Assertive
                                                        paneTitle = "Chuyển phương thức gõ"
                                                    }) {
//                                                    Text(
//                                                        when (typingMode) {
//                                                            "cvnss" -> "Bộ gõ: CVNSS 4.0"
//                                                            "telex" -> "Bộ gõ: Telex"
//                                                            else -> "Bộ gõ: VNI"
//                                                        }
//                                                    )
                                                }
                                            },
                                            state = rememberTooltipState(),
                                        ) {
                                            IconButton(onClick = {
//                                                val nextMode = when (typingMode) {
//                                                    "cvnss" -> "telex"
//                                                    "telex" -> "vni"
//                                                    else -> "cvnss"
//                                                }
//                                                prefs.typingMode = nextMode
//                                                typingMode = nextMode
                                            }) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .border(
                                                            width = 2.dp,
                                                            color = LocalContentColor.current,
                                                            shape = RoundedCornerShape(4.dp),
                                                        ),
                                                    contentAlignment = Alignment.Center,
                                                ) {
//                                                    Text(
//                                                        text = when (typingMode) {
//                                                            "cvnss" -> "CV"
//                                                            "telex" -> "TE"
//                                                            else -> "VN"
//                                                        },
//                                                        color = LocalContentColor.current,
//                                                        fontWeight = FontWeight.Bold,
//                                                        fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
//                                                    )
                                                }
                                            }
                                        }

                                        if (true) {
                                            TooltipBox(
                                                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                                    TooltipAnchorPosition.Above
                                                ),
                                                tooltip = {
                                                    PlainTooltip(
                                                        modifier = Modifier.semantics {
                                                            liveRegion = LiveRegionMode.Assertive
                                                            paneTitle = "Toggle aaaaa"
                                                        }) {
                                                        Text("Mystery mode")
                                                    }
                                                },
                                                state = rememberTooltipState(),
                                            ) {
                                                IconToggleButton(
                                                    checked = isAaaaaMode,
                                                    onCheckedChange = {
                                                        context.state = state.copy(
                                                            mode = if (isAaaaaMode) CharactersMode else AaaaaMode
                                                        )
                                                    },
                                                ) {
                                                    if (isAaaaaMode) Icon(
                                                        Icons.Filled.Redeem,
                                                        contentDescription = "Turn off aaaaa",
                                                    ) else Icon(
                                                        Icons.Outlined.Redeem,
                                                        contentDescription = "Turn on aaaaa",
                                                    )
                                                }
                                            }
                                        }

                                        TooltipBox(
                                            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                                TooltipAnchorPosition.Above
                                            ),
                                            tooltip = {
                                                PlainTooltip(
                                                    modifier = Modifier.semantics {
                                                        liveRegion = LiveRegionMode.Assertive
                                                        paneTitle = "Open settings"
                                                    }) {
                                                    Text("Open settings")
                                                }
                                            },
                                            state = rememberTooltipState(),
                                        ) {
                                            IconButton(onClick = {
                                                val intent = Intent(
                                                    this@SenImService,
                                                    SenActivity::class.java,
                                                ).apply {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                }
                                                startActivity(intent)
                                                requestHideSelf(0)
                                            }) {
                                                Icon(
                                                    Icons.Outlined.Settings,
                                                    contentDescription = "Open settings",
                                                )
                                            }
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
