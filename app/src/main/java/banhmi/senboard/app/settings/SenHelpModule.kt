package banhmi.senboard.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenIcon
import banhmi.senboard.app.ui.SenIconDefaults
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.ui.theme.m3RefPaletteCyan
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenHelp

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenHelpModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenHelp> {
            SenHelpScreen(onNavigateBack = { navigator.goBack() })
        }
    }
}

private enum class HelpCategory(val label: String) {
    All("Tất cả"),
    CVN("34 Quy tắc CVN"),
    ToneMarks("18 Ký hiệu dấu"),
    PWord("Chữ đệm P"),
    Vần56("56 Vần dài"),
}

private data class RuleItem(
    val category: HelpCategory,
    val title: String,
    val rule: String,
    val example: String,
    val note: String? = null
)

@Composable
fun SenHelpScreen(onNavigateBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HelpCategory.All) }

    val allRules = remember {
        listOf(
            // A. 34 QUY TẮC RÚT GỌN CHỮ VIỆT NHANH (CVN)
            // 1. Bỏ dấu sắc ở phụ âm cuối c, p, t
            RuleItem(
                HelpCategory.CVN,
                "Bỏ dấu sắc đuôi C, P, T",
                "c, p, t",
                "các ➔ cac, úp ➔ up, hát ➔ hat"
            ),

            // 2. Quy tắc Y = I & UY = Y
            RuleItem(
                HelpCategory.CVN,
                "Thay Y = I & UY = Y",
                "Y=I, UY=Y",
                "y tá ➔ i tá, thúy ➔ thú",
                "AY, ÂY giữ nguyên (mây bay = mây bay)"
            ),

            // 3. Thay phụ âm đầu
            RuleItem(HelpCategory.CVN, "Thay PH ➔ F", "PH = F", "phai ➔ fai"),
            RuleItem(HelpCategory.CVN, "Thay Đ ➔ D", "Đ = D", "đi ➔ di"),
            RuleItem(HelpCategory.CVN, "Thay QU ➔ Q", "QU = Q", "quay ➔ qay"),
            RuleItem(HelpCategory.CVN, "Thay GI ➔ J", "GI = J", "giữ ➔ jữ"),
            RuleItem(HelpCategory.CVN, "Thay K ➔ C", "K = C", "kín ➔ cín"),
            RuleItem(HelpCategory.CVN, "Thay GH ➔ G", "GH = G", "ghê ➔ gê"),
            RuleItem(HelpCategory.CVN, "Thay KH ➔ K", "KH = K", "khó ➔ kó"),
            RuleItem(HelpCategory.CVN, "Thay D ➔ Z", "D = Z", "do dự ➔ zo zự"),
            RuleItem(HelpCategory.CVN, "Thay NG, NGH ➔ W", "NG/NGH = W", "nga ➔ wa, nghĩ ➔ wĩ"),

            // 4. Thay phụ âm cuối
            RuleItem(HelpCategory.CVN, "Phụ âm cuối NG ➔ G", "NG = G", "mong ➔ mog"),
            RuleItem(HelpCategory.CVN, "Phụ âm cuối NH ➔ H", "NH = H", "hoành ➔ hoàh"),
            RuleItem(HelpCategory.CVN, "Phụ âm cuối CH ➔ K", "CH = K", "nguệch ➔ wuệk"),

            // B. 18 KÝ HIỆU DẤU THAY DẤU CHO CVNSS
            // 1. Nhóm nón ^ (â, ê, ô)
            RuleItem(HelpCategory.ToneMarks, "Dấu Sắc (Nhóm Â, Ê, Ô)", "Sắc = B", "cố ➔ cob"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Huyền (Nhóm Â, Ê, Ô)", "Huyền = D", "cồ ➔ cod"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Hỏi (Nhóm Â, Ê, Ô)", "Hỏi = Q", "cổ ➔ coq"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Ngã (Nhóm Â, Ê, Ô)", "Ngã = G", "cỗ ➔ cog"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Nặng (Nhóm Â, Ê, Ô)", "Nặng = F", "cộ ➔ cof"),
            RuleItem(HelpCategory.ToneMarks, "Thanh Ngang (Nhóm Â, Ê, Ô)", "Ngang = Y", "cô ➔ coy"),

            // 2. Nhóm trăng ~ (ơ, ư, ă)
            RuleItem(HelpCategory.ToneMarks, "Dấu Sắc (Nhóm Ơ, Ư, Ă)", "Sắc = X", "lớ ➔ lox"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Huyền (Nhóm Ơ, Ư, Ă)", "Huyền = K", "lờ ➔ lok"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Hỏi (Nhóm Ơ, Ư, Ă)", "Hỏi = V", "lở ➔ lov"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Ngã (Nhóm Ơ, Ư, Ă)", "Ngã = W", "lỡ ➔ low"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Nặng (Nhóm Ơ, Ư, Ă)", "Nặng = H", "lợ ➔ loh"),
            RuleItem(HelpCategory.ToneMarks, "Thanh Ngang (Nhóm Ơ, Ư, Ă)", "Ngang = O", "lơ ➔ loo"),

            // 3. Nhóm Không dấu phụ (a, e, i, o, u, y)
            RuleItem(
                HelpCategory.ToneMarks,
                "Dấu Sắc (Không dấu phụ)",
                "Sắc = J",
                "vó ➔ voj",
                "Chữ có phụ âm cuối C, P, T thì bỏ J (khác ➔ kac, áp ➔ ap, phút ➔ fut)"
            ),
            RuleItem(HelpCategory.ToneMarks, "Dấu Huyền (Không dấu phụ)", "Huyền = L", "vò ➔ vol"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Hỏi (Không dấu phụ)", "Hỏi = Z", "vỏ ➔ voz"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Ngã (Không dấu phụ)", "Ngã = S", "võ ➔ vos"),
            RuleItem(HelpCategory.ToneMarks, "Dấu Nặng (Không dấu phụ)", "Nặng = R", "vọ ➔ vor"),

            // 4. Ký hiệu P (Chữ đệm câm)
            RuleItem(
                HelpCategory.PWord,
                "Chữ đệm câm P",
                "Suffix P",
                "long ➔ logp (tránh hiểu lầm với lỗ = log)",
                "P đặt sau vần rút gọn ở thanh ngang không dấu phụ: ag, ah, aj, eg, el, ev, ew, ez, ih, oah, og, oj, ol, ov, ow, oz, ug, yh"
            ),

            // 5. 56 Vần dài
            RuleItem(HelpCategory.Vần56, "Vần UYÊT", "uyêt ➔ yd", "tuyết ➔ tyd"),
            RuleItem(HelpCategory.Vần56, "Vần UYÊN", "uyên ➔ yl", "huyện ➔ hylf"),
            RuleItem(HelpCategory.Vần56, "Vần IÊT / YÊT", "iêt ➔ id", "tiết ➔ tidb"),
            RuleItem(HelpCategory.Vần56, "Vần IÊP / YÊP", "iêp ➔ if", "tiếp ➔ tifb, hiệp ➔ hịf"),
            RuleItem(HelpCategory.Vần56, "Vần UÔC", "uôc ➔ us", "thuốc ➔ thus"),
            RuleItem(HelpCategory.Vần56, "Vần UÂT", "uât ➔ âd", "suất ➔ sâd"),
            RuleItem(HelpCategory.Vần56, "Vần ƯƠM", "ươm ➔ ưv", "cườm ➔ cuvk"),
            RuleItem(HelpCategory.Vần56, "Vần ƯƠNG", "ương ➔ ưz", "thường ➔ thuzk"),
            RuleItem(HelpCategory.Vần56, "Vần OANG", "oang ➔ oz", "hoang ➔ hozp"),
            RuleItem(HelpCategory.Vần56, "Vần OAY", "oay ➔ aj", "loay hoay ➔ lajp hajp"),
            RuleItem(HelpCategory.Vần56, "Vần ƯƠU", "ươu ➔ ưw", "rượu ➔ ruwh"),
            RuleItem(HelpCategory.Vần56, "Vần ƯƠN", "ươn ➔ ưl", "lượn ➔ lulh"),
            RuleItem(HelpCategory.Vần56, "Vần OĂT, OĂP, OĂC", "oăt➔ăd, oăp➔ăf", "xoắn ➔ xăl")
        )
    }

    val filteredRules = remember(searchQuery, selectedCategory) {
        allRules.filter { item ->
            (selectedCategory == HelpCategory.All || item.category == selectedCategory) &&
                    (searchQuery.isBlank() ||
                            item.title.contains(searchQuery, ignoreCase = true) ||
                            item.rule.contains(searchQuery, ignoreCase = true) ||
                            item.example.contains(searchQuery, ignoreCase = true))
        }
    }

    SenScaffold(
        topBar = {
//            SenTopBar(
//                title = { Text("Quy tắc gõ CVNSS 4.0") },
//                onNavigateBack = onNavigateBack,
//            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            // FIXED TOP HEADER (Search Bar & Filter Chips)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Bar
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Tìm kiếm quy tắc hoặc từ ví dụ...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Filter Chips Row
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) {
                    items(HelpCategory.entries.toTypedArray()) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.label) }
                        )
                    }
                }
            }

            // SCROLLABLE RULES LIST
            SenColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(filteredRules) { ruleItem ->
                    SenMenu(
                        shapes = ListItemDefaults.segmentedShapes(index = 0, count = 1),
                        supportingContent = {
                            Column(modifier = Modifier.padding(top = 2.dp)) {
                                Text("Ví dụ: ${ruleItem.example}")
                                if (ruleItem.note != null) {
                                    Text(
                                        text = "💡 ${ruleItem.note}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        },
                        leadingContent = {
                            SenIcon(
                                icon = if (ruleItem.category == HelpCategory.ToneMarks) Icons.Filled.Edit else Icons.Filled.Code,
                                colors = SenIconDefaults.vibrantColors(m3RefPaletteCyan),
                            )
                        },
                        trailingContent = {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = ruleItem.rule,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    maxLines = 1,
                                    softWrap = false,
                                )
                            }
                        },
                        onClick = {},
                    ) {
                        Text(ruleItem.title)
                    }
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenHelpScreenPreview() {
    SenTheme {
        SenHelpScreen(onNavigateBack = {})
    }
}
