package com.dengin.files.fileRoom.data.repository

import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileRoom.domain.dao.FileInfoDao
import com.dengin.files.fileRoom.domain.entity.FileInfoDbEntity
import com.dengin.files.fileRoom.domain.mapper.toFileInfoDbEntity
import javax.inject.Inject

class LastModifiedFileRepositoryImpl @Inject constructor(
    private val fileInfoDao: FileInfoDao
) : LastModifiedFileRepository {

    override suspend fun createFileInfo(fileInfoItem: FileInfoItem) {
        fileInfoDao.createFileInfo(fileInfoItem.toFileInfoDbEntity())
    }

    override suspend fun getFilesByPath(path: String): List<FileInfoDbEntity> {
        return fileInfoDao.getFilesByPath(path)
    }
}
