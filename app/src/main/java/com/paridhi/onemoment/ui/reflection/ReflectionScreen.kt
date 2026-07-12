package com.paridhi.onemoment.ui.reflection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paridhi.onemoment.ui.reflection.components.ReflectionMemoryCard
import com.paridhi.onemoment.ui.reflection.components.ReflectionQuoteCard
import com.paridhi.onemoment.ui.reflection.components.ReflectionTextCard

@Composable
fun ReflectionScreen(
    viewModel: ReflectionViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val randomMemory by viewModel.randomMemory.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(randomMemory) {
        if (randomMemory != null) {
            visible = true
        }
    }

    if (randomMemory == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nothing to reflect on yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Save a few memories and come back tomorrow.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    } else {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 32.dp, bottom = 100.dp)
        ) {
            Text(
                text = "REFLECTION",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A memory came back\ntoday.",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 44.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "From July 9, 2026 • Wednesday",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ReflectionMemoryCard(memory = randomMemory!!)

            Spacer(modifier = Modifier.height(16.dp))

            ReflectionTextCard(
                text = randomMemory!!.memoryText
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReflectionQuoteCard(
                quote = "The little moments? The little moments are not little."
            )
        }
        }
    }
}
