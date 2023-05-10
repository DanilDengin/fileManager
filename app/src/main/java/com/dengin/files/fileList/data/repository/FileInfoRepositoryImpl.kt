package com.dengin.files.fileList.data.repository

import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.mapper.FileToDomainMapper
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileInfoRepositoryImpl @Inject constructor(
    private val fileMapper: FileToDomainMapper
) : FileInfoRepository {

    override suspend fun getFileList(path: String): List<FileInfoItem>? {
        return withContext(Dispatchers.Default) {
            val directory = File(path)
            directory.listFiles()?.map { file ->
                fileMapper.mapFileToFileInfoItem(file, path)
            }?.sortedBy { filterList ->
                filterList.name
            }
        }
    }
}
