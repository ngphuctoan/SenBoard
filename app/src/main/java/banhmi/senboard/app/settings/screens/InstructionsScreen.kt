package banhmi.senboard.app.settings.screens

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import banhmi.senboard.app.settings.ui.SenSettingsDivider
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.app.settings.ui.SenSettingsTestingTextField
import banhmi.senboard.ui.theme.LightRainbow

private fun isEnabled(context: Context): Boolean {
    val imService = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imService.enabledInputMethodList.any {
        it.packageName == context.packageName
    }
}

private fun isSelected(context: Context): Boolean = Settings.Secure.getString(
    context.contentResolver,
    Settings.Secure.DEFAULT_INPUT_METHOD,
)?.contains(context.packageName) == true

@Composable
fun InstructionsScreen() {
    val context = LocalContext.current
    val imService = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    var isEnabled by remember { mutableStateOf(isEnabled(context)) }
    var isSelected by remember { mutableStateOf(isSelected(context)) }

    LifecycleResumeEffect(Unit) {
        isEnabled = isEnabled(context)
        onPauseOrDispose { }
    }

    DisposableEffect(context) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                isSelected = isSelected(context)
            }
        }

        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(Settings.Secure.DEFAULT_INPUT_METHOD),
            false,
            observer,
        )

        onDispose {
            context.contentResolver.unregisterContentObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SenSettingsList {
            SenSettingsListGroup {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Kích hoạt bàn phím",
                        supportingLabels = listOf("Truy cập vào Cài đặt để điều chỉnh"),
                        icon = if (isEnabled) Icons.Rounded.Check else Icons.Rounded.MoreHoriz,
                        color = if (isEnabled) LightRainbow.green.color else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isEnabled) LightRainbow.green.onColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = {
                            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                            context.startActivity(intent)
                        },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Đổi phương thức nhập",
                        supportingLabels = listOf("Lựa chọn bàn phím mặc định"),
                        icon = if (isSelected) Icons.Rounded.Check else Icons.Rounded.MoreHoriz,
                        color = if (isSelected) LightRainbow.green.color else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) LightRainbow.green.onColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { imService.showInputMethodPicker() },
                    )
                }
            }
        }

        SenSettingsTestingTextField(onInputMethodChange = { imService.showInputMethodPicker() })
    }
}
