package com.paridhi.onemoment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memoryText: String,
    val photoUri: String?,
    val createdDate: String,
    val createdTime: String,
    val createdTimestamp: Long
)
