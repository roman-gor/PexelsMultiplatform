package com.gorman.pexelsappkmp.logger

import platform.Foundation.NSLog

class IosAppLoggerImpl: AppLogger {
    override fun d(tag: String, message: String) {
        NSLog("[%s] DEBUG: %s", tag, message)
    }
    override fun e(tag: String, message: String, throwable: Throwable?) {
        val errorInfo = throwable?.message ?: "No error message"
        NSLog("[%s] ERROR: %s. Exception: %s", tag, message, errorInfo)
    }
}

actual fun provideAppLogger(): AppLogger = IosAppLoggerImpl()