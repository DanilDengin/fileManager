package com.dengin.files.fileList.presentation.recyclerView

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.dengin.files.R
import com.dengin.files.databinding.FileItemBinding
import com.dengin.files.fileList.domain.entity.FileInfoItem
import com.dengin.files.fileList.domain.entity.FileType
import com.dengin.files.utils.delegate.unsafeLazy

class FileListViewHolder(
    private val binding: FileItemBinding,
    private val openFolder: (String, FileType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var fileType: FileType? = null

    private var absolutePath: String? = null

    private val videoImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_video
        )
    }

    private val folderImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_folder
        )
    }

    private val documentImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_document
        )
    }

    private val pdfImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_pdf
        )
    }

    private val pictureImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_image
        )
    }

    private val unknownFileImage by unsafeLazy {
        AppCompatResources.getDrawable(
            itemView.context,
            R.drawable.ic_unknown_file
        )
    }

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && fileType != null && absolutePath != null) {
                openFolder(requireNotNull(absolutePath), requireNotNull(fileType))
            }
        }
    }

    fun bind(file: FileInfoItem) {
        with(binding) {
            absolutePath = file.absolutePath
            fileType = file.type
            fileNameTextView.text = file.name
            fileSizeTextView.text = file.sizeText
            fileCreationDateTextView.text = file.creationDateText
            fileImageView.setImageDrawable(
                when (file.type) {
                    FileType.VIDEO -> videoImage
                    FileType.FOLDER -> folderImage
                    FileType.DOCUMENT -> documentImage
                    FileType.PDF -> pdfImage
                    FileType.IMAGE -> pictureImage
                    FileType.UNKNOWN_FILE -> unknownFileImage
                }
            )
        }
    }
}
