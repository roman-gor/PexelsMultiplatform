package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository
import com.gorman.pexelsappkmp.domain.viewmodels.logger

class GetCuratedPhotosUseCase(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(page: Int): List<Photo> {
        logger("GetCuratedPhotosUseCase: Invoked, Page: $page")
        return repository.searchCurated(page)
    }
}