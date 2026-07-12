package com.paridhi.onemoment.ui.home

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paridhi.onemoment.data.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: MemoryRepository
) : AndroidViewModel(application) {

    private val _memoryText = MutableStateFlow("")
    val memoryText: StateFlow<String> = _memoryText.asStateFlow()

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    private val _saveStatus = MutableStateFlow<SaveStatus>(SaveStatus.Idle)
    val saveStatus: StateFlow<SaveStatus> = _saveStatus.asStateFlow()

    fun updateMemoryText(text: String) {
        _memoryText.value = text
        if (_saveStatus.value is SaveStatus.Success || _saveStatus.value is SaveStatus.Error) {
            _saveStatus.value = SaveStatus.Idle
        }
    }

    fun updatePhotoUri(uri: Uri?) {
        uri?.let {
            // Take persistable URI permission so we can read this image across reboots
            try {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                getApplication<Application>().contentResolver.takePersistableUriPermission(it, flag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _photoUri.value = uri
    }

    fun removePhoto() {
        _photoUri.value = null
    }

    fun saveMemory() {
        val text = _memoryText.value.trim()
        if (text.isBlank()) {
            _saveStatus.value = SaveStatus.Error("Memory text cannot be empty")
            return
        }

        _saveStatus.value = SaveStatus.Saving

        viewModelScope.launch {
            try {
                val timestamp = System.currentTimeMillis()
                val date = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date(timestamp))
                val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))

                repository.saveMemory(
                    memoryText = text,
                    photoUri = _photoUri.value?.toString(),
                    date = date,
                    time = time,
                    timestamp = timestamp
                )

                // Clear form on success
                _memoryText.value = ""
                _photoUri.value = null
                _saveStatus.value = SaveStatus.Success
            } catch (e: Exception) {
                _saveStatus.value = SaveStatus.Error(e.message ?: "Failed to save memory")
            }
        }
    }

    fun resetStatus() {
        _saveStatus.value = SaveStatus.Idle
    }
}

sealed class SaveStatus {
    object Idle : SaveStatus()
    object Saving : SaveStatus()
    object Success : SaveStatus()
    data class Error(val message: String) : SaveStatus()
}
