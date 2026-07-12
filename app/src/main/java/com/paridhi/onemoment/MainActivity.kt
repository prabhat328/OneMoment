package com.paridhi.onemoment

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.paridhi.onemoment.ui.OneMomentApp
import com.paridhi.onemoment.ui.theme.LocalIsDarkMode
import com.paridhi.onemoment.ui.theme.LocalThemeToggle
import com.paridhi.onemoment.ui.theme.OneMomentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

        setContent {
            val systemTheme = isSystemInDarkTheme()
            val isDarkThemePref = sharedPreferences.getBoolean("is_dark_theme", systemTheme)

            var isDarkMode by remember { mutableStateOf(isDarkThemePref) }

            val toggleTheme: (Boolean) -> Unit = { isDark ->
                isDarkMode = isDark
                sharedPreferences.edit().putBoolean("is_dark_theme", isDark).apply()
            }

            CompositionLocalProvider(
                LocalIsDarkMode provides isDarkMode,
                LocalThemeToggle provides toggleTheme
            ) {
                OneMomentTheme(darkTheme = isDarkMode) {
                    OneMomentApp()
                }
            }
        }
    }
}