package banhmi.senboard.ime

import android.view.View
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import banhmi.senboard.ime.keyboard.ui.SenBoardRoot
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.data.modes.AaaaaMode
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.ui.SenBoardContent
import banhmi.senboard.ime.keyboard.ui.SenBoardLayout
import banhmi.senboard.ime.keyboard.ui.toolbar.Toolbar
import banhmi.senboard.ime.lifecycle.LifecycleImService
import banhmi.senboard.ui.theme.SenBoardTheme

class SenImService : LifecycleImService() {
    private val context by lazy { SenBoardContext(im = this) }

    val controller by lazy { SenBoardController(context) }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateInputView(): View {
        val context = this.context
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                SenBoardTheme {
                    SenBoardRoot {
                        SenBoardContent(controller) {
                            val state = controller.state
                            Column {
                                Toolbar {
                                    // TODO: move these buttons somewhere else
                                    val isAaaaaMode = state.mode == AaaaaMode

                                    TooltipBox(
                                        positionProvider =
                                            TooltipDefaults.rememberTooltipPositionProvider(
                                                TooltipAnchorPosition.Above),
                                        tooltip = {
                                            PlainTooltip(
                                                modifier =
                                                    Modifier.semantics {
                                                        liveRegion = LiveRegionMode.Assertive
                                                        paneTitle = "Switch Vietnamese engine"
                                                    }
                                            ) {
                                                Text("Switch Vietnamese engine")
                                            }
                                        },
                                        state = rememberTooltipState(),
                                    ) {
                                        IconButton(onClick = {}) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .border(
                                                        width = 2.dp,
                                                        color = LocalContentColor.current,
                                                        shape = RoundedCornerShape(4.dp),
                                                    ),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                Text(
                                                    "V",
                                                    color = LocalContentColor.current,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            }
                                        }
                                    }

                                    TooltipBox(
                                        positionProvider =
                                            TooltipDefaults.rememberTooltipPositionProvider(
                                                TooltipAnchorPosition.Above),
                                        tooltip = {
                                            PlainTooltip(
                                                modifier =
                                                    Modifier.semantics {
                                                        liveRegion = LiveRegionMode.Assertive
                                                        paneTitle = "Toggle aaaaa"
                                                    }
                                            ) {
                                                Text("Mystery mode")
                                            }
                                        },
                                        state = rememberTooltipState(),
                                    ) {
                                        FilledIconToggleButton(
                                            checked = isAaaaaMode,
                                            onCheckedChange = {
                                                context.state = state.copy(
                                                    mode = if (isAaaaaMode) CharactersMode else AaaaaMode
                                                )
                                            },
                                        ) {
                                            if (isAaaaaMode) Icon(
                                                Icons.Filled.AutoFixHigh,
                                                contentDescription = "Turn off aaaaa",
                                            ) else Icon(
                                                Icons.Outlined.AutoFixHigh,
                                                contentDescription = "Turn on aaaaa",
                                            )
                                        }
                                    }

                                    TooltipBox(
                                        positionProvider =
                                            TooltipDefaults.rememberTooltipPositionProvider(
                                                TooltipAnchorPosition.Above),
                                        tooltip = {
                                            PlainTooltip(
                                                modifier =
                                                    Modifier.semantics {
                                                        liveRegion = LiveRegionMode.Assertive
                                                        paneTitle = "Open settings"
                                                    }
                                            ) {
                                                Text("Open settings")
                                            }
                                        },
                                        state = rememberTooltipState(),
                                    ) {
                                        IconButton(onClick = {}) {
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
