package com.shabelnikd.bilimtrack

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BilimTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BilimTrackApplication)
            modules(appModule, viewModelModule)
        }
    }
}