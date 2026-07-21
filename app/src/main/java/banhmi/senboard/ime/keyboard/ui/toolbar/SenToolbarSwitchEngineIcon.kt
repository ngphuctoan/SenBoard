package banhmi.senboard.ime.keyboard.ui.toolbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class SwitchEngineLabel(val content: String) {
    None("E"),
    Telex("T"),
    Vni("V"),
    Viqr("Q"),
    Cvnss40("S"),
}

@Composable
fun SenToolbarSwitchEngineIcon(label: SwitchEngineLabel = SwitchEngineLabel.None) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 2.dp,
                color = LocalContentColor.current,
                shape = RoundedCornerShape(4.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.content,
            color = LocalContentColor.current,
            fontWeight = FontWeight.Bold,
        )
    }
}
