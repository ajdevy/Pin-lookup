package com.flexsentlabs.myapplication.core.database.pixabay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ImageRemoteKeysEntity>)

    @Query("SELECT * FROM pixabay_remote_keys WHERE label = :label")
    suspend fun remoteKeysByLabel(label: String): ImageRemoteKeysEntity?

    @Query("DELETE FROM pixabay_remote_keys")
    suspend fun clearRemoteKeys()
}