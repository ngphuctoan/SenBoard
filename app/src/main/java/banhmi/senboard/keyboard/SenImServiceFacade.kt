package banhmi.senboard.keyboard

import android.inputmethodservice.InputMethodService
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

/* A facade that only exposes the important services to the handlers. Additionally, it also
makes getting any service that requires Android version code matching easier */
class SenImServiceFacade(private val imService: InputMethodService) {
    val inputConnection: InputConnection get() = imService.currentInputConnection

    val editorInfo: EditorInfo = imService.currentInputEditorInfo
}
