package banhmi.senboard.shared.utils

import android.content.Context
import android.widget.Toast

object ToastManager {
    private var currentToast: Toast? = null

    fun show(context: Context, overrideLast: Boolean = true, block: (Context) -> Toast) {
        if (overrideLast) currentToast?.cancel()
        currentToast = block(context).apply { show() }
    }
}
