package com.dengin.files.fileList.presentation

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dengin.files.R
import com.dengin.files.fileFilter.domain.entity.SelectedFilter
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.FIRST_SELECTED_FILTER_ARG
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.SECOND_SELECTED_FILTER_ARG
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.useCase.FileInfoItemUseCase
import com.dengin.files.fileList.presentation.FileListFragment.Companion.FILE_PATH_ARG
import com.dengin.files.utils.liveData.SingleLiveEvent
import com.dengin.files.utils.stringResources.StringResources
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FileListViewModel @Inject constructor(
    private val fileListUseCase: FileInfoItemUseCase,
    private val stringResources: StringResources
) : ViewModel() {

    val fileList: LiveData<List<FileInfoItem>?> get() = _fileList
    val exceptionText: LiveData<String> get() = _exceptionText
    val filterList =
        ArrayList<SelectedFilter>(listOf(SelectedFilter.NOTHING, SelectedFilter.NOTHING))
    private val _exceptionText = SingleLiveEvent<String>()
    private val _fileList = MutableLiveData<List<FileInfoItem>?>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(FILE_LIST_VIEW_MODEL_TAG, throwable.toString())
        _exceptionText.value = stringResources.getString(R.string.exception_toast)
    }

    fun getFolderContent(path: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _fileList.value = fileListUseCase.getFileList(path)
            fileListUseCase.getLastModifiedFilesByPath(path)
        }
    }

    fun openFolder(navController: NavController, path: String) {
        navController.navigate(
            R.id.action_fileListFragment_to_fileListFragment,
            bundleOf(FILE_PATH_ARG to "$path/")
        )
    }

    fun navigateToFileFilter(navController: NavController) {
        navController.navigate(
            R.id.action_fileListFragment_to_fileFilterFragment,
            bundleOf(
                FIRST_SELECTED_FILTER_ARG to filterList[0],
                SECOND_SELECTED_FILTER_ARG to filterList[1]
            )
        )
    }

    fun applyFilter() {
        viewModelScope.launch {
            var filteredListFile = emptyList<FileInfoItem>()
            filterList.forEach { filter ->
                filteredListFile = fileListUseCase.applyFilter(filter)
            }
            _fileList.value = filteredListFile
        }
    }

    fun getLastModifiedFilesByPath(path: String) {
        viewModelScope.launch {
            _fileList.value = fileListUseCase.getLastModifiedFilesByPath(path)
        }
    }

    private companion object {
        val FILE_LIST_VIEW_MODEL_TAG: String = FileListViewModel::class.java.name
    }
}
