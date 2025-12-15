package com.gorman.pexelsappkmp.domain.repository

import com.gorman.pexelsappkmp.domain.models.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun insertImage(url: String, name: String)
    fun getAllImages(): Flow<List<Bookmark>>
    suspend fun isImage(url: String): Boolean
    suspend fun deleteByUrl(url: String)
    suspend fun findImageById(imageId: Int): Bookmark?
}