package com.dengin.files.utils.stringExt

import android.os.Build
import android.os.Bundle
import com.dengin.files.fileFilter.domain.entity.SelectedFilter

fun String.getParcelableArgumentByKey(arguments: Bundle?): SelectedFilter? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(this, SelectedFilter::class.java)
    } else {
        arguments?.getParcelable(this)
    }
}
