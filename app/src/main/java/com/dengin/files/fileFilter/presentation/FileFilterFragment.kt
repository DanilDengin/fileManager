package com.dengin.files.fileFilter.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dengin.files.R
import com.dengin.files.databinding.FragmentFileFilterBinding
import com.dengin.files.fileFilter.domain.entity.SelectedFilter
import com.dengin.files.utils.delegate.unsafeLazy
import com.dengin.files.utils.stringExt.getParcelableArgumentByKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FileFilterFragment : BottomSheetDialogFragment(R.layout.fragment_file_filter) {

    private val viewModel: FileFilterViewModel by viewModels()

    private val binding by viewBinding(FragmentFileFilterBinding::bind)

    private val firstFilter by unsafeLazy {
        FIRST_SELECTED_FILTER_ARG.getParcelableArgumentByKey(
            arguments
        )
    }

    private val secondFilter by unsafeLazy {
        SECOND_SELECTED_FILTER_ARG.getParcelableArgumentByKey(
            arguments
        )
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSelectedFilter()
        initRadioButtonListener()
        initApplyButtonListener()
        initResetListener()
    }

    private fun checkSelectedFilter() {
        firstFilter?.also(::setCheckedRadioButton)
        secondFilter?.also(::setCheckedRadioButton)
    }

    private fun setCheckedRadioButton(filter: SelectedFilter) {
        when (filter) {
            SelectedFilter.RESET -> {}
            SelectedFilter.NOTHING -> {}
            SelectedFilter.DESCENDING_SIZE_FILE -> {
                binding.radioButtonDescendingSizeFile.isChecked = true
                selectFilter(SelectedFilter.DESCENDING_SIZE_FILE, FIRST_FILTER_INDEX)
            }
            SelectedFilter.ASCENDING_SIZE_FILE -> {
                binding.radioButtonAscendingSizeFile.isChecked = true
                selectFilter(SelectedFilter.ASCENDING_SIZE_FILE, FIRST_FILTER_INDEX)
            }
            SelectedFilter.CREATION_DATE -> {
                binding.radioButtonCreationDate.isChecked = true
                selectFilter(SelectedFilter.CREATION_DATE, FIRST_FILTER_INDEX)
            }
            SelectedFilter.SHOW_VIDEO -> {
                binding.radioButtonVideoType.isChecked = true
                selectFilter(SelectedFilter.SHOW_VIDEO, SECOND_FILTER_INDEX)
            }
            SelectedFilter.SHOW_DOCUMENTS -> {
                binding.radioButtonDocumentType.isChecked = true
                selectFilter(SelectedFilter.SHOW_DOCUMENTS, SECOND_FILTER_INDEX)
            }
            SelectedFilter.SHOW_IMAGE -> {
                binding.radioButtonImageType.isChecked = true
                selectFilter(SelectedFilter.SHOW_IMAGE, SECOND_FILTER_INDEX)
            }
            SelectedFilter.SHOW_PDF -> {
                binding.radioButtonPdfType.isChecked = true
                selectFilter(SelectedFilter.SHOW_PDF, SECOND_FILTER_INDEX)
            }
        }
    }

    private fun initResetListener() {
        binding.resetTextView.setOnClickListener {
            setFragmentResult(
                parentFragmentManager,
                SelectedFilter.RESET,
                SelectedFilter.RESET
            )
            viewModel.applyFilter(findNavController())
        }
    }

    private fun initApplyButtonListener() {
        binding.applyButton.setOnClickListener {
            setFragmentResult(
                parentFragmentManager,
                viewModel.filterList[FIRST_FILTER_INDEX],
                viewModel.filterList[SECOND_FILTER_INDEX]
            )
            viewModel.applyFilter(findNavController())
        }
    }

    private fun setFragmentResult(
        fragmentManager: FragmentManager,
        firstSelectedFilter: SelectedFilter,
        secondSelectedFilter: SelectedFilter
    ) {
        fragmentManager.setFragmentResult(
            FILTER_KEY,
            bundleOf(
                FIRST_FILTER_BUNDLE_KEY to firstSelectedFilter,
                SECOND_FILTER_BUNDLE_KEY to secondSelectedFilter
            )
        )
    }

    private fun initRadioButtonListener() {
        with(binding) {
            radioGroupFileFilter.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonCreationDate -> {
                        selectFilter(SelectedFilter.CREATION_DATE, FIRST_FILTER_INDEX)
                    }
                    R.id.radioButtonAscendingSizeFile -> {
                        selectFilter(SelectedFilter.ASCENDING_SIZE_FILE, FIRST_FILTER_INDEX)
                    }
                    R.id.radioButtonDescendingSizeFile -> {
                        selectFilter(SelectedFilter.DESCENDING_SIZE_FILE, FIRST_FILTER_INDEX)
                    }
                }
            }
            radioGroupFileTypeFilter.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonVideoType -> {
                        selectFilter(SelectedFilter.SHOW_VIDEO, SECOND_FILTER_INDEX)
                    }
                    R.id.radioButtonDocumentType -> {
                        selectFilter(SelectedFilter.SHOW_DOCUMENTS, SECOND_FILTER_INDEX)
                    }
                    R.id.radioButtonPdfType -> {
                        selectFilter(SelectedFilter.SHOW_PDF, SECOND_FILTER_INDEX)
                    }
                    R.id.radioButtonImageType -> {
                        selectFilter(SelectedFilter.SHOW_IMAGE, SECOND_FILTER_INDEX)
                    }
                }
            }
        }
    }

    private fun selectFilter(selectedFilter: SelectedFilter, filterNumber: Int) {
        binding.applyButton.isEnabled = true
        viewModel.filterList[filterNumber] = selectedFilter
    }

    companion object {
        const val FILTER_KEY = "filterKey"
        const val FIRST_FILTER_INDEX = 0
        const val SECOND_FILTER_INDEX = 1
        const val FIRST_FILTER_BUNDLE_KEY = "firstFilterBundleKey"
        const val SECOND_FILTER_BUNDLE_KEY = "secondFilterBundleKey"
        const val FIRST_SELECTED_FILTER_ARG = "firstSelectedFilter"
        const val SECOND_SELECTED_FILTER_ARG = "secondSelectedFilter"
    }
}
