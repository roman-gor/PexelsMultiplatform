package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository

class SearchInDBOnceUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(url: String): Boolean =
        bookmarkRepository.isImage(url)
}