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

class SenModeScope(
    private val layoutType: SenLayoutType,
) {
    private val keyDatas: MutableList<SenKeyData> = mutableListOf()

    fun senKey(
        styleProvider: SenKeyStyleProvider,
        display: SenKeyDisplay,
        handler: SenKeyHandler,
        altProvider: SenAltProvider,
    ) = keyDatas.add(
        SenKeyData(
            styleProvider,
            display,
            handler,
            altProvider,
        ),
    )

    fun build() = SenMode(layoutType, keyDatas.toList())
}

// These should be extension functions because, they are meant to be extensions :)
fun SenModeScope.senTextKey(
    text: String,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
    altProvider: SenAltProvider = { senAltNone() },
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(text),
    handler = SenTextKeyHandler(text),
    altProvider = altProvider,
)

fun SenModeScope.senCharKey(
    char: Char,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
    altProvider: SenAltProvider = { senAltNone() },
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Char(char),
    handler = SenCharKeyHandler(char),
    altProvider = altProvider,
)

fun SenModeScope.senBackSpaceKey(
    styleProvider: SenKeyStyleProvider = senSecondaryContainerKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.Backspace),
    handler = SenBackSpaceKeyHandler,
    altProvider = { senAltNone() },
)

fun SenModeScope.senShiftKey(
    styleProvider: SenKeyStyleProvider = senSecondaryContainerKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.ShiftIcon,
    handler = SenShiftKeyHandler,
    altProvider = { senAltNone() },
)

fun SenModeScope.senSpaceKey(
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.None,
    handler = SenSpaceKeyHandler,
    altProvider = { senAltNone() },
)

fun SenModeScope.senReturnKey(
    styleProvider: SenKeyStyleProvider = senPrimaryKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Icon(Icons.AutoMirrored.Outlined.KeyboardReturn),
    handler = SenReturnKeyHandler,
    altProvider = { senAltNone() },
)

fun SenModeScope.senModeSwitcherKey(
    modeType: SenModeType,
    label: String,
    styleProvider: SenKeyStyleProvider = senTertiaryContainerKeyStyle,
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Text(label),
    handler = SenModeSwitcherKeyHandler(modeType),
    altProvider = { senAltNone() },
)

fun SenModeScope.senNumberKey(
    number: Int,
    t9Chars: String,
    styleProvider: SenKeyStyleProvider = senNeutralKeyStyle,
    altProvider: SenAltProvider = { senAltNone() },
) = senKey(
    styleProvider = styleProvider,
    display = SenKeyDisplay.Number(number, t9Chars),
    handler = SenTextKeyHandler(number.toString()),
    altProvider = altProvider,
)

fun senMode(
    layoutType: SenLayoutType,
    builder: SenModeScope.() -> Unit,
) = SenModeScope(layoutType)
    .apply(builder)
    .build()
