package com.paridhi.onemoment.ui.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paridhi.onemoment.data.DummyMemoryRepository
import com.paridhi.onemoment.ui.timeline.components.MemoryCard

@Composable
fun TimelineScreen() {
    val memories = remember { DummyMemoryRepository.getMemories() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Timeline",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(memories) { memory ->
                MemoryCard(memory = memory)
            }
            
            // Extra spacing at the bottom for the floating navigation bar
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
