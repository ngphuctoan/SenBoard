package banhmi.senboard.ime.keyboard.models

data class Mode(
    val name: String,
    val layout: Layout,
    val slots: List<KeyData>,
)
