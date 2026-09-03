package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import banhmi.senboard.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.ui.theme.GoogleSansCodeFontFamily
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenLicense

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenLicenseModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenLicense> {
            SenLicenseScreen(navigator)
        }
    }
}

object SenLicenseDefaults {
    @JvmStatic
    internal val LicenseFileName = "license"

    @Composable
    internal fun textStyle() = LocalTextStyle.current.copy(fontFamily = GoogleSansCodeFontFamily)
}

@Composable
fun SenLicenseScreen(
    navigator: SenNavigator,
) {
    SenLicenseContent(
        onNavigateBack = navigator::goBack,
    )
}

@Composable
fun SenLicenseContent(
    onNavigateBack: () -> Unit = {},
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val context = LocalContext.current

    val licenseText = context.assets
        .open(SenLicenseDefaults.LicenseFileName)
        .bufferedReader()
        .readText()

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Giấy phép") },
                navigationIcon = { SenTopBarBackButton(onClick = onNavigateBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item {
                Text(
                    text = licenseText,
                    style = SenLicenseDefaults.textStyle(),
                )
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenLicensePreview() {
    SenTheme {
        SenLicenseContent()
    }
}
