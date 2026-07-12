package com.paridhi.onemoment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StreakIndicator(streakCount: Int, totalDots: Int = 7) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val pastStreakColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)

        repeat(totalDots) { index ->
            val color = when {
                index < streakCount - 1 -> pastStreakColor
                index == streakCount - 1 -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
            }

            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(7.dp)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
            )
        }
        Text(
            text = "  $streakCount-day streak",
            style = MaterialTheme.typography.labelMedium,
            color = pastStreakColor
        )
    }
}