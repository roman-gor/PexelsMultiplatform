package com.gorman.pexelsappkmp.data.repository

import com.gorman.pexelsappkmp.data.datasource.local.sqldelight.BookmarksImageDao
import com.gorman.pexelsappkmp.data.mappers.toDomain
import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.repository.BookmarkRepository
import com.gorman.pexelsappkmp.logger.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl(
    private val dao: BookmarksImageDao,
    private val logger: AppLogger
): BookmarkRepository {
    override suspend fun insertImage(imageUrl: String, phName: String) {
        dao.insert(imageUrl = imageUrl, phName = phName)
        logger.d("BookmarkRep","${dao.getAllPhotos()}")
    }

    override fun getAllImages(): Flow<List<Bookmark>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun isImage(imageUrl: String): Boolean = dao.existsByUrl(imageUrl)

    override suspend fun deleteByUrl(imageUrl: String) {
        dao.deleteByUrl(imageUrl)
        logger.d("BookmarkRep","${dao.getAllPhotos()}")
    }

    override suspend fun findImageById(id: Int): Bookmark? {
        return dao.findById(id.toLong())?.toDomain()
    }
}