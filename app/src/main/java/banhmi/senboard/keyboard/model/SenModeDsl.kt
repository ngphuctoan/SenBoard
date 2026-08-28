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
        alt: SenAlt,
    ) = keyDatas.add(
        SenKeyData(
            styleProvider,
            display,
            handler,
            alt,
        ),
    )

    fun build() = SenMode(layout, keyDatas.toList())
}

// These should be extension functions because, they are meant to be extensions :)
fun SenModeScope.senTextKey(
    text: String,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
    altProvider: () -> SenAlt = { SenAlt.None },
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(text),
    handler = SenTextKeyHandler(text),
    alt = altProvider(),
)

fun SenModeScope.senCharKey(
    char: Char,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
    altProvider: () -> SenAlt = { SenAlt.None },
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Char(char),
    handler = SenCharKeyHandler(char),
    alt = altProvider(),
)

fun SenModeScope.senBackSpaceKey(
    styleProvider: SenKeyStyleProvider = senSecondaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
    handler = SenBackSpaceKeyHandler,
    alt = SenAlt.None,
)

fun SenModeScope.senShiftKey(
    styleProvider: SenKeyStyleProvider = senSecondaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.ShiftIcon,
    handler = SenShiftKeyHandler,
    alt = SenAlt.None,
)

fun SenModeScope.senSpaceKey(
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.None,
    handler = SenSpaceKeyHandler,
    alt = SenAlt.None,
)

fun SenModeScope.senReturnKey(
    styleProvider: SenKeyStyleProvider = senPrimaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
    handler = SenReturnKeyHandler,
    alt = SenAlt.None,
)

fun SenModeScope.senModeSwitcherKey(
    modeType: SenModeType,
    label: String,
    styleProvider: SenKeyStyleProvider = senTertiaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(label),
    handler = SenModeSwitcherKeyHandler(modeType),
    alt = SenAlt.None,
)

fun senMode(
    layout: SenLayout,
    builder: SenModeScope.() -> Unit,
) = SenModeScope(layout)
    .apply(builder)
    .build()
