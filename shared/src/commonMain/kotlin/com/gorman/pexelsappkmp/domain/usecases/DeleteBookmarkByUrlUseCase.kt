package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository

class DeleteBookmarkByUrlUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(url: String) {
        bookmarkRepository.deleteByUrl(url)
    }
}