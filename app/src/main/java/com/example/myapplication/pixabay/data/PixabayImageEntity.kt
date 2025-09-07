package com.example.myapplication.pixabay.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.pixabay.domain.PixabayImage

@Entity(tableName = "pixabay_images")
data class PixabayImageEntity(
    @PrimaryKey
    val id: Long,
    val tags: String, // Store as comma-separated string
    val previewUrl: String?,
    val webformatUrl: String?,
    val largeImageUrl: String?,
    val userName: String?
) {
    fun toDomain(): PixabayImage {
        return PixabayImage(
            id = id,
            tags = tags.split(',').map { it.trim() }.filter { it.isNotEmpty() },
            previewUrl = previewUrl,
            webformatUrl = webformatUrl,
            largeImageUrl = largeImageUrl,
            userName = userName
        )
    }
}

fun PixabayImage.toEntity(): PixabayImageEntity {
    return PixabayImageEntity(
        id = id,
        tags = tags.joinToString(","),
        previewUrl = previewUrl,
        webformatUrl = webformatUrl,
        largeImageUrl = largeImageUrl,
        userName = userName
    )
}
