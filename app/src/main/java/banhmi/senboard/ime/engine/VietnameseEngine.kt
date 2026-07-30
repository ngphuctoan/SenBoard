package banhmi.senboard.ime.engine

import kotlinx.serialization.Serializable

@Serializable
enum class VietnameseEngine(val value: Int) {
    None(0), Telex(1), Vni(2), Cvnss40(3),
}
