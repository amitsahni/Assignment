package com.assignment.view

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.assignment.BuildConfig
import com.assignment.di.*
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
            modules(listOf(context, repository, useCase, apiService, vm))
        }
    }
}