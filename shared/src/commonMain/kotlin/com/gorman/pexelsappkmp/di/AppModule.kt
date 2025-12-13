package com.gorman.pexelsappkmp.di

import com.gorman.pexelsappkmp.data.datasource.remote.PexelsApi
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Header
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bp5on48mdl079Q514RaE3gxt7uDQUzwSyvel6G8JlbQfqWMQlPM8eldF")
            }
        }
    }
    single {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>())
            .baseUrl("https://api.pexels.com/v1/")
            .build()
    }
    single {
        get<Ktorfit>().create<PexelsApi>()
    }
}