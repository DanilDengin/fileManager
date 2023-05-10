package com.dengin.files.utils.delegate

fun <T> unsafeLazy(initializer: () -> T): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = initializer)
