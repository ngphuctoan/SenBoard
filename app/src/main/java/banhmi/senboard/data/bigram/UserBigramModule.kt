package banhmi.senboard.data.bigram

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_BIGRAM_DATASTORE_NAME = "sen_settings"

@Module
@InstallIn(SingletonComponent::class)
object UserBigramModule {
    @Provides
    @Singleton
    fun providesUserBigramDataStore(
        @ApplicationContext context: Context,
    ) = DataStoreFactory.create(
        serializer = UserBigramSerializer,
        produceFile = { context.dataStoreFile(USER_BIGRAM_DATASTORE_NAME) },
    )
}
