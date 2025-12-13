package com.gorman.pexelsappkmp.domain.repository

import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.models.Photo

interface PhotoRepository {
    suspend fun search(query: String, page: Int): List<Photo>
    suspend fun searchCurated(page: Int): List<Photo>
    suspend fun searchFeaturedCollections(): List<Collection>
}