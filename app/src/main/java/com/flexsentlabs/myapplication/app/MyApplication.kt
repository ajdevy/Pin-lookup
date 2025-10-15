package com.flexsentlabs.myapplication.app

import android.app.Application
import com.flexsentlabs.myapplication.app.di.appModule
import com.flexsentlabs.myapplication.imagesearch.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import android.util.Log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}

/**
 * Custom Timber tree for release builds that only logs errors and warnings
 */
class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Only log errors and warnings in release builds
        if (priority == Log.ERROR || priority == Log.WARN) {
            // You could send logs to a crash reporting service here
            // For now, we'll just use the default Android logging
            when (priority) {
                Log.ERROR -> Log.e(tag, message, t)
                Log.WARN -> Log.w(tag, message, t)
            }
        }
    }
}