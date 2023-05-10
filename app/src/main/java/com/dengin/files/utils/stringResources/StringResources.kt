package com.dengin.files.utils.stringResources

interface StringResources {

    fun getString(stringId: Int): String

    fun getQuantityString(stringId: Int, quantity: Int, formatArgs: Any): String
}
