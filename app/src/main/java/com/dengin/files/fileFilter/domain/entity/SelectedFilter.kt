package com.dengin.files.fileFilter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SelectedFilter : Parcelable {
    CREATION_DATE,
    DESCENDING_SIZE_FILE,
    ASCENDING_SIZE_FILE,
    SHOW_VIDEO,
    SHOW_DOCUMENTS,
    SHOW_IMAGE,
    SHOW_PDF,
    NOTHING,
    RESET
}
