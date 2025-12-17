package com.gorman.pexelsappkmp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gorman.pexelsappkmp.data.models.BookmarkImage

@Database(entities = [BookmarkImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkImageDao(): BookmarksImageDao
}
