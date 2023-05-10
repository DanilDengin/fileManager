package com.dengin.files.fileRoom.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dengin.files.fileRoom.domain.dao.FileInfoDao
import com.dengin.files.fileRoom.domain.entity.FileInfoDbEntity

@Database(
    version = 1,
    entities = [FileInfoDbEntity::class],
    exportSchema = false
)
abstract class FileInfoDatabase : RoomDatabase() {

    abstract fun getFileInfoDao(): FileInfoDao
}
