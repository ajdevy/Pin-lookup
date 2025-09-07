package com.example.myapplication.pixabay.domain

data class PixabayImage(
    val id: Long,
    val tags: List<String>,
    val previewUrl: String?,
    val webformatUrl: String?,
    val largeImageUrl: String?,
    val userName: String?
)
