package com.gorman.pexelsappkmp.di

import com.gorman.pexelsappkmp.data.datasource.local.getDatabaseBuilder
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformModule = module {
    single {
        getDatabaseBuilder(get())
    }
    single { OkHttp.create {} }
}
