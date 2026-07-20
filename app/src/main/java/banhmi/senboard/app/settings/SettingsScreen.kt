package banhmi.senboard.app.settings

import androidx.activity.compose.BackHandler
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.InstructionsScreen
import banhmi.senboard.shared.utils.isAppInDarkTheme
import kotlinx.coroutines.delay

enum class SettingsDestination {
    Main,
    Input,
    Appearance,
    Feedback,
    About,
    Instructions,
}

@Composable
fun SettingsHost(onNavigateBack: () -> Unit) {
    var currentScreen by remember { mutableStateOf(SettingsDestination.Main) }
    val context = LocalContext.current
    val prefs = remember { SenBoardPreferences(context) }

    // State to track last back press timestamp for double-tap-to-exit
    var lastBackTime by remember { mutableLongStateOf(0L) }

    // Custom logo-free Toast state
    var toastMessage by remember { mutableStateOf<String?>(null) }

    // Auto-dismiss custom toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000)
            toastMessage = null
        }
    }

    val showToast = { msg: String -> toastMessage = msg }

    // Intercept back gesture
    if (currentScreen != SettingsDestination.Main) {
        BackHandler {
            currentScreen = SettingsDestination.Main
        }
    } else {
        BackHandler {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackTime < 2000) {
                (context as? Activity)?.finish()
            } else {
                lastBackTime = currentTime
                showToast("Nhấn back lần nữa để thoát")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            SettingsDestination.Main -> SettingsScreen(
                onNavigateTo = { currentScreen = it }
            )
            SettingsDestination.Input -> InputSettingsScreen(
                prefs = prefs,
                onNavigateBack = { currentScreen = SettingsDestination.Main },
            )
            SettingsDestination.Appearance -> AppearanceSettingsScreen(
                prefs = prefs,
                onNavigateBack = { currentScreen = SettingsDestination.Main },
            )
            SettingsDestination.Feedback -> FeedbackSettingsScreen(
                prefs = prefs,
                onNavigateBack = { currentScreen = SettingsDestination.Main },
            )
            SettingsDestination.About -> AboutScreen(
                onNavigateBack = { currentScreen = SettingsDestination.Main },
                showToast = showToast
            )
            SettingsDestination.Instructions -> InstructionsScreen(
                onNavigateBack = { currentScreen = SettingsDestination.Main },
            )
        }

        // Render custom logo-free Toast
        toastMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateTo: (SettingsDestination) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Cài đặt SenBoard",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Card 1: Thiết lập
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                val isDark = isAppInDarkTheme()
                SettingsCategoryRow(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    iconBackground = if (isDark) Color(0xFF4FD8EB) else Color(0xFF006874),
                    iconTint = if (isDark) Color(0xFF00363D) else Color.White,
                    title = "Hướng dẫn cài đặt",
                    subtitle = "Từng bước kích hoạt và sử dụng bàn phím",
                    onClick = { onNavigateTo(SettingsDestination.Instructions) }
                )
            }

            // Card 2: Phương thức gõ, Giao diện, Phản hồi
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                val isDark = isAppInDarkTheme()
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsCategoryRow(
                        icon = Icons.Outlined.Keyboard,
                        iconBackground = if (isDark) Color(0xFF8FFB91) else Color(0xFF006D14),
                        iconTint = if (isDark) Color(0xFF00390A) else Color.White,
                        title = "Phương thức gõ",
                        subtitle = "Chế độ gõ, tự động sửa, gợi ý từ",
                        onClick = { onNavigateTo(SettingsDestination.Input) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    )
                    SettingsCategoryRow(
                        icon = Icons.Outlined.Palette,
                        iconBackground = if (isDark) Color(0xFFAAC7FF) else Color(0xFF005FAF),
                        iconTint = if (isDark) Color(0xFF003061) else Color.White,
                        title = "Giao diện",
                        subtitle = "Chủ đề, chiều cao bàn phím, viền phím",
                        onClick = { onNavigateTo(SettingsDestination.Appearance) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    )
                    SettingsCategoryRow(
                        icon = Icons.Outlined.Vibration,
                        iconBackground = if (isDark) Color(0xFFFFB862) else Color(0xFF8B5000),
                        iconTint = if (isDark) Color(0xFF4A2800) else Color.White,
                        title = "Phản hồi",
                        subtitle = "Rung và âm thanh khi nhấn phím",
                        onClick = { onNavigateTo(SettingsDestination.Feedback) }
                    )
                }
            }

            // Card 3: Giới thiệu
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                val isDark = isAppInDarkTheme()
                SettingsCategoryRow(
                    icon = Icons.Outlined.Info,
                    iconBackground = if (isDark) Color(0xFFC6C6CA) else Color(0xFF5E5E62),
                    iconTint = if (isDark) Color(0xFF2E3134) else Color.White,
                    title = "Giới thiệu",
                    subtitle = "Phiên bản bàn phím, thông tin phát triển",
                    onClick = { onNavigateTo(SettingsDestination.About) }
                )
            }
        }
    }
}

@Composable
private fun SettingsCategoryRow(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Colored Circular Icon Container (CircleShape)
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconBackground, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}
