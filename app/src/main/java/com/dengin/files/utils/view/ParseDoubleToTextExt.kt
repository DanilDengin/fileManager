package com.dengin.files.utils.view

fun Double.parseToText(format: String): String {
    return String.format(format, String.format("%.1f", this))
}
