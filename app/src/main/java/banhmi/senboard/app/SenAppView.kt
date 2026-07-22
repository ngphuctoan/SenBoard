package banhmi.senboard.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import banhmi.senboard.app.settings.routes.SenSettingsRoutes
import banhmi.senboard.app.settings.screens.AboutScreen
import banhmi.senboard.app.settings.screens.AppearanceSettingsScreen
import banhmi.senboard.app.settings.screens.InputMethodSettingsScreen
import banhmi.senboard.app.settings.screens.InstructionsScreen
import banhmi.senboard.app.settings.screens.LicenseScreen
import banhmi.senboard.app.settings.screens.SettingsScreen
import banhmi.senboard.app.settings.screens.SoundsAndHapticsSettingsScreen

@Composable
fun SenAppView() {
    val navController = rememberNavController()
    val onBackClick: () -> Unit = { navController.popBackStack() }

    /*
    * Copied straight from AOSP :b
    * References:
    * - https://android.googlesource.com/platform/packages/apps/Car/Settings/+/1b73e25e75ed6d6e14e6a74e2f2c994fea547937/res/animator/
    * - https://android.googlesource.com/platform/frameworks/base/+/1cdfff555f4a21f71ccc978290e2e212e2f8b168/core/res/res/values/config.xml
    * - https://android.googlesource.com/platform/frameworks/base/+/1cdfff555f4a21f71ccc978290e2e212e2f8b168/core/res/res/interpolator/decelerate_quint.xml
    */
    val mediumAnimationDuration = 400
    val decelerateQuint = EaseOutQuint
    val slideDistance = with(LocalDensity.current) { 100.dp.roundToPx() }

    NavHost(
        navController = navController,
        startDestination = SenSettingsRoutes.SettingsRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = mediumAnimationDuration,
                    easing = decelerateQuint,
                ),
                initialOffset = { slideDistance },
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = mediumAnimationDuration,
                    easing = decelerateQuint,
                ),
            )
        },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = mediumAnimationDuration,
                    easing = decelerateQuint,
                ),
                targetOffset = { slideDistance },
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = mediumAnimationDuration,
                    easing = decelerateQuint,
                ),
            )
        },
    ) {
        composable<SenSettingsRoutes.SettingsRoute> {
            SettingsScreen(onRouteNavigate = { navController.navigate(it) })
        }

        composable<SenSettingsRoutes.InstructionsRoute> {
            InstructionsScreen(onBackClick)
        }

        composable<SenSettingsRoutes.InputMethodSettingsRoute> {
            InputMethodSettingsScreen(onBackClick)
        }

        composable<SenSettingsRoutes.AppearanceSettingsRoute> {
            AppearanceSettingsScreen(onBackClick)
        }

        composable<SenSettingsRoutes.SoundsAndHapticsSettingsRoute> {
            SoundsAndHapticsSettingsScreen(onBackClick)
        }

        composable<SenSettingsRoutes.AboutRoute> {
            AboutScreen(
                onBackClick = onBackClick,
                onLicenseClick = { navController.navigate(SenSettingsRoutes.LicenseRoute) },
            )
        }

        composable<SenSettingsRoutes.LicenseRoute> {
            LicenseScreen(onBackClick)
        }
    }
}
