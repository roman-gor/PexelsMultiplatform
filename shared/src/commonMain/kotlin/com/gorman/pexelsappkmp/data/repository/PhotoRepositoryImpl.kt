package com.gorman.pexelsappkmp.data.repository

import com.gorman.pexelsappkmp.data.datasource.remote.PexelsApi
import com.gorman.pexelsappkmp.data.mappers.toDomain
import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.repository.PhotoRepository
import com.gorman.pexelsappkmp.logger.AppLogger

class PhotoRepositoryImpl(
    private val api: PexelsApi,
    private val logger: AppLogger
): PhotoRepository {
    override suspend fun search(
        query: String,
        page: Int
    ): List<Photo> {
        return api.searchPhotos(query = query, page = page).toDomain()
    }

    override suspend fun searchCurated(page: Int): List<Photo> {
        logger.d("PhotoRepositoryImpl","Calling PexelsApi.searchCuratedPhotos, Page: $page")
        return api.searchCuratedPhotos(page = page).toDomain()
    }

    override suspend fun searchFeaturedCollections(): List<Collection> {
        return api.searchFeaturedCollections().toDomain()
    }

}