package com.paridhi.onemoment.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.paridhi.onemoment.navigation.AppNavigation
import com.paridhi.onemoment.ui.splash.SplashScreen

@Composable
fun OneMomentApp() {
    var showSplash by remember { mutableStateOf(true) }

    AnimatedContent(
        targetState = showSplash,
        transitionSpec = {
            fadeIn(animationSpec = tween(1000)) togetherWith
                    fadeOut(animationSpec = tween(1000))
        },
        label = "AppTransition"
    ) { isSplashing ->
        if (isSplashing) {
            SplashScreen(onSplashFinished = { showSplash = false })
        } else {
            AppNavigation()
        }
    }
}
