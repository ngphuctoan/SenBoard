package banhmi.senboard.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.SpaceBar
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSettingsScreen(
    prefs: SenBoardPreferences,
    onNavigateBack: () -> Unit,
) {
    var typingMode by remember { mutableStateOf(prefs.typingMode) }
    var autoCapitalize by remember { mutableStateOf(prefs.autoCapitalize) }
    var doubleSpacePeriod by remember { mutableStateOf(prefs.doubleSpacePeriod) }
    var showSuggestions by remember { mutableStateOf(prefs.showSuggestions) }
    
    // Capture initial state to keep the toggle visible until navigation
    val initialDeveloperMode = remember { prefs.isDeveloperMode }
    var isDeveloperMode by remember { mutableStateOf(prefs.isDeveloperMode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phương thức gõ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Quay lại")
                    }
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Category 1: Typing Mode Selection Card
            Column {
                Text(
                    text = "Chế độ gõ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
                )

                val modes = listOf(
                    "telex" to "Bộ gõ Telex tiếng Việt",
                    "cvnss" to "Tốc ký CVNSS 4.0",
                    "direct" to "Gõ thẳng chữ Latin (Không dấu)"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        modes.forEachIndexed { index, (key, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (typingMode == key),
                                        onClick = {
                                            typingMode = key
                                            prefs.typingMode = key
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (typingMode == key),
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (index < modes.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 56.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                )
                            }
                        }
                    }
                }
            }

            // Category 2: Text Correction Cards (Switches)
            Column {
                Text(
                    text = "Sửa văn bản",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        // Switch 1: Auto Capitalization
                        InputSwitchRow(
                            icon = Icons.Outlined.Title,
                            iconColor = Color(0xFF007AFF), // Blue
                            title = "Tự động viết hoa đầu câu",
                            subtitle = "Tự động viết hoa chữ cái đầu tiên của câu mới",
                            checked = autoCapitalize,
                            onCheckedChange = {
                                autoCapitalize = it
                                prefs.autoCapitalize = it
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 56.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                        )
                        // Switch 2: Double Space Period
                        InputSwitchRow(
                            icon = Icons.Outlined.SpaceBar,
                            iconColor = Color(0xFF4CD964), // Green
                            title = "Dấu chấm bằng phím cách",
                            subtitle = "Nhấn đúp phím cách để chèn dấu chấm câu nhanh",
                            checked = doubleSpacePeriod,
                            onCheckedChange = {
                                doubleSpacePeriod = it
                                prefs.doubleSpacePeriod = it
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 56.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                        )
                        // Switch 3: Show Suggestions
                        InputSwitchRow(
                            icon = Icons.Outlined.Lightbulb,
                            iconColor = Color(0xFFFF9500), // Orange
                            title = "Hiện thanh gợi ý từ",
                            subtitle = "Hiển thị các từ gợi ý và tốc ký khi gõ",
                            checked = showSuggestions,
                            onCheckedChange = {
                                showSuggestions = it
                                prefs.showSuggestions = it
                            }
                        )

                        if (initialDeveloperMode) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 56.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            )
                            InputSwitchRow(
                                icon = Icons.Outlined.Redeem,
                                iconColor = Color(0xFF007AFF), // Orange
                                title = "Easter egg",
                                subtitle = "Turn off easter egg",
                                checked = isDeveloperMode,
                                onCheckedChange = {
                                    isDeveloperMode = it
                                    prefs.isDeveloperMode = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InputSwitchRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconColor.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
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

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
