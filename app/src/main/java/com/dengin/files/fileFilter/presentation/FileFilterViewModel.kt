package com.dengin.files.fileFilter.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.dengin.files.fileFilter.domain.entity.SelectedFilter

class FileFilterViewModel : ViewModel() {

    val filterList =
        ArrayList<SelectedFilter>(listOf(SelectedFilter.NOTHING, SelectedFilter.NOTHING))

    fun applyFilter(navController: NavController) {
        navController.navigateUp()
    }
}
