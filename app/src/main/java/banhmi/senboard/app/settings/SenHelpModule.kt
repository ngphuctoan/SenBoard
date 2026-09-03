package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import banhmi.senboard.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.app.ui.segmentedPadding
import banhmi.senboard.ui.theme.SenTheme
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

    CVN("Quy tắc (34)"),

    ToneMarks("Ký hiệu dấu (18)"),

    PWord("Chữ đệm"),

    @Suppress("EnumEntryName", "NonAsciiCharacters")
    Vần56("Vần dài (56)"),
}

/**
 * Represents a rule or example conversion pair (CVNSS ➔ CQN).
 */
data class Conversion(
    val cvnss: String,
    val cqn: String,
)

infix fun String.convertsTo(cqn: String): Conversion = Conversion(this, cqn)

private data class RuleItem(
    val category: HelpCategory,
    val title: String,
    val rule: Conversion,
    val examples: List<Conversion>,
    val note: String? = null,
)

@Composable
fun SenHelpScreen(onNavigateBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HelpCategory.All) }

    val filteredRules = remember(searchQuery, selectedCategory) {
        CvnssHelpRules.filter { item ->
            (selectedCategory == HelpCategory.All || item.category == selectedCategory) &&
                    (searchQuery.isBlank() ||
                            item.title.contains(searchQuery, ignoreCase = true) ||
                            item.rule.cvnss.contains(searchQuery, ignoreCase = true) ||
                            item.rule.cqn.contains(searchQuery, ignoreCase = true) ||
                            item.examples.any { ex ->
                                ex.cvnss.contains(searchQuery, ignoreCase = true) ||
                                        ex.cqn.contains(searchQuery, ignoreCase = true)
                            })
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberSenTopBarState())

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Hướng dẫn sử dụng") },
                navigationIcon = { SenTopBarBackButton(onClick = onNavigateBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            // Header: Search Bar & Filter Chips
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Search Bar
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tìm kiếm") },
                        placeholder = { Text("Ví dụ: \"q\", \"hỏi\", \"^\", \"divq\", \"điểm\"") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Tìm kiếm",
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                        contentDescription = "Xoá",
                                    )
                                }
                            }
                        },
                        singleLine = true,
                    )
                }

                // Filter Chips
                /* LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                ) {
                    items(HelpCategory.entries.toTypedArray()) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.label) },
                        )
                    }
                } */
                PrimaryScrollableTabRow(
                    selectedTabIndex = HelpCategory.entries.indexOf(selectedCategory),
                    containerColor = Color.Transparent,
                    edgePadding = 0.dp,
                ) {
                    HelpCategory.entries.forEach { category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            text = {
                                Text(
                                    text = category.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                        )
                    }
                }
            }

            // Rules List
            SenColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                itemsIndexed(filteredRules) { index, ruleItem ->
                    SenMenu(
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index,
                            count = filteredRules.size,
                        ),
                        supportingContent = {
                            Column(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                // Formula
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text(
                                        text = ruleItem.rule.cvnss,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = ruleItem.rule.cqn,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }

                                // Examples
                                ruleItem.examples.forEach { ex ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Text(
                                            text = ex.cvnss,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                            contentDescription = null,
                                            modifier = Modifier.size(13.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                        Text(
                                            text = ex.cqn,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }

                                // Note
                                if (ruleItem.note != null) {
                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Lightbulb,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.tertiary,
                                        )
                                        Text(
                                            text = ruleItem.note,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.tertiary,
                                        )
                                    }
                                }
                            }
                        },
                        modifier = if (index != filteredRules.lastIndex) {
                            Modifier.segmentedPadding()
                        } else {
                            Modifier
                        },
                    ) {
                        Text(ruleItem.title)
                    }
                }
            }
        }
    }
}

private val CvnssHelpRules = listOf(
    // A. 34 QUY TẮC RÚT GỌN CHỮ VIỆT NHANH (CVN)
    // 1. Bỏ dấu sắc ở phụ âm cuối c, p, t
    RuleItem(
        HelpCategory.CVN,
        "Bỏ dấu sắc đuôi C, P, T",
        "c, p, t" convertsTo "không dấu sắc",
        listOf("cac" convertsTo "các", "up" convertsTo "úp", "hat" convertsTo "hát"),
    ),

    // 2. Quy tắc Y = I & UY = Y
    RuleItem(
        HelpCategory.CVN,
        "Y = I & UY = Y",
        "i / y" convertsTo "y / uy",
        listOf("i tá" convertsTo "y tá", "thú" convertsTo "thúy", "byt" convertsTo "buýt"),
        "AY, ÂY giữ nguyên (mây bay = mây bay)",
    ),

    // 3. Phụ âm đầu (CVNSS ➔ CQN)
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm F ➔ PH",
        "f" convertsTo "ph",
        listOf("fai" convertsTo "phai"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm D ➔ Đ",
        "d" convertsTo "đ",
        listOf("di" convertsTo "đi", "di dâu dó" convertsTo "đi đâu đó"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm Q ➔ QU",
        "q" convertsTo "qu",
        listOf("qa" convertsTo "qua", "qi" convertsTo "quy", "qy" convertsTo "quy"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm J ➔ GI",
        "j" convertsTo "gi",
        listOf("já jì" convertsTo "giá gì", "jữ jìn" convertsTo "giữ gìn"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm C ➔ K",
        "c" convertsTo "k",
        listOf("cín" convertsTo "kín", "cê" convertsTo "kê", "cẻ" convertsTo "kẻ"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm G ➔ GH",
        "g" convertsTo "gh",
        listOf("gì" convertsTo "ghì", "gê" convertsTo "ghê", "ge" convertsTo "ghe"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm K ➔ KH",
        "k" convertsTo "kh",
        listOf("ki kó kăn" convertsTo "khi khó khăn"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm Z ➔ D",
        "z" convertsTo "d",
        listOf("zì" convertsTo "dì", "zo zự" convertsTo "do dự"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm W ➔ NG, NGH",
        "w" convertsTo "ng/ngh",
        listOf("wa" convertsTo "nga", "wĩ" convertsTo "nghĩ", "wề" convertsTo "nghề"),
    ),

    // 4. Thay phụ âm cuối
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm cuối G ➔ NG",
        "g" convertsTo "ng",
        listOf("mog" convertsTo "mong"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm cuối H ➔ NH",
        "h" convertsTo "nh",
        listOf("bah" convertsTo "banh", "hoàh" convertsTo "hoành", "huêh" convertsTo "huênh"),
    ),
    RuleItem(
        HelpCategory.CVN,
        "Phụ âm cuối K ➔ CH",
        "k" convertsTo "ch",
        listOf("sạk" convertsTo "sạch", "hoạk" convertsTo "hoạch", "wuệk" convertsTo "nguệch"),
    ),

    // B. 18 KÝ HIỆU DẤU THAY DẤU CHO CVNSS
    // 1. Nhóm nón ^ (â, ê, ô)
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Sắc (Nhóm Â, Ê, Ô)",
        "b" convertsTo "sắc ^",
        listOf(
            "anb" convertsTo "ấn",
            "eb" convertsTo "ế",
            "ekb" convertsTo "ếch",
            "bidb" convertsTo "biết",
            "totb" convertsTo "tốt",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Huyền (Nhóm Â, Ê, Ô)",
        "d" convertsTo "huyền ^",
        listOf(
            "amd" convertsTo "ầm",
            "qand" convertsTo "quần",
            "ved" convertsTo "về",
            "tild" convertsTo "tiền",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Hỏi (Nhóm Â, Ê, Ô)",
        "q" convertsTo "hỏi ^",
        listOf(
            "anq" convertsTo "ẩn",
            "deq" convertsTo "để",
            "divq" convertsTo "điểm",
            "oq" convertsTo "ổ",
            "tujq" convertsTo "tuổi",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Ngã (Nhóm Â, Ê, Ô)",
        "g" convertsTo "ngã ^",
        listOf(
            "vayg" convertsTo "vẫy",
            "reg" convertsTo "rễ",
            "wylg" convertsTo "nguyễn",
            "log" convertsTo "lỗ",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Nặng (Nhóm Â, Ê, Ô)",
        "f" convertsTo "nặng ^",
        listOf(
            "vayf" convertsTo "vậy",
            "hiff" convertsTo "hiệp",
            "lof" convertsTo "lộ",
            "ruzf" convertsTo "ruộng",
        ),
    ),

    // 2. Nhóm trăng ~ (ơ, ư, ă)
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Sắc (Nhóm Ơ, Ư, Ă)",
        "x" convertsTo "sắc ~",
        listOf(
            "lamx" convertsTo "lắm",
            "ox" convertsTo "ớ",
            "otx" convertsTo "ớt",
            "ux" convertsTo "ứ",
            "cujx" convertsTo "cưới",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Huyền (Nhóm Ơ, Ư, Ă)",
        "k" convertsTo "huyền ~",
        listOf(
            "qank" convertsTo "quằn",
            "cok" convertsTo "cờ",
            "tuk" convertsTo "từ",
            "tuzk" convertsTo "tường",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Hỏi (Nhóm Ơ, Ư, Ă)",
        "v" convertsTo "hỏi ~",
        listOf(
            "hanv" convertsTo "hẳn",
            "fov" convertsTo "phở",
            "xuv" convertsTo "xử",
            "bujv" convertsTo "bưởi",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Ngã (Nhóm Ơ, Ư, Ă)",
        "w" convertsTo "ngã ~",
        listOf(
            "sanw" convertsTo "sẵn",
            "jonw" convertsTo "giỡn",
            "luw" convertsTo "lữ",
            "lujw" convertsTo "lưỡi",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Nặng (Nhóm Ơ, Ư, Ă)",
        "h" convertsTo "nặng ~",
        listOf(
            "hash" convertsTo "hoặc",
            "doih" convertsTo "đợi",
            "tuh" convertsTo "tự",
            "fuzh" convertsTo "phượng",
        ),
    ),

    // 3. Nhóm Không dấu phụ (a, e, i, o, u, y)
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Sắc (Không dấu phụ)",
        "j" convertsTo "sắc",
        listOf(
            "aj" convertsTo "á",
            "sakj" convertsTo "sách",
            "ej" convertsTo "é",
            "ij" convertsTo "í",
            "uj" convertsTo "ú",
            "yj" convertsTo "úy",
        ),
        "Các chữ có phụ âm cuối C, P, T thì không thêm J (ac ➔ ác, ep ➔ ép, at ➔ át)",
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Huyền (Không dấu phụ)",
        "l" convertsTo "huyền",
        listOf(
            "al" convertsTo "à",
            "hel" convertsTo "hè",
            "vil" convertsTo "vì",
            "conl" convertsTo "còn",
            "ul" convertsTo "ù",
            "tyl" convertsTo "tùy",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Hỏi (Không dấu phụ)",
        "z" convertsTo "hỏi",
        listOf(
            "az" convertsTo "ả",
            "rez" convertsTo "rẻ",
            "tiz" convertsTo "tỉ",
            "coz" convertsTo "cỏ",
            "uz" convertsTo "ủ",
            "qyz" convertsTo "quỷ",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Ngã (Không dấu phụ)",
        "s" convertsTo "ngã",
        listOf(
            "das" convertsTo "đã",
            "ves" convertsTo "vẽ",
            "mis" convertsTo "mỹ",
            "vos" convertsTo "võ",
            "cugs" convertsTo "cũng",
        ),
    ),
    RuleItem(
        HelpCategory.ToneMarks,
        "Dấu Nặng (Không dấu phụ)",
        "r" convertsTo "nặng",
        listOf(
            "ar" convertsTo "ạ",
            "mer" convertsTo "mẹ",
            "wir" convertsTo "nghị",
            "vur" convertsTo "vụ",
            "tyr" convertsTo "tụy",
        ),
    ),

    // 4. Ký hiệu P (Chữ đệm câm)
    RuleItem(
        HelpCategory.PWord,
        "Chữ đệm câm P",
        "suffix p" convertsTo "thanh ngang",
        listOf("logp" convertsTo "long", "xajp" convertsTo "xoay", "regp" convertsTo "reng"),
        "Đệm P sau vần rút gọn ở thanh ngang không dấu phụ để tránh trùng với lỗ = log, xá = xaj, rễ = reg",
    ),

    // 5. 56 Vần dài
    RuleItem(
        HelpCategory.Vần56,
        "Vần UYÊT / UYÊN",
        "yd / yl" convertsTo "uyêt / uyên",
        listOf("tydb" convertsTo "tuyết", "wylg" convertsTo "nguyễn"),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần IÊT / YÊT / IÊP / IÊC / IÊN / IÊM / IÊNG / IÊU",
        "id / if / is / il / iv / iz / iw" convertsTo "vần iê/yê",
        listOf(
            "vidb" convertsTo "viết",
            "hiff" convertsTo "hiệp",
            "visf" convertsTo "việc",
            "tild" convertsTo "tiền",
            "hivq" convertsTo "hiểm",
            "wizy" convertsTo "nghiêng",
            "liwg" convertsTo "liễu",
            "idb" convertsTo "yết",
            "ily" convertsTo "yên",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần UÔT / UÔC / UÔN / UÔM / UÔNG / UÔI",
        "ud / us / ul / uv / uz / uj" convertsTo "vần uô",
        listOf(
            "nudb" convertsTo "nuốt",
            "cusf" convertsTo "cuộc",
            "luly" convertsTo "luôn",
            "nhuvf" convertsTo "nhuộm",
            "uzq" convertsTo "uổng",
            "rujd" convertsTo "ruồi",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần ƯƠT / ƯƠP / ƯƠC / ƯƠN / ƯƠM / ƯƠNG / ƯƠU / ƯƠI",
        "ưd / ưf / ưs / ưl / ưv / ưz / ưw / ưj" convertsTo "vần ươ",
        listOf(
            "ludh" convertsTo "lượt",
            "cufx" convertsTo "cướp",
            "busx" convertsTo "bước",
            "mulx" convertsTo "mướn",
            "cuvk" convertsTo "cườm",
            "tuzv" convertsTo "tưởng",
            "ruwh" convertsTo "rượu",
            "lujw" convertsTo "lưỡi",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần UÂT / UÂN / UÂNG / UÂY",
        "âd / âl / âz / âj" convertsTo "vần uâ",
        listOf(
            "ladf" convertsTo "luật",
            "kalq" convertsTo "khuẩn",
            "kazy" convertsTo "khuâng",
            "kajy" convertsTo "khuây",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần UƠT / UƠN / UƠI",
        "ơd / ơl / ơj" convertsTo "vần uơ",
        listOf("hodx" convertsTo "huớt", "holw" convertsTo "huỡn", "ojo" convertsTo "uơi"),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần OĂT / OĂP / OĂC / OĂN / OĂM / OĂNG",
        "ăd / ăf / ăs / ăl / ăv / ăz" convertsTo "vần oă",
        listOf(
            "wadx" convertsTo "ngoắt",
            "wafh" convertsTo "ngoặp",
            "hash" convertsTo "hoặc",
            "xalx" convertsTo "xoắn",
            "avo" convertsTo "oăm",
            "hazw" convertsTo "hoẵng",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần OET / OEC / OEN / OEM / OENG / OEO",
        "ed / es / el / ev / ez / ew" convertsTo "vần oe",
        listOf(
            "tedj" convertsTo "toét",
            "xesr" convertsTo "xoẹc",
            "kelp" convertsTo "khoen",
            "wevj" convertsTo "ngoém",
            "nhezp" convertsTo "nhoeng",
            "wewz" convertsTo "ngoẻo",
        ),
    ),
    RuleItem(
        HelpCategory.Vần56,
        "Vần OAT / OAP / OAC / OAN / OAM / OANG / OAO / OAI / OAY",
        "od / of / os / ol / ov / oz / ow / oj / aj" convertsTo "vần oa",
        listOf(
            "lodr" convertsTo "loạt",
            "wofj" convertsTo "ngoáp",
            "kosj" convertsTo "khoác",
            "tolj" convertsTo "toán",
            "wovr" convertsTo "ngoạm",
            "kozz" convertsTo "khoảng",
            "wowj" convertsTo "ngoáo",
            "xojl" convertsTo "xoài",
            "xajp" convertsTo "xoay",
        ),
    ),
)

@Composable
@SenPreviewCommon
fun SenHelpScreenPreview() {
    SenTheme {
        SenHelpScreen(onNavigateBack = {})
    }
}
