package banhmi.senboard.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/* A state holder for toasts, where every bake() call will override the previous toast,
so newer toast don't have to wait for the previous one to finish displaying */
class Toaster(
    private val context: Context,
) {
    private var currentToast: Toast? by mutableStateOf(null)

    // Bake me a toast 🍞
    fun bake(
        message: String,
        // Short length is commonly used, so might as well make it the default length
        length: Int = Toast.LENGTH_SHORT,
    ) {
        if (currentToast != null) currentToast?.cancel()
        currentToast = Toast.makeText(context, message, length).apply { show() }
    }
}

@Composable
fun rememberToaster(
    context: Context,
) = remember {
    Toaster(context)
}
