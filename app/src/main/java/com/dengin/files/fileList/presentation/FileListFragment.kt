package com.dengin.files.fileList.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dengin.files.R
import com.dengin.files.app.App
import com.dengin.files.databinding.FragmentFileListBinding
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.FILTER_KEY
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.FIRST_FILTER_BUNDLE_KEY
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.FIRST_FILTER_INDEX
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.SECOND_FILTER_BUNDLE_KEY
import com.dengin.files.fileFilter.presentation.FileFilterFragment.Companion.SECOND_FILTER_INDEX
import com.dengin.files.fileList.di.DaggerFileListComponent
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.entity.FileType
import com.dengin.files.fileList.presentation.recyclerView.FileListAdapter
import com.dengin.files.fileList.presentation.recyclerView.FileListItemDecorator
import com.dengin.files.utils.delegate.unsafeLazy
import com.dengin.files.utils.stringExt.getParcelableArgumentByKey
import com.dengin.files.utils.viewModel.viewModel
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject
import javax.inject.Provider

class FileListFragment : Fragment(R.layout.fragment_file_list) {

    @Inject
    lateinit var viewModelProvider: Provider<FileListViewModel>

    private var isReadImagePermissionGranted = false

    private var isReadVideoPermissionGranted = false

    private var isReadAudioPermissionGranted = false

    private var isReadStoragePermissionGranted = false

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private val binding by viewBinding(FragmentFileListBinding::bind)

    private val fileListAdapter: FileListAdapter by unsafeLazy {
        FileListAdapter(::showFileContent)
    }

    private val homeDirectory by unsafeLazy {
        Environment.getExternalStorageDirectory().toString()
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showFileList()
            } else {
                binding.permissionButton.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.require_permission_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isReadImagePermissionGranted =
            permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: isReadImagePermissionGranted
        isReadVideoPermissionGranted =
            permissions[Manifest.permission.READ_MEDIA_VIDEO] ?: isReadVideoPermissionGranted
        isReadAudioPermissionGranted =
            permissions[Manifest.permission.READ_MEDIA_AUDIO] ?: isReadAudioPermissionGranted
        isReadStoragePermissionGranted =
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadStoragePermissionGranted

        if (
            isReadImagePermissionGranted &&
            isReadVideoPermissionGranted &&
            isReadAudioPermissionGranted
        ) {
            showFileList()
        } else {
            binding.permissionButton.visibility = View.VISIBLE
            Toast.makeText(
                requireContext(),
                getString(R.string.require_permission_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val openFileErrorMessage by unsafeLazy {
        getString(R.string.open_file_error_toast)
    }

    private val argument by unsafeLazy { arguments?.getString(FILE_PATH_ARG) }

    override fun onAttach(context: Context) {
        DaggerFileListComponent.factory()
            .create((requireContext().applicationContext as App).appComponent)
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        if (argument != null) {
            viewModel.getFolderContent(requireNotNull(argument))
        } else {
            checkReadStoragePermission()
        }
        initMenu()
        initObservers()
        initFragmentResultListener()
        initListeners()
    }

    private fun initListeners() {
        binding.permissionButton.setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    ALL_FILES_TAB -> viewModel.getFolderContent(argument ?: homeDirectory)
                    LAST_MODIFIED_FILES_TAB -> viewModel.getLastModifiedFilesByPath(
                        argument ?: homeDirectory
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun initObservers() {
        viewModel.fileList.observe(viewLifecycleOwner, ::updateView)
        viewModel.exceptionText.observe(viewLifecycleOwner) { exceptionToast ->
            Toast.makeText(requireContext(), exceptionToast, Toast.LENGTH_SHORT).show()
        }
        viewModel.progressBarState.observe(viewLifecycleOwner) { isVisible ->
            binding.progressBar.isVisible = isVisible
        }
    }

    private fun updateView(files: List<FileInfoItem>?) {
        if (files == null || files.isEmpty()) {
            binding.noFilesTextView.visibility = View.VISIBLE
        } else {
            binding.noFilesTextView.visibility = View.INVISIBLE
        }
        fileListAdapter.submitList(files) {
            binding.fileListRecyclerView.scrollToPosition(RECYCLER_VIEW_START_POSITION)
        }
    }

    private fun showFileContent(path: String, fileType: FileType) {
        var typeOpenedFile: String? = null
        when (fileType) {
            FileType.FOLDER -> viewModel.openFolder(findNavController(), path)
            FileType.DOCUMENT -> typeOpenedFile = "application/msword"
            FileType.PDF -> typeOpenedFile = "application/pdf"
            FileType.IMAGE -> typeOpenedFile = "image/*"
            FileType.VIDEO -> typeOpenedFile = "video/*"
            FileType.UNKNOWN_FILE -> typeOpenedFile = "*/*"
        }
        typeOpenedFile?.also { type ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setDataAndType(Uri.parse(path), type)
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                context?.startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    openFileErrorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.file_list_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == R.id.filterView) {
                        viewModel.navigateToFileFilter(findNavController())
                        return true
                    } else {
                        false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
    }

    private fun initRecyclerView() {
        with(binding.fileListRecyclerView) {
            adapter = fileListAdapter
            addItemDecoration(FileListItemDecorator())
        }
    }

    private fun initFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            FILTER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            binding.tabLayout.getTabAt(ALL_FILES_TAB)?.select()
            val firstFilter = FIRST_FILTER_BUNDLE_KEY.getParcelableArgumentByKey(bundle)
            val secondFilter = SECOND_FILTER_BUNDLE_KEY.getParcelableArgumentByKey(bundle)
            firstFilter?.also { filter ->
                viewModel.filterList[FIRST_FILTER_INDEX] = filter
            }
            secondFilter?.also { filter ->
                viewModel.filterList[SECOND_FILTER_INDEX] = filter
            }
            viewModel.applyFilter()
        }
    }

    private fun showFileList() {
        binding.permissionButton.visibility = View.GONE
        viewModel.getFolderContent(argument ?: homeDirectory)
    }

    private fun checkReadStoragePermission() {
        isReadStoragePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isReadImagePermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
            isReadVideoPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
            isReadAudioPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
            val permissionRequest: MutableList<String> = ArrayList()
            if (
                isReadImagePermissionGranted &&
                isReadVideoPermissionGranted &&
                isReadAudioPermissionGranted &&
                isReadStoragePermissionGranted
            ) {
                showFileList()
            } else {
                if (!isReadImagePermissionGranted) {
                    permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                }
                if (!isReadVideoPermissionGranted) {
                    permissionRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
                }
                if (!isReadAudioPermissionGranted) {
                    permissionRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
                }
                if (!isReadStoragePermissionGranted) {
                    permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                if (permissionRequest.isNotEmpty()) {
                    requestMultiplePermissions.launch(permissionRequest.toTypedArray())
                }
            }
        } else {
            if (isReadStoragePermissionGranted) {
                showFileList()
            } else {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    companion object {
        private const val RECYCLER_VIEW_START_POSITION = 0
        private const val ALL_FILES_TAB = 0
        private const val LAST_MODIFIED_FILES_TAB = 1
        const val FILE_PATH_ARG = "filePathArg"
    }
}
