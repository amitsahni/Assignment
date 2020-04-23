package com.assignment.view

import androidx.multidex.MultiDexApplication
import com.assignment.BuildConfig
import com.assignment.di.includeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AppApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(includeModule())
        }
    }
}