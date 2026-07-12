package com.paridhi.onemoment.data

import com.paridhi.onemoment.data.local.MemoryEntity
import kotlinx.coroutines.flow.Flow

interface MemoryRepository {
    suspend fun saveMemory(memoryText: String, photoUri: String?, date: String, time: String, timestamp: Long)
    suspend fun saveMemories(memories: List<MemoryEntity>)
    fun getAllMemories(): Flow<List<MemoryEntity>>
    suspend fun getMemoryById(id: Long): MemoryEntity?
    suspend fun deleteMemory(id: Long)
    suspend fun deleteAllMemories()
    suspend fun getRandomReflectionMemory(): MemoryEntity?
    suspend fun getMemoriesCount(): Int
}
