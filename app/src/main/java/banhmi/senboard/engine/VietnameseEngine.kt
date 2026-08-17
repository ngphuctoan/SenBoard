package banhmi.senboard.engine

import banhmi.senboard.engine.impl.CvnssEngine
import banhmi.senboard.engine.impl.PlainTextEngine
import banhmi.senboard.engine.impl.TelexEngine
import banhmi.senboard.engine.impl.VniEngine
import kotlinx.serialization.Serializable

interface VietnameseEngine {
    fun convertWord(rawWord: String): String
}

@Serializable
enum class VietnameseEngineType(
    val id: Int, // DataStore internal value
    val shortName: String, // For the toolbar
    val description: String, // For the settings page
) {
    PlainText(id = 0, shortName = "E", description = "Không gõ dấu"),
    Telex(id = 1, shortName = "T", description = "Telex"),
    Vni(id = 2, shortName = "V", description = "VNI"),
    Cvss(id = 3, shortName = "S", description = "Chữ Việt song song");
}

fun provideVietnameseEngine(vietnameseEngineType: VietnameseEngineType): VietnameseEngine =
    when (vietnameseEngineType) {
        VietnameseEngineType.PlainText -> PlainTextEngine
        VietnameseEngineType.Telex -> TelexEngine
        VietnameseEngineType.Vni -> VniEngine
        VietnameseEngineType.Cvss -> CvnssEngine
    }
