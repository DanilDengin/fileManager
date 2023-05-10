package com.dengin.files.fileList.domain.mapper

import com.dengin.files.R
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.entity.FileType
import com.dengin.files.utils.delegate.unsafeLazy
import com.dengin.files.utils.stringResources.StringResources
import com.dengin.files.utils.view.parseToText
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.StringJoiner
import javax.inject.Inject
import kotlin.math.pow

class FileToDomainMapper @Inject constructor(
    private val stringResources: StringResources
) {

    private val fileSizeBText: String by unsafeLazy {
        stringResources.getString(R.string.file_size_b_text_view)
    }

    private val fileSizeKbText: String by unsafeLazy {
        stringResources.getString(R.string.file_size_Kb_text_view)
    }

    private val fileSizeMbText: String by unsafeLazy {
        stringResources.getString(R.string.file_size_Mb_text_view)
    }

    private val fileSizeGbText: String by unsafeLazy {
        stringResources.getString(R.string.file_size_Gb_text_view)
    }

    private val kbFileSize by unsafeLazy { TWO.pow(TEN_POW) }

    private val mbFileSize by unsafeLazy { TWO.pow(TWENTY_POW) }

    private val gbFileSize by unsafeLazy { TWO.pow(THIRTY_POW) }

    fun mapFileToFileInfoItem(file: File, path: String): FileInfoItem {
        return FileInfoItem(
            name = file.name,
            size = file.length(),
            sizeText = calculateSize(file),
            creationDate = getCreationDate(file),
            creationDateText = getCreationDateText(file),
            type = determineType(file),
            absolutePath = file.absolutePath,
            path = path,
            hashCode = calculateHashCode(file)
        )
    }

    private fun calculateHashCode(file: File): String {
        return if (!file.isDirectory) {
            val data = Files.readAllBytes(Paths.get(file.absolutePath))
            val hash = MessageDigest.getInstance(MD_FIVE_ALGORITHM).digest(data)
            BigInteger(SIGNUM, hash).toString(RADIX)
        } else {
            HASH_CODE_FOLDER
        }
    }

    private fun calculateSize(file: File): String {
        return if (file.isDirectory) {
            file.listFiles()?.size?.let { items ->
                stringResources.getQuantityString(
                    R.plurals.file_size_text_view,
                    items,
                    items
                )
            }.orEmpty()
        } else {
            val fileSize = file.length().toDouble()
            val fileSizeKb by unsafeLazy { fileSize / TWO_TO_TENTH_POWER }
            val fileSizeMb by unsafeLazy { fileSizeKb / TWO_TO_TENTH_POWER }
            val fileSizeGb by unsafeLazy { fileSizeMb / TWO_TO_TENTH_POWER }
            when {
                fileSize < kbFileSize -> {
                    fileSize.parseToText(fileSizeBText)
                }
                fileSize < mbFileSize -> {
                    fileSizeKb.parseToText(fileSizeKbText)
                }
                fileSize < gbFileSize -> {
                    fileSizeMb.parseToText(fileSizeMbText)
                }
                else -> {
                    fileSizeGb.parseToText(fileSizeGbText)
                }
            }
        }
    }

    private fun determineType(file: File): FileType {
        return if (file.isDirectory) {
            FileType.FOLDER
        } else {
            when (file.extension) {
                PDF_TYPE -> FileType.PDF
                PNG_TYPE, JPEG_TYPE, JPG_TYPE -> FileType.IMAGE
                DOCX_TYPE, TXT_TYPE -> FileType.DOCUMENT
                MP4_TYPE, AVI_TYPE -> FileType.VIDEO
                else -> FileType.UNKNOWN_FILE
            }
        }
    }

    private fun getCreationDate(file: File): Long {
        return Files.readAttributes(file.toPath(), BasicFileAttributes::class.java).creationTime()
            .toMillis()
    }

    private fun getCreationDateText(file: File): String {
        val timeInMills = getCreationDate(file)
        val creationDate = GregorianCalendar()
        creationDate.time = Date(timeInMills)
        val calendarDate = StringJoiner(".")
        val timeDate = StringJoiner(":")
        val format = DecimalFormat("00")
        calendarDate.add(format.format(creationDate.get(Calendar.DAY_OF_MONTH)))
            .add(format.format(creationDate.get(Calendar.MONTH) + 1))
            .add(format.format(creationDate.get(Calendar.YEAR)))
            .toString()
        timeDate.add(format.format(creationDate.get(Calendar.HOUR_OF_DAY)))
            .add(format.format(creationDate.get(Calendar.MINUTE)))
        return "$calendarDate $timeDate"
    }

    private companion object {
        const val THIRTY_POW = 30
        const val TWENTY_POW = 20
        const val TEN_POW = 10
        const val TWO = 2.0
        const val MD_FIVE_ALGORITHM = "MD5"
        const val SIGNUM = 1
        const val RADIX = 16
        const val PDF_TYPE = "pdf"
        const val PNG_TYPE = "png"
        const val JPEG_TYPE = "jpeg"
        const val JPG_TYPE = "jpg"
        const val DOCX_TYPE = "docx"
        const val TXT_TYPE = "txt"
        const val MP4_TYPE = "mp4"
        const val AVI_TYPE = "avi"
        const val TWO_TO_TENTH_POWER = 1024
        const val HASH_CODE_FOLDER = "0"
    }
}
