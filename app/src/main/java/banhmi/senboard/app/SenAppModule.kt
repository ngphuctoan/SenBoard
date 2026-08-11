package banhmi.senboard.app

import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.settings.SenSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenAppModule {
    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): SenNavigator = SenNavigator(startDestination = SenSettings)
}
