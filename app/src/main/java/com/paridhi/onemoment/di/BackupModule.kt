package com.paridhi.onemoment.di

import android.content.Context
import com.paridhi.onemoment.backup.BackupManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupModule {
    @Provides
    @Singleton
    fun provideBackupManager(@ApplicationContext context: Context): BackupManager {
        return BackupManager(context)
    }
}
