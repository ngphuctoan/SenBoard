package banhmi.senboard.app.annotations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Light",
    device = Devices.PIXEL_9_PRO_XL,
    apiLevel = 36,
//    showSystemUi = true,
)
@Preview(
    name = "Dark",
    device = Devices.PIXEL_9_PRO_XL,
    apiLevel = 36,
//    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
)
annotation class SenPreviewCommon
