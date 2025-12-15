package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository

class AddBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(url: String, name: String) {
        bookmarkRepository.insertImage(url, name)
    }
}