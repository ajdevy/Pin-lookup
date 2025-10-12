package com.flexsentlabs.myapplication.pixabay.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [PixabayImageEntity::class, PixabayRemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class PixabayDatabase : RoomDatabase() {
    abstract fun pixabayImageDao(): PixabayImageDao
    abstract fun pixabayRemoteKeysDao(): PixabayRemoteKeysDao

    companion object {
        fun create(context: Context): PixabayDatabase {
            return Room.databaseBuilder(
                context,
                PixabayDatabase::class.java,
                "pixabay_database"
            ).build()
        }
    }
}
