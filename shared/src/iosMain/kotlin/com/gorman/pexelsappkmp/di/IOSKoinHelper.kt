package com.gorman.pexelsappkmp.di

import com.gorman.pexelsappkmp.domain.viewmodels.BookmarksViewModel
import com.gorman.pexelsappkmp.domain.viewmodels.DetailsViewModel
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class IOSKoinHelper: KoinComponent {
    val getHomeViewModel: HomeViewModel get() = get()
    val getDetailsViewModel: DetailsViewModel get() = get()
    val getBookmarksViewModel: BookmarksViewModel get() = get()
}