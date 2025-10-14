package com.flexsentlabs.myapplication.database.pixabay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(remoteKey: List<PixabayRemoteKeys>)

    @Query("SELECT * FROM pixabay_remote_keys WHERE searchQuery = :query")
    suspend fun remoteKeysByQuery(query: String): PixabayRemoteKeys?

    @Query("DELETE FROM pixabay_remote_keys WHERE searchQuery = :query")
    suspend fun clearRemoteKeysByQuery(query: String)

    @Query("DELETE FROM pixabay_remote_keys")
    suspend fun clearAll()
}