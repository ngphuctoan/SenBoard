package banhmi.senboard

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.core.content.res.ResourcesCompat
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import banhmi.senboard.app.icon.LocalSenAppIcon
import banhmi.senboard.app.icon.SenAppIconResult
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.ui.theme.SenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SenActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: SenNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards SenEntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val resources = LocalResources.current

            val appIcon = try {
                val drawable = ResourcesCompat.getDrawable(resources, R.mipmap.ic_launcher, context.theme)
                if (drawable != null) {
                    SenAppIconResult.Success(drawable)
                } else {
                    SenAppIconResult.Failed
                }
            } catch (_: Resources.NotFoundException) {
                SenAppIconResult.Failed
            }

            SenTheme {
                CompositionLocalProvider(LocalSenAppIcon provides appIcon) {
                    NavDisplay(
                        backStack = navigator.backStack,
                        onBack = { navigator.goBack() },
                        entryProvider = entryProvider {
                            entryProviderScopes.forEach { builder -> this.builder() }
                        },
                        transitionSpec = {
                            // Slide in from right when navigating forward
                            slideInHorizontally(
                                initialOffsetX = { 100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeIn(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) togetherWith slideOutHorizontally(
                                targetOffsetX = { -100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeOut(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            )
                        },
                        popTransitionSpec = {
                            // Slide in from left when navigating back
                            slideInHorizontally(
                                initialOffsetX = { -100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeIn(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) togetherWith slideOutHorizontally(
                                targetOffsetX = { 100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeOut(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            )
                        },
                        predictivePopTransitionSpec = {
                            // Slide in from left when navigating back
                            slideInHorizontally(
                                initialOffsetX = { -100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeIn(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) togetherWith slideOutHorizontally(
                                targetOffsetX = { 100 },
                                animationSpec = tween(400, easing = EaseOutQuint),
                            ) + fadeOut(
                                animationSpec = tween(400, easing = EaseOutQuint),
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
                    )
                }
            }
        }
    }
}
