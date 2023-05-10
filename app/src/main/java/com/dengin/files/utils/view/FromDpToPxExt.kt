package com.dengin.files.utils.view

import android.content.res.Resources
import kotlin.math.roundToInt

fun Int.fromDpToPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
