package com.gdproduct.rendana

import android.graphics.Color
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController

import com.gdproduct.rendana.ui.RendanaNavigator
import com.gdproduct.rendana.ui.screens.settings.SettingsViewModel
import com.gdproduct.rendana.ui.theme.RendanaTheme

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
		
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
		
        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState()
            RendanaTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                RendanaNavigator(
                    navController = navController
                )
            }
        }
    }
}