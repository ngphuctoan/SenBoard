package banhmi.senboard

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import banhmi.senboard.ui.theme.SenBoardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenAppView() {
    var preview by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        SenBoardTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text("Installation instructions:")

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Instruction(1) { Text("Open Keyboard Settings and enable SenBoard using the button below.") }
                        Instruction(2) { Text("Switch your current keyboard to SenBoard.") }
                    }

                    KeyboardSettingsButton {
                        Text("Enable the keyboard")
                        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                        Icon(
                            Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Open the keyboard settings page"
                        )
                    }

                    Text("Enjoy the new keyboard \uD83C\uDF89")

                    KeyboardPreviewTextField(
                        label = { Text("Test keyboard here") },
                        tooltipLabel = "Change keyboard layout",
                        value = preview,
                        onValueChange = { preview = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
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
        content()
    }
}

@Composable
fun InstructionCounter(count: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(32.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
    ) {
        Text(
            count.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}
