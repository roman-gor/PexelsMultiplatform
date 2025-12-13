package com.gorman.pexelsappkmp

import android.app.Application
import com.gorman.pexelsappkmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Application)
            androidLogger()
        }
    }
}