package com.gorman.pexelsappkmp.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.gorman.pexelsappkmp.data.datasource.local.AppDatabase
import com.gorman.pexelsappkmp.data.datasource.remote.createPexelsApi
import com.gorman.pexelsappkmp.data.repository.BookmarkRepositoryImpl
import com.gorman.pexelsappkmp.data.repository.PhotoRepositoryImpl
import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository
import com.gorman.pexelsappkmp.domain.usecases.AddBookmarkUseCase
import com.gorman.pexelsappkmp.domain.usecases.DeleteBookmarkByUrlUseCase
import com.gorman.pexelsappkmp.domain.usecases.FindBookmarkByIdUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetAllBookmarksUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetCuratedPhotosUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetFeaturedCollectionsUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetPhotosByQueryUseCase
import com.gorman.pexelsappkmp.domain.usecases.SearchInDBOnceUseCase
import com.gorman.pexelsappkmp.domain.viewmodels.BookmarksViewModel
import com.gorman.pexelsappkmp.domain.viewmodels.DetailsViewModel
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClient> {
        provideHttpClient()
    }
    single {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>())
            .baseUrl("https://api.pexels.com/")
            .build()
    }
    single { get<Ktorfit>().createPexelsApi() }
    singleOf(::PhotoRepositoryImpl).bind<PhotoRepository>()
    singleOf(::BookmarkRepositoryImpl).bind<BookmarkRepository>()
    single<AppDatabase> {
        val builder = get<RoomDatabase.Builder<AppDatabase>>()
        builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().bookmarkImageDao() }
}

val useCasesModule = module {
    factoryOf(::GetCuratedPhotosUseCase)
    factoryOf(::GetFeaturedCollectionsUseCase)
    factoryOf(::GetPhotosByQueryUseCase)
    factoryOf(::AddBookmarkUseCase)
    factoryOf(::DeleteBookmarkByUrlUseCase)
    factoryOf(::FindBookmarkByIdUseCase)
    factoryOf(::GetAllBookmarksUseCase)
    factoryOf(::SearchInDBOnceUseCase)
}

val viewModelsModule = module {
    single { HomeViewModel(get(), get(), get()) }
    single { DetailsViewModel(get(), get(), get(), get()) }
    single { BookmarksViewModel(get()) }
}

expect val platformModule: Module

expect fun provideHttpClient(): HttpClient