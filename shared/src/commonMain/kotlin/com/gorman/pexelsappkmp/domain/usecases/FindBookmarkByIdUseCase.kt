package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository

class FindBookmarkByIdUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(imageId: Int): Bookmark? =
        bookmarkRepository.findImageById(imageId)
}