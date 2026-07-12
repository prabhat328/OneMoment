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

    init {
        loadRandomMemory()
    }

    private fun loadRandomMemory() {
        viewModelScope.launch {
            _randomMemory.value = repository.getRandomReflectionMemory()
        }
    }
}
