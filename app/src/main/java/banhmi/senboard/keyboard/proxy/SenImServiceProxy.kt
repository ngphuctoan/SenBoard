package banhmi.senboard.keyboard.proxy

import android.view.inputmethod.InputConnection
import banhmi.senboard.keyboard.SenImService

/* A proxy that only exposes the important services to the handlers. Additionally, it also
makes getting any service that requires Android version code matching easier */
class SenImServiceProxy(private val imService: SenImService) {
    val inputConnection: InputConnection get() = imService.currentInputConnection
}
