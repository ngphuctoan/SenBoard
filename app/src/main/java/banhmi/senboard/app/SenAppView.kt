package banhmi.senboard.app

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.settings.SettingsHost
import banhmi.senboard.ui.theme.SenBoardTheme

@Composable
fun SenAppView() {
    // Directly launch the Settings screen home page when opening the app
    SettingsHost(onNavigateBack = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionsScreen(
    onNavigateBack: () -> Unit,
) {
    var preview by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hướng dẫn cài đặt", fontWeight = FontWeight.Bold) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    "Kích hoạt bàn phím ảo theo các bước sau:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Instruction(1) { 
                        Text(
                            "Mở phần cài đặt hệ thống và bật bàn phím ảo SenBoard bằng nút bấm phía dưới.",
                            style = MaterialTheme.typography.bodyLarge
                        ) 
                    }
                    Instruction(2) { 
                        Text(
                            "Chuyển đổi phương thức nhập liệu hiện tại sang bàn phím SenBoard.",
                            style = MaterialTheme.typography.bodyLarge
                        ) 
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                KeyboardSettingsButton {
                    Text("Kích hoạt bàn phím ảo", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Icon(
                        Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = "Mở cài đặt hệ thống"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Hãy trải nghiệm bàn phím ảo mới của bạn Dà húuuu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                KeyboardPreviewTextField(
                    label = { Text("Gõ thử bàn phím tại đây") },
                    tooltipLabel = "Đổi bố cục bàn phím",
                    value = preview,
                    onValueChange = { preview = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun KeyboardSettingsButton(content: @Composable () -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        content()
    }
}

@Composable
fun Instruction(count: Int, content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        InstructionCounter(count)
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}

@Composable
fun InstructionCounter(count: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(36.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
    ) {
        Text(
            count.toString(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}
