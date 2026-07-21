package banhmi.senboard.app.settings.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface SenSettingsRoutes {
    @Serializable
    object SettingsRoute : SenSettingsRoutes

    @Serializable
    object InstructionsRoute : SenSettingsRoutes

    @Serializable
    object InputMethodSettingsRoute : SenSettingsRoutes

    @Serializable
    object AppearanceSettingsRoute : SenSettingsRoutes

    @Serializable
    object SoundsAndHapticsSettingsRoute : SenSettingsRoutes

    @Serializable
    object AboutRoute : SenSettingsRoutes
}
