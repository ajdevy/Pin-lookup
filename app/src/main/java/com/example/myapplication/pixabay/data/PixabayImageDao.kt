package com.example.myapplication.pixabay.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayImageDao {
    @Query("SELECT * FROM pixabay_images ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, PixabayImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<PixabayImageEntity>)

    @Query("DELETE FROM pixabay_images")
    suspend fun clearAll()
}
