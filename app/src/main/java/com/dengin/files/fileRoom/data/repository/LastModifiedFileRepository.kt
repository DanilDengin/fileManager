package com.dengin.files.fileRoom.data.repository

import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileRoom.domain.entity.FileInfoDbEntity

interface LastModifiedFileRepository {

    suspend fun createFileInfo(fileInfoItem: FileInfoItem)

    suspend fun getFilesByPath(path: String): List<FileInfoDbEntity>
}
