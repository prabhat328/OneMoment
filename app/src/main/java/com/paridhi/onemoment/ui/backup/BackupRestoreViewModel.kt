package com.paridhi.onemoment.ui.backup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paridhi.onemoment.backup.BackupManager
import com.paridhi.onemoment.backup.BackupPayload
import com.paridhi.onemoment.data.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BackupRestoreState {
    object Idle : BackupRestoreState()
    object Loading : BackupRestoreState()
    data class Success(val message: String) : BackupRestoreState()
    data class Error(val message: String) : BackupRestoreState()
}

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BackupRestoreState>(BackupRestoreState.Idle)
    val uiState: StateFlow<BackupRestoreState> = _uiState.asStateFlow()

    private val _selectedRestoreUri = MutableStateFlow<Uri?>(null)
    val selectedRestoreUri: StateFlow<Uri?> = _selectedRestoreUri.asStateFlow()

    private val _backupMetadata = MutableStateFlow<BackupPayload?>(null)
    val backupMetadata: StateFlow<BackupPayload?> = _backupMetadata.asStateFlow()

    fun resetState() {
        _uiState.value = BackupRestoreState.Idle
    }

    fun clearSelection() {
        _selectedRestoreUri.value = null
        _backupMetadata.value = null
    }

    fun backupToFile(uri: Uri, password: CharArray) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreState.Loading
            try {
                val memories = memoryRepository.getAllMemories().first()
                val result = backupManager.createBackup(uri, memories, password)
                if (result.isSuccess) {
                    _uiState.value = BackupRestoreState.Success("Backup created successfully")
                } else {
                    _uiState.value = BackupRestoreState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = BackupRestoreState.Error(e.message ?: "Failed to backup")
            }
        }
    }

    suspend fun verifyPasswordAndGetDecryptedMemories(password: CharArray): Result<List<com.paridhi.onemoment.data.local.MemoryEntity>> {
        val uri = _selectedRestoreUri.value ?: return Result.failure(Exception("No URI selected"))
        _uiState.value = BackupRestoreState.Loading
        return backupManager.restoreBackup(uri, password)
    }

    fun saveRestoredMemories(restoredMemories: List<com.paridhi.onemoment.data.local.MemoryEntity>, replaceExisting: Boolean) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreState.Loading
            try {
                if (replaceExisting) {
                    memoryRepository.deleteAllMemories()
                }
                memoryRepository.saveMemories(restoredMemories)
                _uiState.value = BackupRestoreState.Success("Restored ${restoredMemories.size} memories successfully")
                clearSelection()
            } catch (e: Exception) {
                _uiState.value = BackupRestoreState.Error("Failed to save restored data: ${e.message}")
            }
        }
    }

    fun setDecryptionError() {
        _uiState.value = BackupRestoreState.Error("Failed to decrypt backup. Incorrect password?")
    }

    fun verifyBackupFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreState.Loading
            val result = backupManager.readBackupMetadata(uri)
            if (result.isSuccess) {
                _backupMetadata.value = result.getOrNull()
                _selectedRestoreUri.value = uri
                _uiState.value = BackupRestoreState.Idle
            } else {
                _uiState.value = BackupRestoreState.Error("Invalid or corrupted backup file.")
            }
        }
    }

    fun restoreFromFile(password: CharArray, replaceExisting: Boolean) {
        val uri = _selectedRestoreUri.value ?: return
        viewModelScope.launch {
            _uiState.value = BackupRestoreState.Loading
            val result = backupManager.restoreBackup(uri, password)
            if (result.isSuccess) {
                try {
                    val restoredMemories = result.getOrNull() ?: emptyList()
                    if (replaceExisting) {
                        memoryRepository.deleteAllMemories()
                    }
                    memoryRepository.saveMemories(restoredMemories)

                    _uiState.value = BackupRestoreState.Success("Restored ${restoredMemories.size} memories successfully")
                    clearSelection()
                } catch (e: Exception) {
                    _uiState.value = BackupRestoreState.Error("Failed to save restored data: ${e.message}")
                }
            } else {
                _uiState.value = BackupRestoreState.Error("Failed to decrypt backup. Incorrect password?")
            }
        }
    }

    suspend fun getMemoriesCount(): Int {
        return memoryRepository.getMemoriesCount()
    }
}
