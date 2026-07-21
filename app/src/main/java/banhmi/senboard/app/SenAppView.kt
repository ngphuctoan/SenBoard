package banhmi.senboard.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import banhmi.senboard.app.settings.routes.SenSettingsRoutes
import banhmi.senboard.app.settings.screens.AboutScreen
import banhmi.senboard.app.settings.screens.AppearanceSettingsScreen
import banhmi.senboard.app.settings.screens.InputMethodSettingsScreen
import banhmi.senboard.app.settings.screens.InstructionsScreen
import banhmi.senboard.app.settings.screens.SettingsScreen
import banhmi.senboard.app.settings.screens.SoundsAndHapticsSettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenAppViewTopAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = "Quay lại Cài đặt",
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}

@Composable
fun SenAppViewScaffold(
    title: String,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { SenAppViewTopAppBar(title, onBackClick) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.ime),
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun SenAppView() {
    val navController = rememberNavController()

    var topAppBarTitle by remember {
        mutableStateOf("Cài đặt SenBoard")
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        topAppBarTitle = when {
            destination.hasRoute<SenSettingsRoutes.InstructionsRoute>() -> "Hướng dẫn"
            destination.hasRoute<SenSettingsRoutes.InputMethodSettingsRoute>() -> "Phương thức nhập"
            destination.hasRoute<SenSettingsRoutes.AppearanceSettingsRoute>() -> "Giao diện"
            destination.hasRoute<SenSettingsRoutes.SoundsAndHapticsSettingsRoute>() -> "Âm thanh & Haptic"
            destination.hasRoute<SenSettingsRoutes.AboutRoute>() -> "Giới thiệu"
            else -> "Cài đặt SenBoard"
        }
    }

    val canNavigateBack = navController.previousBackStackEntry != null

    SenAppViewScaffold(
        title = topAppBarTitle,
        onBackClick = if (canNavigateBack) {
            { navController.popBackStack() }
        } else null,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SenSettingsRoutes.SettingsRoute,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { it / 4 }) + fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it / 4 }) + fadeOut() },
        ) {
            composable<SenSettingsRoutes.SettingsRoute> {
                SettingsScreen(onRouteNavigate = { navController.navigate(it) })
            }

            composable<SenSettingsRoutes.InstructionsRoute> {
                InstructionsScreen()
            }

            composable<SenSettingsRoutes.InputMethodSettingsRoute> {
                InputMethodSettingsScreen()
            }

            composable<SenSettingsRoutes.AppearanceSettingsRoute> {
                AppearanceSettingsScreen()
            }

            composable<SenSettingsRoutes.SoundsAndHapticsSettingsRoute> {
                SoundsAndHapticsSettingsScreen()
            }

            composable<SenSettingsRoutes.AboutRoute> {
                AboutScreen()
            }
        }
    }
}
