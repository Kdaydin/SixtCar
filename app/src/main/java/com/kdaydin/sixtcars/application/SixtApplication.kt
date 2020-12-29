package com.kdaydin.sixtcars.application

import android.app.Application
import com.kdaydin.sixtcars.module.networkModule
import com.kdaydin.sixtcars.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SixtApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SixtApplication)
            modules(networkModule, viewModelModule)
        }
    }
}