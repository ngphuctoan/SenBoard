package banhmi.senboard.ime.engine

import kotlinx.serialization.Serializable

@Serializable
enum class VietnameseEngine(
    val dataStoreValue: Int, // DataStore internal value
    val engineName: String, // For the settings page
) {
    Telex(dataStoreValue = 1, engineName = "Telex"),
    Vni(dataStoreValue = 2, engineName = "VNI"),
    Cvss(dataStoreValue = 3, engineName = "Chữ Việt song song"),
}
