package banhmi.senboard.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import banhmi.senboard.R

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val GoogleSansFont = GoogleFont(name = "Google Sans")

val GoogleSansFontFamily = FontFamily(
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
    ),
    Font(resId = R.font.googlesans_regular),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.googlesans_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.SemiBold,
    ),
    Font(
        resId = R.font.googlesans_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.Bold,
    ),
    Font(
        resId = R.font.googlesans_bold,
        weight = FontWeight.Bold,
    ),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        style = FontStyle.Italic,
    ),
    Font(resId = R.font.googlesans_italic),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.Medium,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.googlesans_mediumitalic,
        weight = FontWeight.Medium,
    ),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.googlesans_semibolditalic,
        weight = FontWeight.SemiBold,
    ),
    Font(
        googleFont = GoogleSansFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.googlesans_bolditalic,
        weight = FontWeight.Bold,
    ),
)

private val defaultTypography = Typography()
val SenTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = GoogleSansFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = GoogleSansFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = GoogleSansFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = GoogleSansFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = GoogleSansFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = GoogleSansFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = GoogleSansFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = GoogleSansFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = GoogleSansFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = GoogleSansFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = GoogleSansFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = GoogleSansFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = GoogleSansFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = GoogleSansFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = GoogleSansFontFamily),
)
