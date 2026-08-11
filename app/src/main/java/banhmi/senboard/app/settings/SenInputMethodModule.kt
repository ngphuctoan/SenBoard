package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenColumnSpacer
import banhmi.senboard.app.ui.SenHeader
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.outOf
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenInputMethod

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenInputMethodModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(): SenEntryProviderInstaller = {
        entry<SenInputMethod> {
            SenInputMethodScreen()
        }
    }
}

@Composable
fun SenInputMethodScreen() {
    SenScaffold(
        topBar = {},
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item { SenHeader("Phương thức gõ") }

            itemsIndexed(listOf("Chữ Việt song song", "Telex", "VNI")) { index, methodName ->
                SenMenu(
                    selected = index == 0,
                    shapes = SenMenuDefaults.segmentedShapes(index outOf 3),
                    leadingContent = {
                        RadioButton(
                            selected = index == 0,
                            onClick = null,
                        )
                    },
                    onClick = {},
                ) {
                    Text(methodName)
                }
            }

            item { SenColumnSpacer() }

            item { SenHeader("Hỗ trợ") }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 3),
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = null,
                        )
                    },
                    onClick = {},
                ) {
                    Text("Tự động viết hoa")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(1 outOf 3),
                    supportingContent = { Text("Ấn dấu cách hai lần sẽ thêm một dấu chấm") },
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = null,
                        )
                    },
                    onClick = {},
                ) {
                    Text("Phím tắt \".\"")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(2 outOf 3),
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = null,
                        )
                    },
                    onClick = {},
                ) {
                    Text("Gợi ý từ kế tiếp")
                }
            }

            item { SenColumnSpacer() }

            item { SenHeader("Easter egg") }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(40.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Đến trang Giới thiệu ứng dụng",
                            )
                            VerticalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(start = 6.dp, end = 12.dp),
                            )
                            Switch(
                                checked = true,
                                onCheckedChange = {},
                            )
                        }
                    },
                    onClick = {},
                ) {
                    Text("Chế độ aaaaa")
                }
            }

            item { SenColumnSpacer() }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyle.current.merge(
                            MaterialTheme.typography.bodyMedium
                        ),
                        LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Thông tin về easter egg",
                        )
                        Text("Bạn có thể bật lại easter egg tại Giới thiệu ứng dụng")
                    }
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenInputMethodScreenPreview() {
    SenTheme {
        SenInputMethodScreen()
    }
}
