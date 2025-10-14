package com.flexsentlabs.myapplication.domain.images.models

data class PixabayImage(
    val id: Long,
    val tags: List<String>,
    val previewUrl: String?,
    val webformatUrl: String?,
    val largeImageUrl: String?,
    val userName: String?
)
