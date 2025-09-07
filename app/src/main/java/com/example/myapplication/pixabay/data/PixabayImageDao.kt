package com.example.myapplication.pixabay.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayImageDao {
    @Query("SELECT * FROM pixabay_images WHERE searchQuery = :query ORDER BY page ASC, id ASC")
    fun pagingSource(query: String): PagingSource<Int, PixabayImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<PixabayImageEntity>)

    @Query("DELETE FROM pixabay_images WHERE searchQuery = :query")
    suspend fun clearByQuery(query: String)

    @Query("DELETE FROM pixabay_images")
    suspend fun clearAll()
}

@Dao
interface PixabayRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PixabayRemoteKeys>)

    @Query("SELECT * FROM pixabay_remote_keys WHERE searchQuery = :query")
    suspend fun remoteKeysByQuery(query: String): PixabayRemoteKeys?

    @Query("DELETE FROM pixabay_remote_keys WHERE searchQuery = :query")
    suspend fun clearRemoteKeysByQuery(query: String)

    @Query("DELETE FROM pixabay_remote_keys")
    suspend fun clearAll()
}
