package com.paridhi.onemoment.di

import android.content.Context
import androidx.room.Room
import com.paridhi.onemoment.data.MemoryRepository
import com.paridhi.onemoment.data.RoomMemoryRepository
import com.paridhi.onemoment.data.local.AppDatabase
import com.paridhi.onemoment.data.local.MemoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "one_moment_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMemoryDao(database: AppDatabase): MemoryDao {
        return database.memoryDao()
    }

    @Provides
    @Singleton
    fun provideMemoryRepository(memoryDao: MemoryDao): MemoryRepository {
        return RoomMemoryRepository(memoryDao)
    }
}
