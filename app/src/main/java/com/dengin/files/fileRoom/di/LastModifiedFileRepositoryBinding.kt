package com.dengin.files.fileRoom.di

import com.dengin.files.fileRoom.data.database.FileInfoDatabase
import com.dengin.files.fileRoom.data.repository.LastModifiedFileRepository
import com.dengin.files.fileRoom.data.repository.LastModifiedFileRepositoryImpl
import com.dengin.files.fileRoom.domain.dao.FileInfoDao
import com.dengin.files.utils.di.FeatureScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface LastModifiedFileRepositoryBinding {

    @Binds
    @FeatureScope
    fun bindLastModifiedFileRepository(
        lastModifiedFileRepositoryImpl: LastModifiedFileRepositoryImpl
    ): LastModifiedFileRepository

    companion object {
        @FeatureScope
        @Provides
        fun provideContactMapDao(fileInfoDatabase: FileInfoDatabase): FileInfoDao {
            return fileInfoDatabase.getFileInfoDao()
        }
    }
}
