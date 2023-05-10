package com.dengin.files.fileRoom.domain.mapper

import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileRoom.domain.entity.FileInfoDbEntity

fun FileInfoItem.toFileInfoDbEntity() = FileInfoDbEntity(
    fileAbsolutePath = absolutePath,
    path = path,
    fileHashCode = hashCode
)
