package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository

class GetCuratedPhotosUseCase(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Photo>> {
        return try {
            Result.success(repository.searchCurated(page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}