package com.dengin.files.fileList.domain.useCase

import com.dengin.files.fileFilter.domain.entity.SelectedFilter
import com.dengin.files.fileList.data.repository.FileInfoRepository
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.entity.FileType
import com.dengin.files.fileRoom.data.repository.LastModifiedFileRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileInfoItemUseCaseImpl @Inject constructor(
    private val fileRepository: FileInfoRepository,
    private val lastModifiedFileRepository: LastModifiedFileRepository
) : FileInfoItemUseCase {

    private var files = emptyList<FileInfoItem>()

    private var filteredList = emptyList<FileInfoItem>()

    private val lastModifiedFiles = mutableListOf<FileInfoItem>()

    override suspend fun getFileList(path: String): List<FileInfoItem>? {
        return files.ifEmpty {
            fileRepository.getFileList(path)?.also { filesList ->
                files = filesList
                filteredList = filesList
            }
        }
    }

    override suspend fun applyFilter(selectedFilter: SelectedFilter): List<FileInfoItem> {
        return withContext(Dispatchers.Default) {
            when (selectedFilter) {
                SelectedFilter.NOTHING -> {
                    filteredList
                }
                SelectedFilter.CREATION_DATE -> {
                    filteredList
                        .sortedBy { files -> files.creationDate }
                        .reversed()
                        .also { actualList -> filteredList = actualList }
                }
                SelectedFilter.DESCENDING_SIZE_FILE -> {
                    filteredList
                        .sortedBy { files -> files.size }
                        .reversed()
                        .also { actualList -> filteredList = actualList }
                }
                SelectedFilter.ASCENDING_SIZE_FILE -> {
                    filteredList
                        .sortedBy { files -> files.size }
                        .also { actualList -> filteredList = actualList }
                }
                SelectedFilter.SHOW_VIDEO -> {
                    filteredList.filter { file ->
                        file.type == FileType.VIDEO
                    }
                }
                SelectedFilter.SHOW_DOCUMENTS -> {
                    filteredList.filter { file ->
                        file.type == FileType.DOCUMENT
                    }
                }
                SelectedFilter.SHOW_IMAGE -> {
                    filteredList.filter { file ->
                        file.type == FileType.IMAGE
                    }
                }
                SelectedFilter.SHOW_PDF -> {
                    filteredList.filter { file ->
                        file.type == FileType.PDF
                    }
                }
                SelectedFilter.RESET -> {
                    files.also { list -> filteredList = list }
                }
            }
        }
    }

    override suspend fun getLastModifiedFilesByPath(path: String): List<FileInfoItem> {
        return lastModifiedFiles.ifEmpty {
            val fileDbList = lastModifiedFileRepository.getFilesByPath(path)
            if (fileDbList.isNotEmpty()) {
                files.forEach { fileInfoItem ->
                    if (fileInfoItem.type != FileType.FOLDER) {
                        var changedFile: FileInfoItem? = null
                        val foundFileDb = fileDbList.find { fileDbEntity ->
                            changedFile = fileInfoItem
                            fileInfoItem.absolutePath == fileDbEntity.fileAbsolutePath
                        }
                        if (foundFileDb == null) {
                            changedFile?.also(lastModifiedFiles::add)
                        } else {
                            val hc = foundFileDb.fileHashCode
                            if (hc != fileInfoItem.hashCode) {
                                changedFile?.also { file -> lastModifiedFiles.add(file) }
                            }
                        }
                    }
                }
            }
            files.forEach { file ->
                if (file.type != FileType.FOLDER) {
                    lastModifiedFileRepository.createFileInfo(file)
                }
            }
            lastModifiedFiles
        }
    }
}
