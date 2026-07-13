package banhmi.senboard.ime

import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.ComposeView
import banhmi.senboard.ime.keyboard.ui.SenBoardRoot
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.ui.SenBoardLayout
import banhmi.senboard.ime.keyboard.ui.toolbar.Toolbar
import banhmi.senboard.ime.lifecycle.LifecycleImService
import banhmi.senboard.ui.theme.SenBoardTheme

class SenImService : LifecycleImService() {
    private val context by lazy { SenBoardContext(im = this) }

    val controller by lazy { SenBoardController(context) }

    @OptIn(
        ExperimentalMaterial3AdaptiveApi::class,
        ExperimentalMaterial3WindowSizeClassApi::class,
    )
    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                val windowSizeClass = WindowSizeClass.calculateFromSize(currentWindowDpSize())

                SenBoardTheme {
                    SenBoardRoot(
                        controller = controller,
                        widthSizeClass = windowSizeClass.widthSizeClass,
                    ) {
                        val state = controller.state

                        Column {
                            Toolbar { }
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
