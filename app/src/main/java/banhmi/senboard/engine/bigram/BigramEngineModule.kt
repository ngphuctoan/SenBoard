package banhmi.senboard.engine.bigram

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BigramEngineModule {
    @Provides
    @Singleton
    fun providesBigramEngineModule(
        @ApplicationContext context: Context,
    ) = BigramEngine(context)
}
