package com.dengin.files.fileList.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dengin.files.databinding.FileItemBinding
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.entity.FileType

class FileListAdapter(
    private val openFolder: (String, FileType) -> Unit
) : ListAdapter<FileInfoItem, FileListViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<FileInfoItem>() {

        override fun areItemsTheSame(oldContact: FileInfoItem, newContact: FileInfoItem) =
            oldContact.absolutePath == newContact.absolutePath

        override fun areContentsTheSame(oldContact: FileInfoItem, newContact: FileInfoItem) =
            oldContact == newContact
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListViewHolder {
        val binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileListViewHolder(binding, openFolder)
    }

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) {
        holder.bind(file = currentList[position])
    }
}
