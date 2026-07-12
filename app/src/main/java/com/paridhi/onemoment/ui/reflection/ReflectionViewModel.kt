package com.paridhi.onemoment.ui.reflection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paridhi.onemoment.data.MemoryRepository
import com.paridhi.onemoment.data.local.MemoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReflectionViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _randomMemory = MutableStateFlow<MemoryEntity?>(null)
    val randomMemory: StateFlow<MemoryEntity?> = _randomMemory.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> = _quote.asStateFlow()

    private val titles = listOf(
        "Today remembered you.",
        "Remember this day?",
        "A quiet memory returned.",
        "Some moments deserve another smile.",
        "One of your beautiful days.",
        "A moment found its way back."
    )

    private val quotes = listOf(
        "The little moments become the big memories.",
        "Life is made of ordinary days remembered well.",
        "Keep what matters.",
        "Sometimes yesterday gives strength to today.",
        "Memories are proof that beautiful moments happened."
    )

    init {
        loadRandomMemory()
    }

    private fun loadRandomMemory() {
        viewModelScope.launch {
            val memory = repository.getRandomReflectionMemory()
            _randomMemory.value = memory
            if (memory != null) {
                _title.value = titles.random()
                _quote.value = quotes.random()
            }
        }
    }
}
