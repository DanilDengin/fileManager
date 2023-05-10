package com.dengin.files.app.di

import android.content.Context
import com.dengin.files.fileRoom.data.database.FileInfoDatabase
import com.dengin.files.utils.stringResources.StringResources
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        StringResourcesBindingModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    val stringResources: StringResources

    val fileInfoDatabase: FileInfoDatabase

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
