package com.gorman.pexelsappkmp.domain.repository

import com.gorman.pexelsappkmp.domain.models.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun insertImage(imageUrl: String, phName: String)
    fun getAllImages(): Flow<List<Bookmark>>
    suspend fun isImage(imageUrl: String): Boolean
    suspend fun deleteByUrl(imageUrl: String)
    suspend fun findImageById(id: Int): Bookmark?
}