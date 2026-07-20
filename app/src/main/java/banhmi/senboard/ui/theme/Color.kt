package banhmi.senboard.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
    val colorContainerSoft: Color,
    val onColorContainerSoft: Color,
    val colorFixed: Color,
    val colorFixedDim: Color,
    val onColorFixed: Color,
    val onColorFixedVariant: Color,
)

/**
 * Rainbow colors defined using OKLCH consistency for uniform perceptual brightness.
 */
@Immutable
data class RainbowColors(
    val red: ColorFamily,
    val rose: ColorFamily,
    val pink: ColorFamily,
    val fuchsia: ColorFamily,
    val purple: ColorFamily,
    val violet: ColorFamily,
    val indigo: ColorFamily,
    val blue: ColorFamily,
    val sky: ColorFamily,
    val cyan: ColorFamily,
    val teal: ColorFamily,
    val emerald: ColorFamily,
    val green: ColorFamily,
    val lime: ColorFamily,
    val yellow: ColorFamily,
    val amber: ColorFamily,
    val orange: ColorFamily,
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// --- LIGHT PALETTES ---
val LightRed = ColorFamily(
    color = Color(0xFFE52525), onColor = Color.White,
    colorContainer = Color(0xFFFFDAD6), onColorContainer = Color(0xFF410002),
    colorContainerSoft = Color(0xFFFFD8D5), onColorContainerSoft = Color(0xFF8D0008),
    colorFixed = Color(0xFFFFDAD6), colorFixedDim = Color(0xFFFFB4AB),
    onColorFixed = Color(0xFF410002), onColorFixedVariant = Color(0xFF93000A)
)
val LightRose = ColorFamily(
    color = Color(0xFFE31B5F), onColor = Color.White,
    colorContainer = Color(0xFFFFD9E2), onColorContainer = Color(0xFF3E001D),
    colorContainerSoft = Color(0xFFFFE1E8), onColorContainerSoft = Color(0xFF8A003D),
    colorFixed = Color(0xFFFFD9E2), colorFixedDim = Color(0xFFFFB1C8),
    onColorFixed = Color(0xFF3E001D), onColorFixedVariant = Color(0xFF980047)
)
val LightOrange = ColorFamily(
    color = Color(0xFFB35F00), onColor = Color.White,
    colorContainer = Color(0xFFFFDCBE), onColorContainer = Color(0xFF2D1600),
    colorContainerSoft = Color(0xFFFFD9B8), onColorContainerSoft = Color(0xFF633400),
    colorFixed = Color(0xFFFFDCBE), colorFixedDim = Color(0xFFFFB870),
    onColorFixed = Color(0xFF2D1600), onColorFixedVariant = Color(0xFF6A3B00)
)
val LightAmber = ColorFamily(
    color = Color(0xFF956E00), onColor = Color.White,
    colorContainer = Color(0xFFFFE08D), onColorContainer = Color(0xFF241A00),
    colorContainerSoft = Color(0xFFFFF1CC), onColorContainerSoft = Color(0xFF644900),
    colorFixed = Color(0xFFFFE08D), colorFixedDim = Color(0xFFF9BD00),
    onColorFixed = Color(0xFF241A00), onColorFixedVariant = Color(0xFF755500)
)
val LightYellow = ColorFamily(
    color = Color(0xFF7A7C00), onColor = Color.White,
    colorContainer = Color(0xFFE6E971), onColorContainer = Color(0xFF1C1D00),
    colorContainerSoft = Color(0xFFE4E76D), onColorContainerSoft = Color(0xFF444500),
    colorFixed = Color(0xFFE6E971), colorFixedDim = Color(0xFFC9CC58),
    onColorFixed = Color(0xFF1C1D00), onColorFixedVariant = Color(0xFF484900)
)
val LightLime = ColorFamily(
    color = Color(0xFF4F8100), onColor = Color.White,
    colorContainer = Color(0xFFD6FF8E), onColorContainer = Color(0xFF112000),
    colorContainerSoft = Color(0xFFE6FFC0), onColorContainerSoft = Color(0xFF2D4B00),
    colorFixed = Color(0xFFD6FF8E), colorFixedDim = Color(0xFFB4F34A),
    onColorFixed = Color(0xFF112000), onColorFixedVariant = Color(0xFF385D00)
)
val LightGreen = ColorFamily(
    color = Color(0xFF008D4D), onColor = Color.White,
    colorContainer = Color(0xFF98F7B5), onColorContainer = Color(0xFF00210E),
    colorContainerSoft = Color(0xFFB0F9C4), onColorContainerSoft = Color(0xFF00542D),
    colorFixed = Color(0xFF98F7B5), colorFixedDim = Color(0xFF7CDAA0),
    onColorFixed = Color(0xFF00210E), onColorFixedVariant = Color(0xFF00522B)
)
val LightEmerald = ColorFamily(
    color = Color(0xFF008A70), onColor = Color.White,
    colorContainer = Color(0xFF91FADF), onColorContainer = Color(0xFF002019),
    colorContainerSoft = Color(0xFFC2FEEF), onColorContainerSoft = Color(0xFF005141),
    colorFixed = Color(0xFF91FADF), colorFixedDim = Color(0xFF73DDC3),
    onColorFixed = Color(0xFF002019), onColorFixedVariant = Color(0xFF005141)
)
val LightTeal = ColorFamily(
    color = Color(0xFF008080), onColor = Color.White,
    colorContainer = Color(0xFF9CF0F0), onColorContainer = Color(0xFF002020),
    colorContainerSoft = Color(0xFFC6F7F7), onColorContainerSoft = Color(0xFF004D4D),
    colorFixed = Color(0xFF9CF0F0), colorFixedDim = Color(0xFF7FD4D4),
    onColorFixed = Color(0xFF002020), onColorFixedVariant = Color(0xFF004D4D)
)
val LightCyan = ColorFamily(
    color = Color(0xFF007A8A), onColor = Color.White,
    colorContainer = Color(0xFFB9F0FF), onColorContainer = Color(0xFF001F25),
    colorContainerSoft = Color(0xFFD9F7FF), onColorContainerSoft = Color(0xFF004953),
    colorFixed = Color(0xFFB9F0FF), colorFixedDim = Color(0xFF5DD6F4),
    onColorFixed = Color(0xFF001F25), onColorFixedVariant = Color(0xFF004953)
)
val LightSky = ColorFamily(
    color = Color(0xFF0077B6), onColor = Color.White,
    colorContainer = Color(0xFFCAF0F8), onColorContainer = Color(0xFF001D2D),
    colorContainerSoft = Color(0xFFE1F7FD), onColorContainerSoft = Color(0xFF00446A),
    colorFixed = Color(0xFFCAF0F8), colorFixedDim = Color(0xFF90E0EF),
    onColorFixed = Color(0xFF001D2D), onColorFixedVariant = Color(0xFF00446A)
)
val LightBlue = ColorFamily(
    color = Color(0xFF007DCF), onColor = Color.White,
    colorContainer = Color(0xFFD1E4FF), onColorContainer = Color(0xFF001D36),
    colorContainerSoft = Color(0xFFCDE1FF), onColorContainerSoft = Color(0xFF004C81),
    colorFixed = Color(0xFFD1E4FF), colorFixedDim = Color(0xFF9ECAFF),
    onColorFixed = Color(0xFF001D36), onColorFixedVariant = Color(0xFF00497D)
)
val LightIndigo = ColorFamily(
    color = Color(0xFF5D66C4), onColor = Color.White,
    colorContainer = Color(0xFFE0E0FF), onColorContainer = Color(0xFF040B62),
    colorContainerSoft = Color(0xFFDAD9FF), onColorContainerSoft = Color(0xFF323A8C),
    colorFixed = Color(0xFFE0E0FF), colorFixedDim = Color(0xFFBEC2FF),
    onColorFixed = Color(0xFF040B62), onColorFixedVariant = Color(0xFF373E90)
)
val LightViolet = ColorFamily(
    color = Color(0xFFAD52A7), onColor = Color.White,
    colorContainer = Color(0xFFFFD7F5), onColorContainer = Color(0xFF380036),
    colorContainerSoft = Color(0xFFFFCEF3), onColorContainerSoft = Color(0xFF6F286C),
    colorFixed = Color(0xFFFFD7F5), colorFixedDim = Color(0xFFFFADEB),
    onColorFixed = Color(0xFF380036), onColorFixedVariant = Color(0xFF722A6F)
)
val LightPurple = ColorFamily(
    color = Color(0xFF8B46B2), onColor = Color.White,
    colorContainer = Color(0xFFF3D9FF), onColorContainer = Color(0xFF2D0050),
    colorContainerSoft = Color(0xFFF7E6FF), onColorContainerSoft = Color(0xFF5D1E81),
    colorFixed = Color(0xFFF3D9FF), colorFixedDim = Color(0xFFDDAFFF),
    onColorFixed = Color(0xFF2D0050), onColorFixedVariant = Color(0xFF712B9A)
)
val LightFuchsia = ColorFamily(
    color = Color(0xFFC026D3), onColor = Color.White,
    colorContainer = Color(0xFFFFD7F1), onColorContainer = Color(0xFF3B0029),
    colorContainerSoft = Color(0xFFFFE5F6), onColorContainerSoft = Color(0xFF8A0064),
    colorFixed = Color(0xFFFFD7F1), colorFixedDim = Color(0xFFF9A8D4),
    onColorFixed = Color(0xFF3B0029), onColorFixedVariant = Color(0xFF911E7D)
)
val LightPink = ColorFamily(
    color = Color(0xFFDB2777), onColor = Color.White,
    colorContainer = Color(0xFFFFD9E2), onColorContainer = Color(0xFF3F001D),
    colorContainerSoft = Color(0xFFFFE2E9), onColorContainerSoft = Color(0xFF9A004B),
    colorFixed = Color(0xFFFFD9E2), colorFixedDim = Color(0xFFF9A8D4),
    onColorFixed = Color(0xFF3F001D), onColorFixedVariant = Color(0xFF9E1D5B)
)

val LightRainbow = RainbowColors(
    red = LightRed, rose = LightRose, pink = LightPink, fuchsia = LightFuchsia,
    purple = LightPurple, violet = LightViolet, indigo = LightIndigo, blue = LightBlue,
    sky = LightSky, cyan = LightCyan, teal = LightTeal, emerald = LightEmerald,
    green = LightGreen, lime = LightLime, yellow = LightYellow, amber = LightAmber,
    orange = LightOrange
)

// --- DARK PALETTES ---
val DarkRed = ColorFamily(
    color = Color(0xFFFFD2CC), onColor = Color(0xFF690005),
    colorContainer = Color(0xFF93000A), onColorContainer = Color(0xFFFFDAD6),
    colorContainerSoft = Color(0xFF8D0008), onColorContainerSoft = Color(0xFFFFD8D5),
    colorFixed = Color(0xFFFFDAD6), colorFixedDim = Color(0xFFFFB4AB),
    onColorFixed = Color(0xFF410002), onColorFixedVariant = Color(0xFF93000A)
)
val DarkRose = ColorFamily(
    color = Color(0xFFFFD1D9), onColor = Color(0xFF630030),
    colorContainer = Color(0xFF8E0045), onColorContainer = Color(0xFFFFD9E2),
    colorContainerSoft = Color(0xFF8A003D), onColorContainerSoft = Color(0xFFFFE1E8),
    colorFixed = Color(0xFFFFD9E2), colorFixedDim = Color(0xFFFFB1C8),
    onColorFixed = Color(0xFF3E001D), onColorFixedVariant = Color(0xFF980047)
)
val DarkOrange = ColorFamily(
    color = Color(0xFFFFDAB7), onColor = Color(0xFF4A2800),
    colorContainer = Color(0xFF6A3B00), onColorContainer = Color(0xFFFFDCBE),
    colorContainerSoft = Color(0xFF633400), onColorContainerSoft = Color(0xFFFFD9B8),
    colorFixed = Color(0xFFFFDCBE), colorFixedDim = Color(0xFFFFB870),
    onColorFixed = Color(0xFF2D1600), onColorFixedVariant = Color(0xFF6A3B00)
)
val DarkAmber = ColorFamily(
    color = Color(0xFFFFDCA6), onColor = Color(0xFF4E3A00),
    colorContainer = Color(0xFF705300), onColorContainer = Color(0xFFFFE08D),
    colorContainerSoft = Color(0xFF644900), onColorContainerSoft = Color(0xFFFFF1CC),
    colorFixed = Color(0xFFFFE08D), colorFixedDim = Color(0xFFF9BD00),
    onColorFixed = Color(0xFF241A00), onColorFixedVariant = Color(0xFF755500)
)
val DarkYellow = ColorFamily(
    color = Color(0xFFE4E76D), onColor = Color(0xFF313300),
    colorContainer = Color(0xFF484900), onColorContainer = Color(0xFFE6E971),
    colorContainerSoft = Color(0xFF444500), onColorContainerSoft = Color(0xFFE4E76D),
    colorFixed = Color(0xFFE6E971), colorFixedDim = Color(0xFFC9CC58),
    onColorFixed = Color(0xFF1C1D00), onColorFixedVariant = Color(0xFF484900)
)
val DarkLime = ColorFamily(
    color = Color(0xFFD6FF8E), onColor = Color(0xFF264400),
    colorContainer = Color(0xFF376100), onColorContainer = Color(0xFFD6FF8E),
    colorContainerSoft = Color(0xFF2D4B00), onColorContainerSoft = Color(0xFFE6FFC0),
    colorFixed = Color(0xFFD6FF8E), colorFixedDim = Color(0xFFB4F34A),
    onColorFixed = Color(0xFF112000), onColorFixedVariant = Color(0xFF385D00)
)
val DarkGreen = ColorFamily(
    color = Color(0xFFB0F9C4), onColor = Color(0xFF00391C),
    colorContainer = Color(0xFF00522B), onColorContainer = Color(0xFF98F7B5),
    colorContainerSoft = Color(0xFF00542D), onColorContainerSoft = Color(0xFFB0F9C4),
    colorFixed = Color(0xFF98F7B5), colorFixedDim = Color(0xFF7CDAA0),
    onColorFixed = Color(0xFF00210E), onColorFixedVariant = Color(0xFF00522B)
)
val DarkEmerald = ColorFamily(
    color = Color(0xFF91FADF), onColor = Color(0xFF00372D),
    colorContainer = Color(0xFF005141), onColorContainer = Color(0xFF91FADF),
    colorContainerSoft = Color(0xFF005141), onColorContainerSoft = Color(0xFFC2FEEF),
    colorFixed = Color(0xFF91FADF), colorFixedDim = Color(0xFF73DDC3),
    onColorFixed = Color(0xFF002019), onColorFixedVariant = Color(0xFF005141)
)
val DarkTeal = ColorFamily(
    color = Color(0xFF9CF0F0), onColor = Color(0xFF003737),
    colorContainer = Color(0xFF004D4D), onColorContainer = Color(0xFF9CF0F0),
    colorContainerSoft = Color(0xFF004D4D), onColorContainerSoft = Color(0xFFC6F7F7),
    colorFixed = Color(0xFF9CF0F0), colorFixedDim = Color(0xFF7FD4D4),
    onColorFixed = Color(0xFF002020), onColorFixedVariant = Color(0xFF004D4D)
)
val DarkCyan = ColorFamily(
    color = Color(0xFFB9F0FF), onColor = Color(0xFF00363D),
    colorContainer = Color(0xFF004953), onColorContainer = Color(0xFFB9F0FF),
    colorContainerSoft = Color(0xFF004953), onColorContainerSoft = Color(0xFFD9F7FF),
    colorFixed = Color(0xFFB9F0FF), colorFixedDim = Color(0xFF5DD6F4),
    onColorFixed = Color(0xFF001F25), onColorFixedVariant = Color(0xFF004953)
)
val DarkSky = ColorFamily(
    color = Color(0xFFCAF0F8), onColor = Color(0xFF003350),
    colorContainer = Color(0xFF00446A), onColorContainer = Color(0xFFCAF0F8),
    colorContainerSoft = Color(0xFF00446A), onColorContainerSoft = Color(0xFFE1F7FD),
    colorFixed = Color(0xFFCAF0F8), colorFixedDim = Color(0xFF90E0EF),
    onColorFixed = Color(0xFF001D2D), onColorFixedVariant = Color(0xFF00446A)
)
val DarkBlue = ColorFamily(
    color = Color(0xFFCDE1FF), onColor = Color(0xFF003258),
    colorContainer = Color(0xFF00497D), onColorContainer = Color(0xFFD1E4FF),
    colorContainerSoft = Color(0xFF004C81), onColorContainerSoft = Color(0xFFCDE1FF),
    colorFixed = Color(0xFFD1E4FF), colorFixedDim = Color(0xFF9ECAFF),
    onColorFixed = Color(0xFF001D36), onColorFixedVariant = Color(0xFF00497D)
)
val DarkIndigo = ColorFamily(
    color = Color(0xFFDAD9FF), onColor = Color(0xFF1F2578),
    colorContainer = Color(0xFF373E90), onColorContainer = Color(0xFFE0E0FF),
    colorContainerSoft = Color(0xFF323A8C), onColorContainerSoft = Color(0xFFDAD9FF),
    colorFixed = Color(0xFFE0E0FF), colorFixedDim = Color(0xFFBEC2FF),
    onColorFixed = Color(0xFF040B62), onColorFixedVariant = Color(0xFF373E90)
)
val DarkViolet = ColorFamily(
    color = Color(0xFFFFCEF3), onColor = Color(0xFF571257),
    colorContainer = Color(0xFF722A6F), onColorContainer = Color(0xFFFFD7F5),
    colorContainerSoft = Color(0xFF6F286C), onColorContainerSoft = Color(0xFFFFCEF3),
    colorFixed = Color(0xFFFFD7F5), colorFixedDim = Color(0xFFFFADEB),
    onColorFixed = Color(0xFF380036), onColorFixedVariant = Color(0xFF722A6F)
)
val DarkPurple = ColorFamily(
    color = Color(0xFFF3D9FF), onColor = Color(0xFF451963),
    colorContainer = Color(0xFF62298C), onColorContainer = Color(0xFFF3D9FF),
    colorContainerSoft = Color(0xFF5D1E81), onColorContainerSoft = Color(0xFFF7E6FF),
    colorFixed = Color(0xFFF3D9FF), colorFixedDim = Color(0xFFDDAFFF),
    onColorFixed = Color(0xFF2D0050), onColorFixedVariant = Color(0xFF712B9A)
)
val DarkFuchsia = ColorFamily(
    color = Color(0xFFFFD7F1), onColor = Color(0xFF65004A),
    colorContainer = Color(0xFF911E7D), onColorContainer = Color(0xFFFFD7F1),
    colorContainerSoft = Color(0xFF8A0064), onColorContainerSoft = Color(0xFFFFE5F6),
    colorFixed = Color(0xFFFFD7F1), colorFixedDim = Color(0xFFF9A8D4),
    onColorFixed = Color(0xFF3B0029), onColorFixedVariant = Color(0xFF911E7D)
)
val DarkPink = ColorFamily(
    color = Color(0xFFFFD9E2), onColor = Color(0xFF6F0038),
    colorContainer = Color(0xFF9E1D5B), onColorContainer = Color(0xFFFFD9E2),
    colorContainerSoft = Color(0xFF9A004B), onColorContainerSoft = Color(0xFFFFE2E9),
    colorFixed = Color(0xFFFFD9E2), colorFixedDim = Color(0xFFF9A8D4),
    onColorFixed = Color(0xFF3F001D), onColorFixedVariant = Color(0xFF9E1D5B)
)

val DarkRainbow = RainbowColors(
    red = DarkRed, rose = DarkRose, pink = DarkPink, fuchsia = DarkFuchsia,
    purple = DarkPurple, violet = DarkViolet, indigo = DarkIndigo, blue = DarkBlue,
    sky = DarkSky, cyan = DarkCyan, teal = DarkTeal, emerald = DarkEmerald,
    green = DarkGreen, lime = DarkLime, yellow = DarkYellow, amber = DarkAmber,
    orange = DarkOrange
)
