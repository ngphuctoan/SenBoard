package banhmi.senboard.app.settings

import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.navigation.SenSettingsAbout
import banhmi.senboard.app.navigation.SenSettingsHome
import banhmi.senboard.app.navigation.SenSettingsHaptics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenSettingsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenSettingsHome> {
            SenSettingsHomeScreen(onNavigate = { route -> navigator.goTo(route) })
        }
        entry<SenSettingsAbout> {
            SenSettingsAboutScreen(onNavigateBack = { navigator.goBack() })
        }
        entry<SenSettingsHaptics> {
            SenSettingsHapticsScreen(onNavigateBack = { navigator.goBack() })
        }
    }
}
