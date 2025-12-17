package com.gorman.pexelsappkmp.di

import com.gorman.pexelsappkmp.BuildKonfig
import com.gorman.pexelsappkmp.data.datasource.local.getDatabaseBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val pexelsKey = BuildKonfig.API_KEY

actual val platformModule = module {
    single { getDatabaseBuilder() }
}

actual fun provideHttpClient(): HttpClient =
    HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }

        defaultRequest {
            header("Authorization", pexelsKey)
        }
    }