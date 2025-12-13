package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository

class GetFeaturedCollectionsUseCase(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(): List<Collection> {
        return repository.searchFeaturedCollections()
    }
}