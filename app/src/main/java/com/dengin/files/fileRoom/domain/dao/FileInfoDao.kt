package com.dengin.files.fileRoom.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dengin.files.fileRoom.domain.entity.FileInfoDbEntity

@Dao
interface FileInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createFileInfo(fileInfoDbEntity: FileInfoDbEntity)

    @Query("SELECT * FROM files WHERE path = :path")
    suspend fun getFilesByPath(path: String): List<FileInfoDbEntity>
}
