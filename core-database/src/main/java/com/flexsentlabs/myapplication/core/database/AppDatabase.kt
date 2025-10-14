package com.flexsentlabs.myapplication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageDao
import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageEntity
import com.flexsentlabs.myapplication.core.database.pixabay.PixabayRemoteKeysDao

@Database(
    entities = [PixabayImageEntity::class, com.flexsentlabs.myapplication.core.database.pixabay.ImageRemoteKeysEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pixabayImageDao(): PixabayImageDao
    abstract fun pixabayRemoteKeysDao(): PixabayRemoteKeysDao
}
