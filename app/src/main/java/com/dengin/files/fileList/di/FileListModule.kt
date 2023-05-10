package com.dengin.files.fileList.di

import com.dengin.files.fileList.data.repository.FileInfoRepository
import com.dengin.files.fileList.data.repository.FileInfoRepositoryImpl
import com.dengin.files.fileList.domain.useCase.FileInfoItemUseCase
import com.dengin.files.fileList.domain.useCase.FileInfoItemUseCaseImpl
import com.dengin.files.utils.di.FeatureScope
import dagger.Binds
import dagger.Module

@Module
interface FileListModule {

    @FeatureScope
    @Binds
    fun bindFileInfoRepository(fileInfoRepositoryImpl: FileInfoRepositoryImpl): FileInfoRepository

    @FeatureScope
    @Binds
    fun bindFileInfoItemUseCase(fileInfoRepositoryImpl: FileInfoItemUseCaseImpl): FileInfoItemUseCase
}
