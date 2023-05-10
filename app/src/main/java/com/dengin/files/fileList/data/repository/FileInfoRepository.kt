package com.dengin.files.fileList.data.repository

import com.dengin.files.fileList.domain.entity.FileInfoItem

interface FileInfoRepository {

    suspend fun getFileList(path: String): List<FileInfoItem>?
}
