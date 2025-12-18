package com.gorman.pexelsappkmp.data.datasource.local.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gorman.pexelsappkmp.data.models.BookmarkImage
import com.gorman.pexelsappkmp.db.BookmarksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkImageDaoImpl(
    db: BookmarksDatabase
): BookmarksImageDao {
    private val queries = db.bookmarksImageQueries
    override suspend fun insert(imageUrl: String, phName: String) {
        queries.insertImage(imageUrl, phName)
    }

    override fun getAll(): Flow<List<BookmarkImage>> {
        return queries.getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { BookmarkImage(id = it.id, imageUrl = it.imageUrl, phName = it.phName) }
            }
    }

    override suspend fun existsByUrl(imageUrl: String): Boolean {
        return queries.existsByUrl(imageUrl).executeAsOne()
    }

    override suspend fun deleteByUrl(imageUrl: String) {
        queries.deleteByUrl(imageUrl)
    }

    override suspend fun findById(id: Long): BookmarkImage? {
        return queries.findById(id).executeAsOneOrNull()?.let {
            BookmarkImage(id = it.id, imageUrl = it.imageUrl, phName = it.phName)
        }
    }

    override suspend fun delete(id: Long) {
        queries.deleteById(id)
    }

    override fun getAllPhotos(): List<BookmarkImage> {
        return queries.getAll().executeAsList().map {
            BookmarkImage(id = it.id, imageUrl = it.imageUrl, phName = it.phName)
        }
    }
}