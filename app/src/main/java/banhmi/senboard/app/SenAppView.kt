package banhmi.senboard.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

            composable<SenSettingsRoutes.SoundsAndHapticsSettingsRoute> {
                SoundsAndHapticsSettingsScreen()
            }

            composable<SenSettingsRoutes.AboutRoute> {
                AboutScreen()
            }
        }
    }
}

//package banhmi.senboard.app
//
//import android.content.Context
//import android.content.Intent
//import android.provider.Settings
//import android.view.inputmethod.InputMethodManager
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.outlined.ArrowBack
//import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
//import androidx.compose.material.icons.outlined.Check
//import androidx.compose.material.icons.outlined.Close
//import androidx.compose.material.icons.outlined.Edit
//import androidx.compose.material.icons.outlined.Keyboard
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.SwitchDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalWindowInfo
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleEventObserver
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import banhmi.senboard.app.settings.SettingsHost
//
//@Composable
//fun SenAppView() {
//    // Directly launch the Settings screen home page when opening the app
//    SettingsHost(onNavigateBack = {})
//}
//
//fun isKeyboardEnabled(context: Context): Boolean {
//    val imeManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    val enabledImes = imeManager.enabledInputMethodList
//    return enabledImes.any { it.packageName == context.packageName }
//}
//
//fun isKeyboardSelected(context: Context): Boolean {
//    val currentIme = Settings.Secure.getString(
//        context.contentResolver,
//        Settings.Secure.DEFAULT_INPUT_METHOD
//    )
//    return currentIme?.contains(context.packageName) == true
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InstructionsScreen(
//    onNavigateBack: () -> Unit,
//) {
//    val context = LocalContext.current
//    var isEnabled by remember { mutableStateOf(isKeyboardEnabled(context)) }
//    var isSelected by remember { mutableStateOf(isKeyboardSelected(context)) }
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                isEnabled = isKeyboardEnabled(context)
//                isSelected = isKeyboardSelected(context)
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    val isWindowFocused = LocalWindowInfo.current.isWindowFocused
//    LaunchedEffect(isWindowFocused) {
//        if (isWindowFocused) {
//            isEnabled = isKeyboardEnabled(context)
//            isSelected = isKeyboardSelected(context)
//        }
//    }
//
//    var previewText by remember { mutableStateOf("") }
//    val isDark = isSystemInDarkTheme()
//
//    // Pure Samsung One UI color guidelines
//    val backgroundColor = if (isDark) Color(0xFF000000) else Color(0xFFF4F5F7)
//    val cardColor = if (isDark) Color(0xFF151518) else Color(0xFFFFFFFF)
//    val textPrimary = if (isDark) Color(0xFFFFFFFF) else Color(0xFF111111)
//    val textSecondary = if (isDark) Color(0xFF8E8E93) else Color(0xFF6E6E73)
//    val accentColor = Color(0xFF007AFF) // Samsung Blue
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {},
//                navigationIcon = {
//                    IconButton(onClick = onNavigateBack) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
//                            contentDescription = "Quay lại",
//                            tint = textPrimary
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = backgroundColor,
//                ),
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(backgroundColor)
//                .padding(innerPadding)
//                .imePadding() // Adjust padding dynamically when keyboard is shown
//                .verticalScroll(rememberScrollState())
//                .padding(horizontal = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Samsung big settings header block
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 8.dp, top = 8.dp, bottom = 12.dp)
//            ) {
//                Text(
//                    text = "Thiết lập",
//                    fontSize = 38.sp,
//                    fontWeight = FontWeight.Light, // Samsung Light header style
//                    color = textPrimary,
//                    letterSpacing = (-1.0).sp
//                )
//            }
//
//            // Samsung settings block 1: Steps Group Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(26.dp), // High rounded corners like One UI 6+
//                colors = CardDefaults.cardColors(containerColor = cardColor),
//                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//            ) {
//                Column(modifier = Modifier.fillMaxWidth()) {
//
//                    // Row 1: Enable Keyboard in settings
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
//                            }
//                            .padding(horizontal = 16.dp, vertical = 16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        // Blue circular icon container
//                        Box(
//                            modifier = Modifier
//                                .size(36.dp)
//                                .background(Color(0xFF0A84FF), shape = CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Outlined.Keyboard,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "Kích hoạt bàn phím",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = textPrimary
//                            )
//                            Text(
//                                text = if (isEnabled) "Đã bật trong Cài đặt hệ thống" else "Bật SenBoard trong danh sách bàn phím",
//                                fontSize = 14.sp,
//                                color = if (isEnabled) Color(0xFF34A853) else textSecondary
//                            )
//                        }
//
//                        if (isEnabled) {
//                            Icon(
//                                imageVector = Icons.Outlined.Check,
//                                contentDescription = null,
//                                tint = Color(0xFF34A853),
//                                modifier = Modifier.size(20.dp)
//                            )
//                        } else {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
//                                contentDescription = null,
//                                tint = textSecondary.copy(alpha = 0.5f),
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//
//                    HorizontalDivider(
//                        modifier = Modifier.padding(start = 68.dp),
//                        color = textSecondary.copy(alpha = 0.08f)
//                    )
//
//                    // Row 2: Select Keyboard
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable(enabled = isEnabled) {
//                                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//                                imm?.showInputMethodPicker()
//                            }
//                            .padding(horizontal = 16.dp, vertical = 16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        // Purple circular icon container
//                        Box(
//                            modifier = Modifier
//                                .size(36.dp)
//                                .background(
//                                    if (isEnabled) Color(0xFF5856D6) else Color.Gray.copy(alpha = 0.4f),
//                                    shape = CircleShape
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Outlined.Keyboard,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "Chọn phương thức nhập liệu",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = if (isEnabled) textPrimary else textPrimary.copy(alpha = 0.4f)
//                            )
//                            Text(
//                                text = if (isSelected) "Đang sử dụng SenBoard" else "Đặt SenBoard làm bàn phím mặc định",
//                                fontSize = 14.sp,
//                                color = if (isSelected) Color(0xFF34A853) else textSecondary.copy(alpha = if (isEnabled) 1f else 0.4f)
//                            )
//                        }
//
//                        if (isSelected) {
//                            Icon(
//                                imageVector = Icons.Outlined.Check,
//                                contentDescription = null,
//                                tint = Color(0xFF34A853),
//                                modifier = Modifier.size(20.dp)
//                            )
//                        } else {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
//                                contentDescription = null,
//                                tint = textSecondary.copy(alpha = if (isEnabled) 0.5f else 0.2f),
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Samsung settings block 2: Try Typing Box
//            Text(
//                text = "Gõ thử nghiệm",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = accentColor,
//                modifier = Modifier.padding(start = 12.dp, top = 8.dp)
//            )
//
//            // Premium test input field
//            TextField(
//                value = previewText,
//                onValueChange = { previewText = it },
//                enabled = isEnabled,
//                placeholder = {
//                    Text(
//                        text = "Nhấp để gõ thử tại đây...",
//                        color = textSecondary.copy(alpha = if (isEnabled) 0.7f else 0.4f),
//                        fontSize = 16.sp
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Outlined.Edit,
//                        contentDescription = null,
//                        tint = textSecondary.copy(alpha = if (isEnabled) 0.7f else 0.4f)
//                    )
//                },
//                trailingIcon = {
//                    if (previewText.isNotEmpty()) {
//                        IconButton(onClick = { previewText = "" }) {
//                            Icon(
//                                imageVector = Icons.Outlined.Close,
//                                contentDescription = "Xóa chữ",
//                                tint = textSecondary
//                            )
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(28.dp),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = cardColor,
//                    unfocusedContainerColor = cardColor,
//                    disabledContainerColor = cardColor.copy(alpha = 0.5f),
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent,
//                    cursorColor = accentColor,
//                    focusedTextColor = textPrimary,
//                    unfocusedTextColor = textPrimary
//                ),
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//        }
//    }
//}
