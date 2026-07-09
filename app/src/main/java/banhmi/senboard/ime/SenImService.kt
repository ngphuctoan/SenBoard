package banhmi.senboard.ime

import android.view.View
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView

class SenImService : LifecycleImService() {
    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setViewTreeOwners()
            setContent {
                MaterialTheme {
                    Button(onClick = { currentInputConnection.commitText("a", 1) }) {
                        Text("a")
                    }
                }
            }
        }
    }
}