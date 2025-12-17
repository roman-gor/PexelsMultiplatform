package com.gorman.pexelsappkmp.logger

interface AppLogger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

expect fun provideAppLogger(): AppLogger