package com.dengin.files.app

import android.app.Application
import com.dengin.files.app.di.AppComponent
import com.dengin.files.app.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }
}
