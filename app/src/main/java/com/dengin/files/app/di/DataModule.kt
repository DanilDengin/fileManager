package com.dengin.files.app.di

import android.content.Context
import androidx.room.Room
import com.dengin.files.fileRoom.data.database.FileInfoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Singleton
    @Provides
    fun provideContactMapDatabase(context: Context): FileInfoDatabase {
        return Room.databaseBuilder(
            context,
            FileInfoDatabase::class.java,
            FILE_INFO_DATABASE_NAME
        ).build()
    }

    private const val FILE_INFO_DATABASE_NAME = "lastModifiedFiles"
}
