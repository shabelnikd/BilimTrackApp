package com.shabelnikd.bilimtrack

import android.app.Application
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BilimTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceHelper().initialize(this)
    }
}