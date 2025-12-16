package com.gorman.pexelsappkmp.domain.usecases

import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository

class GetPhotosByQueryUseCase (
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(query: String, page: Int): Result<List<Photo>> {
        return try {
            Result.success(repository.search(query, page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}