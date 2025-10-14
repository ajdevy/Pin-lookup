package com.flexsentlabs.myapplication.database.pixabay

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pixabay_remote_keys")
data class PixabayRemoteKeys(
    @PrimaryKey
    val searchQuery: String,
    val prevKey: Int?,
    val nextKey: Int?
)
