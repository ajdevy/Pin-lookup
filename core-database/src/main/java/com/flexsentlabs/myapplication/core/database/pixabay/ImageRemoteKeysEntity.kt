package com.flexsentlabs.myapplication.core.database.pixabay

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pixabay_remote_keys")
data class ImageRemoteKeysEntity(
    @PrimaryKey
    val label: String,
    val prevKey: Int?,
    val nextKey: Int?
)
