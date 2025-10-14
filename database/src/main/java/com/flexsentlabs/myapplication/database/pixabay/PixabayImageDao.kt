package com.flexsentlabs.myapplication.database.pixabay

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

    @Query("SELECT * FROM pixabay_images WHERE id = :id")
    suspend fun findById(id: Long): PixabayImageEntity?

    @Query("DELETE FROM pixabay_images")
    suspend fun clearAll()
}

