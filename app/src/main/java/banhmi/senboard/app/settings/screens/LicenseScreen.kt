package banhmi.senboard.app.settings.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.core.net.toUri
import banhmi.senboard.app.settings.models.SenSettingsItemAction
import banhmi.senboard.app.settings.ui.SenScreenScaffold
import banhmi.senboard.app.settings.ui.SenScreenTopAppBar
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.shared.utils.ToastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LicenseScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    val licenseText = produceState(initialValue = "") {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open("license/Apache-2.0.txt").bufferedReader().use {
                    it.readText()
                }
            }.getOrDefault("Không thể lấy thông tin giấy phép :(")
        }
    }

    val apacheLicense20Url = "https://www.apache.org/licenses/LICENSE-2.0".toUri()

    SenScreenScaffold(topBar = { SenScreenTopAppBar("Giấy phép", onBackClick) }) { innerPadding ->
        SenSettingsList(modifier = Modifier.padding(innerPadding)) {
            SenSettingsListGroup(isSubMenu = true) {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Apache License 2.0 (Apache-2.0)",
                        supportingLabels = listOf("Xem thông tin tại apache.org"),
                        icon = Icons.AutoMirrored.Rounded.OpenInNew,
                        action = SenSettingsItemAction.Bottom({
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(licenseText.value, fontFamily = FontFamily.Monospace)
                            }
                        }),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, apacheLicense20Url)
                            runCatching { context.startActivity(intent) }.onFailure { exception ->
                                val message = when (exception) {
                                    is ActivityNotFoundException -> "Không tìm thấy trình duyệt hoặc ứng dụng phù hợp"
                                    else -> exception.localizedMessage
                                }
                                ToastManager.show(context) {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
