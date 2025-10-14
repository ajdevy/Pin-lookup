package com.flexsentlabs.myapplication.imagesearch.data.mapper

import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageEntity
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

fun PixabayImageEntity.toDomain(): PixabayImage {
    return PixabayImage(
        id = id,
        tags = tags.split(',').map { it.trim() }.filter { it.isNotEmpty() },
        previewUrl = previewUrl,
        webformatUrl = webformatUrl,
        largeImageUrl = largeImageUrl,
        userName = userName
    )
}

fun PixabayImage.toEntity(searchQuery: String, page: Int): PixabayImageEntity {
    return PixabayImageEntity(
        id = id,
        tags = tags.joinToString(","),
        previewUrl = previewUrl,
        webformatUrl = webformatUrl,
        largeImageUrl = largeImageUrl,
        userName = userName,
        searchQuery = searchQuery,
        page = page
    )
}
