package banhmi.senboard.keyboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import banhmi.senboard.keyboard.impl.handler.SenBackSpaceKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenCharKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenModeSwitcherKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenReturnKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenShiftKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenSpaceKeyHandler
import banhmi.senboard.keyboard.impl.handler.SenTextKeyHandler

class SenModeScope(private val layout: SenLayout) {
    private val keyDatas: MutableList<SenKeyData> = mutableListOf()

    fun senKey(
        styleProvider: SenKeyStyleProvider,
        display: SenKeyDisplay,
        handler: SenKeyHandler,
    ): Boolean = keyDatas.add(
        SenKeyData(
            styleProvider,
            display,
            handler,
        )
    )

    fun build(): SenMode = SenMode(layout, keyDatas)
}

// These should be extension functions because, they are meant to be extensions :)
fun SenModeScope.senTextKey(
    text: String,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(text),
    handler = SenTextKeyHandler(text),
)

fun SenModeScope.senCharKey(
    char: Char,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Char(char),
    handler = SenCharKeyHandler(char),
)

fun SenModeScope.senBackSpaceKey(
    styleProvider: SenKeyStyleProvider = senSecondaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
    handler = SenBackSpaceKeyHandler,
)

fun SenModeScope.senShiftKey(
    styleProvider: SenKeyStyleProvider = senSecondaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.ShiftIcon,
    handler = SenShiftKeyHandler,
)

fun SenModeScope.senSpaceKey(
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.None,
    handler = SenSpaceKeyHandler,
)

fun SenModeScope.senReturnKey(
    styleProvider: SenKeyStyleProvider = senPrimaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
    handler = SenReturnKeyHandler,
)

fun SenModeScope.senModeSwitcherKey(
    modeType: SenModeType,
    label: String,
    styleProvider: SenKeyStyleProvider = senTertiaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(label),
    handler = SenModeSwitcherKeyHandler(modeType),
)

fun senMode(layout: SenLayout, builder: SenModeScope.() -> Unit): SenMode = SenModeScope(layout)
    .apply(builder)
    .build()
