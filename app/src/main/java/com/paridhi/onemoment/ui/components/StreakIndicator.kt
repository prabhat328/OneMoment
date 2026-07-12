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
        repeat(totalDots) { index ->
            val isFilled = index < streakCount
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(7.dp)
                    .background(
                        color = if (isFilled)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
        }
        Text(
            text = "  $streakCount-day streak",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}