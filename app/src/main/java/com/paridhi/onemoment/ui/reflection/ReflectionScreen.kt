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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paridhi.onemoment.data.DummyMemoryRepository
import com.paridhi.onemoment.ui.reflection.components.ReflectionMemoryCard
import com.paridhi.onemoment.ui.reflection.components.ReflectionQuoteCard

@Composable
fun ReflectionScreen() {
    val scrollState = rememberScrollState()
    val randomMemory = remember { DummyMemoryRepository.getMemories().random() }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

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
                text = "Today remembered you.",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Text(
                text = "Sunday • 13 July",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ReflectionMemoryCard(memory = randomMemory)

            Spacer(modifier = Modifier.height(32.dp))

            ReflectionQuoteCard(
                quote = "The best thing about a picture is that it never changes, even when the people in it do.",
                author = "Andy Warhol"
            )
        }
    }
}
