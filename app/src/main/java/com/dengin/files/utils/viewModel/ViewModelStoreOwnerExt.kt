package com.dengin.files.utils.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(crossinline initializer: () -> T): T {
    return ViewModelProvider(this, vmFactory(initializer)).get(T::class.java)
}

inline fun <VM : ViewModel> vmFactory(crossinline initializer: () -> VM) =
    object : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = initializer() as T
    }
