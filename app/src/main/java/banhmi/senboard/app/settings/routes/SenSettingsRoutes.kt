package banhmi.senboard.app.settings.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface SenSettingsRoutes {
    @Serializable
    object SettingsRoute : SenSettingsRoutes

    @Serializable
    object SoundsAndHapticsSettingsRoute : SenSettingsRoutes

    @Serializable
    object AboutRoute : SenSettingsRoutes
}
