package com.gorman.pexelsappkmp.data.repository

import com.gorman.pexelsappkmp.data.datasource.local.BookmarksImageDao
import com.gorman.pexelsappkmp.data.mappers.toDomain
import com.gorman.pexelsappkmp.data.models.BookmarkImage
import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl(
    private val dao: BookmarksImageDao
): BookmarkRepository {
    override suspend fun insertImage(url: String, name: String) {
        dao.insert(BookmarkImage(imageUrl = url, phName = name))
    }

    override fun getAllImages(): Flow<List<Bookmark>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun isImage(url: String): Boolean = dao.existsByUrl(url)

    override suspend fun deleteByUrl(url: String) {
        dao.deleteByUrl(url)
    }

    override suspend fun findImageById(imageId: Int): Bookmark? {
        return dao.findById(imageId)?.toDomain()
    }
}