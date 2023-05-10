package com.dengin.files.fileList.domain.useCase

import com.dengin.files.fileFilter.domain.entity.SelectedFilter
import com.dengin.files.fileList.domain.entity.FileInfoItem

interface FileInfoItemUseCase {

    suspend fun getFileList(path: String): List<FileInfoItem>?

    suspend fun applyFilter(
        selectedFilter: SelectedFilter,
        fileInfoItemList: List<FileInfoItem>
    ): List<FileInfoItem>

    suspend fun getLastModifiedFilesByPath(path: String): List<FileInfoItem>
}
