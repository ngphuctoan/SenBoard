package banhmi.senboard.app.settings.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.shared.settings.SenSettingsViewModel
import banhmi.senboard.app.settings.ui.SenSettingsDivider
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.app.settings.ui.SenScreenScaffold
import banhmi.senboard.app.settings.ui.SenScreenTopAppBar
import banhmi.senboard.shared.utils.ToastManager

const val EASTER_EGG_MAX_COUNT = 6
const val EASTER_EGG_REVEAL_COUNT = 3

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    onLicenseClick: () -> Unit,
    viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory),
) {
    val context = LocalContext.current

    val packageInfo = remember(context) {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0L),
                )
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
        }.getOrNull()
    }

    // We won't ever give you up, but I want to give up mobile development :(
    val remoteRepositoryUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ".toUri()

    val state by viewModel.inputMethodState.collectAsStateWithLifecycle()

    var easterEggCounter by remember { mutableIntStateOf(0) }

    SenScreenScaffold(topBar = { SenScreenTopAppBar("Giới thiệu", onBackClick) }) { innerPadding ->
        SenSettingsList(modifier = Modifier.padding(innerPadding)) {
            SenSettingsListGroup(showIcons = false) {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Phiên bản ứng dụng",
                        supportingLabels = listOf(packageInfo?.versionName ?: "dev"),
                        onClick = {
                            when {
                                state.easterEggEnabled -> {
                                    easterEggCounter = EASTER_EGG_MAX_COUNT
                                }

                                easterEggCounter >= EASTER_EGG_MAX_COUNT -> {
                                    viewModel.updateEasterEggEnabled(true)
                                }

                                else -> easterEggCounter++
                            }

                            val message = when {
                                easterEggCounter >= EASTER_EGG_MAX_COUNT -> "Easter egg đã được kích hoạt!"

                                easterEggCounter >= EASTER_EGG_REVEAL_COUNT -> {
                                    val remainingCounter = EASTER_EGG_MAX_COUNT - easterEggCounter
                                    "Ấn thêm $remainingCounter lượt để bật Easter egg"
                                }

                                else -> null
                            }

                            message?.let { message ->
                                ToastManager.show(context) {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT)
                                }
                            }
                        },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Giấy phép",
                        supportingLabels = listOf("Apache License 2.0"),
                        onClick = onLicenseClick,
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Xem mã nguồn",
                        supportingLabels = listOf("Lưu ý: Yêu cầu tài khoản trường"),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, remoteRepositoryUrl)
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
