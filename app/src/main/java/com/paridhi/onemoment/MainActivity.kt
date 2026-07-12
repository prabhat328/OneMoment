package com.paridhi.onemoment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.paridhi.onemoment.ui.OneMomentApp
import com.paridhi.onemoment.ui.theme.OneMomentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OneMomentTheme {
                OneMomentApp()
            }
        }
    }
}