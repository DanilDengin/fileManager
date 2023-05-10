package com.dengin.files.fileList.di

import com.dengin.files.app.di.AppComponent
import com.dengin.files.fileList.presentation.FileListFragment
import com.dengin.files.fileRoom.di.LastModifiedFileRepositoryBinding
import com.dengin.files.utils.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [
        FileListModule::class,
        LastModifiedFileRepositoryBinding::class
    ],
    dependencies = [AppComponent::class]
)
interface FileListComponent {

    fun inject(fileListFragment: FileListFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): FileListComponent
    }
}
