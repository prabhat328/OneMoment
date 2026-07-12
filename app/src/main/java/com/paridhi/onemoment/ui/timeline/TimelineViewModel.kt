package com.paridhi.onemoment.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paridhi.onemoment.data.MemoryRepository
import com.paridhi.onemoment.data.local.MemoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    repository: MemoryRepository
) : ViewModel() {

    val memories: StateFlow<List<MemoryEntity>> = repository.getAllMemories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
