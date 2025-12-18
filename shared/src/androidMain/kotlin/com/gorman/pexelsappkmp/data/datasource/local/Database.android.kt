//package com.gorman.pexelsappkmp.data.datasource.local
//
//import android.content.Context
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.gorman.pexelsappkmp.data.datasource.local.room.AppDatabase
//
//fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
//    val appContext = context.applicationContext
//    val dbFile = appContext.getDatabasePath("bookmarks.db")
//    return Room.databaseBuilder<AppDatabase>(
//        context = appContext,
//        name = dbFile.absolutePath
//    ).fallbackToDestructiveMigration(false)
//}