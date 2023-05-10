package com.dengin.files.app.di

import com.dengin.files.utils.stringResources.StringResources
import com.dengin.files.utils.stringResources.StringResourcesImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface StringResourcesBindingModule {

    @Binds
    @Singleton
    fun bindStringResources(stringResourcesImpl: StringResourcesImpl): StringResources
}
