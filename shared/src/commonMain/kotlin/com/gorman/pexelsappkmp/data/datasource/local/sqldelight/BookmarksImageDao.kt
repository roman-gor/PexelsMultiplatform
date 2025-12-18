package com.gorman.pexelsappkmp.data.datasource.local.sqldelight

import com.gorman.pexelsappkmp.data.models.BookmarkImage
import kotlinx.coroutines.flow.Flow

interface BookmarksImageDao {
    suspend fun insert(imageUrl: String, phName: String)
    fun getAll(): Flow<List<BookmarkImage>>
    suspend fun existsByUrl(imageUrl: String): Boolean
    suspend fun deleteByUrl(imageUrl: String)
    suspend fun findById(id: Long): BookmarkImage?
    suspend fun delete(id: Long)
    fun getAllPhotos(): List<BookmarkImage>
}