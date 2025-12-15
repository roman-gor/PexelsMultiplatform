package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

class GetAllBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke(): Flow<List<Bookmark>> {
        return bookmarkRepository.getAllImages()
    }
}