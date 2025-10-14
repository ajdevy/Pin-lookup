package com.flexsentlabs.myapplication.core.database.pixabay

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pixabay_images")
data class PixabayImageEntity(
    @PrimaryKey
    val id: Long,
    val tags: String, // Store as comma-separated string
    val previewUrl: String?,
    val webformatUrl: String?,
    val largeImageUrl: String?,
    val userName: String?,
    val searchQuery: String,
    val page: Int
)
