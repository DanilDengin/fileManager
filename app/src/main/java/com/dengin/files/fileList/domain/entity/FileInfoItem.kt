package com.dengin.files.fileList.domain.entity

data class FileInfoItem(
    val name: String,
    val size: Long,
    val sizeText: String,
    val creationDate: Long,
    val creationDateText: String,
    val type: FileType,
    val absolutePath: String,
    val path: String,
    val hashCode: String
)
