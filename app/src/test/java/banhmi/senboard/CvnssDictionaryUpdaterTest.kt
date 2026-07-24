package banhmi.senboard

import banhmi.senboard.ime.engine.CvnssEngine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import java.io.File

@Serializable
data class UpdaterMetadata(
    val format: String,
    val total_entries: Int,
    val processed_entries: Int,
    val version: String
)

@Serializable
data class UpdaterDictionaryRoot(
    val metadata: UpdaterMetadata? = null,
    val dictionary: Map<String, DictEntry>
)

class CvnssDictionaryUpdaterTest {

    @Test
    fun updateDictionaryJsonToOfficialCvnss() {
        val dictFile = File("../dictionary.json")
        if (!dictFile.exists()) {
            println("dictionary.json not found!")
            return
        }

        val content = dictFile.readText(Charsets.UTF_8)
        val json = Json { 
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        val root = json.decodeFromString<UpdaterDictionaryRoot>(content)

        // Tiến hành chuẩn hóa cả từ tiếng Việt (key) và mã gõ (value)
        val updatedMap = LinkedHashMap<String, DictEntry>()
        for (entry in root.dictionary) {
            // Tạo mã gõ chuẩn
            val officialCode = CvnssEngine.encodeWord(entry.key)
            // Chuẩn hóa từ tiếng Việt gốc sang chính tả mới (ví dụ: qùa -> quà)
            val normalizedCqn = java.text.Normalizer.normalize(
                CvnssEngine.convertWord(officialCode), 
                java.text.Normalizer.Form.NFC
            )
            
            // Đưa vào map (nếu trùng từ sau khi chuẩn hóa sẽ tự ghi đè để loại bỏ trùng lặp)
            updatedMap[normalizedCqn] = DictEntry(
                cvnss = officialCode, 
                definition = entry.value.definition
            )
        }

        // Tạo root mới với metadata cập nhật số lượng từ thực tế sau khi gộp trùng
        val newRoot = UpdaterDictionaryRoot(
            metadata = UpdaterMetadata(
                format = "CVNSS4.0",
                total_entries = updatedMap.size,
                processed_entries = updatedMap.size,
                version = "4.0-Official"
            ),
            dictionary = updatedMap
        )
        val updatedContent = json.encodeToString(newRoot)

        dictFile.writeText(updatedContent, Charsets.UTF_8)
        println("Successfully normalized keys and updated dictionary.json!")
    }
}
