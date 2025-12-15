package com.gorman.pexelsappkmp.di

import com.gorman.pexelsappkmp.data.datasource.local.getDatabaseBuilder
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {
    single { getDatabaseBuilder() }
    single { Darwin.create {} }
}
