package com.paridhi.onemoment.data

import com.paridhi.onemoment.data.local.MemoryDao
import com.paridhi.onemoment.data.local.MemoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomMemoryRepository @Inject constructor(
    private val memoryDao: MemoryDao
) : MemoryRepository {

    override suspend fun saveMemory(
        memoryText: String,
        photoUri: String?,
        date: String,
        time: String,
        timestamp: Long
    ) {
        val memory = MemoryEntity(
            memoryText = memoryText,
            photoUri = photoUri,
            createdDate = date,
            createdTime = time,
            createdTimestamp = timestamp
        )
        memoryDao.insertMemory(memory)
    }

    override fun getAllMemories(): Flow<List<MemoryEntity>> {
        return memoryDao.getAllMemories()
    }

    override suspend fun getMemoryById(id: Long): MemoryEntity? {
        return memoryDao.getMemoryById(id)
    }

    override suspend fun deleteMemory(id: Long) {
        memoryDao.deleteMemory(id)
    }

    override suspend fun getRandomReflectionMemory(): MemoryEntity? {
        val allMemories = memoryDao.getAllMemories().first()
        if (allMemories.isEmpty()) return null

        // Exclude today's memory based on a simple date string match
        // A better approach would be to calculate the start of the current day using timestamps
        val currentTime = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000

        val pastMemories = allMemories.filter {
            (currentTime - it.createdTimestamp) > oneDayMillis
        }

        if (pastMemories.isEmpty()) return null
        return pastMemories.random()
    }
}
