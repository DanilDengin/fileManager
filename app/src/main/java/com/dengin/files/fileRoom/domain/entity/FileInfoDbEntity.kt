package com.dengin.files.fileRoom.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
class FileInfoDbEntity(

    @PrimaryKey
    val fileAbsolutePath: String,

    @ColumnInfo(name = "path")
    val path: String,

    @ColumnInfo(name = "fileHashCode")
    val fileHashCode: String
)
