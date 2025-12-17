package com.gorman.pexelsappkmp.logger

import android.util.Log

class AppLoggerImpl: AppLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message)
    }

}

actual fun provideAppLogger(): AppLogger = AppLoggerImpl()