package com.flexsentlabs.myapplication.pixabay.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pixabay_remote_keys")
data class PixabayRemoteKeys(
    @PrimaryKey
    val searchQuery: String,
    val prevKey: Int?,
    val nextKey: Int?
)
