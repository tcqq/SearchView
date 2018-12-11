package com.example.searchview

import android.app.Application
import timber.log.Timber

/**
 * @author Alan Dreamer
 * @since 2018-12-11 Created
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}