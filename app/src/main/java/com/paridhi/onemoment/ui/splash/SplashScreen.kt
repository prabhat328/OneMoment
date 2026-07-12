package com.paridhi.onemoment.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paridhi.onemoment.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    // ---------- Entrance animation states ----------
    val leafAlpha = remember { Animatable(0f) }
    val leafScale = remember { Animatable(0.8f) }
    val leafDropOffsetY = remember { Animatable(-28f) } // starts slightly above, drifts down into place
    val leafEntranceRotation = remember { Animatable(-8f) }

    val logoAlpha = remember { Animatable(0f) }
    val logoOffsetY = remember { Animatable(10f) }
    val taglineAlpha = remember { Animatable(0f) }

    // ---------- Colors ----------
    val isDarkMode = com.paridhi.onemoment.ui.theme.LocalIsDarkMode.current
    val backgroundIvory = if (isDarkMode) MaterialTheme.colorScheme.background else Color(0xFFFBF8F3)
    val warmGlow = if (isDarkMode) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF3E9D8)
    val charcoalText = MaterialTheme.colorScheme.onBackground

    LaunchedEffect(Unit) {
        // Leaf gently falls into place like it's caught a small gust
        launch {
            leafAlpha.animateTo(1f, tween(900, easing = EaseOutCubic))
        }
        launch {
            leafScale.animateTo(1f, tween(900, easing = EaseOutCubic))
        }
        launch {
            leafDropOffsetY.animateTo(0f, tween(900, easing = EaseOutQuad))
        }
        launch {
            leafEntranceRotation.animateTo(0f, tween(900, easing = EaseInOutSine))
        }

        delay(500)
        launch {
            logoAlpha.animateTo(1f, tween(800, easing = EaseOutCubic))
        }
        launch {
            logoOffsetY.animateTo(0f, tween(800, easing = EaseOutCubic))
        }

        delay(350)
        launch {
            taglineAlpha.animateTo(1f, tween(800, easing = EaseOutCubic))
        }

        // Final pause so the breeze-loop is visible before leaving
        delay(1600)
        onSplashFinished()
    }

    // ---------- Infinite "floating in mild air" motion ----------
    val breeze = rememberInfiniteTransition(label = "breeze")

    // Slow rotational sway, like a leaf tilting side to side in wind
    val sway by breeze.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    // Gentle vertical bob, as if lifted and lowered by soft air currents
    val bobY by breeze.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobY"
    )

    // Slight horizontal drift, offset in timing from the bob so movement feels organic, not synced
    val driftX by breeze.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftX"
    )

    // Subtle scale "breathing" — mimics the leaf catching slightly more/less air
    val breathScale by breeze.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    // Shadow beneath the leaf pulses subtly opposite the bob, as if rising/falling from the surface
    val shadowScale by breeze.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadowScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(warmGlow, backgroundIvory),
                    center = Offset.Unspecified,
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Soft shadow beneath the leaf, grounds it so the float reads as "in the air"
            Box(
                modifier = Modifier
                    .offset(y = 58.dp)
                    .size(46.dp, 10.dp)
                    .scale(shadowScale)
                    .alpha(leafAlpha.value * 0.35f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                if (isDarkMode) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f) else Color(0x33362A1D),
                                Color.Transparent
                            )
                        )
                    )
            )

            Image(
                painter = painterResource(id = R.drawable.ic_leaf_natural),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .offset(
                        x = driftX.dp,
                        y = (leafDropOffsetY.value + bobY).dp
                    )
                    .scale(leafScale.value * breathScale)
                    .alpha(leafAlpha.value)
                    .rotate(leafEntranceRotation.value + sway)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "One Moment",
                style = MaterialTheme.typography.displayMedium.copy(
                    letterSpacing = 1.5.sp,
                    fontSize = 32.sp
                ),
                fontWeight = FontWeight.W300,
                color = charcoalText,
                modifier = Modifier
                    .offset(y = logoOffsetY.value.dp)
                    .alpha(logoAlpha.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Keep what matters.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    letterSpacing = 0.5.sp
                ),
                color = charcoalText.copy(alpha = 0.6f),
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }
    }
}
