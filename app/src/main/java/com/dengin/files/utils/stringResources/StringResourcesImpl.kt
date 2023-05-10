package com.dengin.files.utils.stringResources

import android.content.Context
import javax.inject.Inject

class StringResourcesImpl @Inject constructor(
    private val context: Context
) : StringResources {

    override fun getString(stringId: Int): String {
        return context.getString(stringId)
    }

    override fun getQuantityString(stringId: Int, quantity: Int, formatArgs: Any): String {
        return context.resources.getQuantityString(stringId, quantity, formatArgs)
    }
}
