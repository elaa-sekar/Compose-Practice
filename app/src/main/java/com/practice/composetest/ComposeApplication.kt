package com.practice.composetest

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ComposeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initializing Log & Inspect Tools
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(applicationContext)
        }
    }
}