package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenHelp

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenHelpModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(): SenEntryProviderInstaller = {
        entry<SenHelp> {
            SenHelpScreen()
        }
    }
}

@Composable
fun SenHelpScreen() {
    SenScaffold(
        topBar = {},
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {}
    }
}

@Composable
@SenPreviewCommon
fun SenHelpScreenPreview() {
    SenTheme {
        SenHelpScreen()
    }
}
