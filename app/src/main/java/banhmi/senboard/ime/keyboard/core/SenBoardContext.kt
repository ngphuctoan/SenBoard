package banhmi.senboard.ime.keyboard.core

import android.inputmethodservice.InputMethodService
import android.view.inputmethod.InputConnection
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class SenBoardContext(
    private val im: InputMethodService,
    initialState: SenBoardState = SenBoardState(),
) {
    var state by mutableStateOf(initialState)
        internal set

    fun getEditor(): InputConnection? = im.currentInputConnection
}
