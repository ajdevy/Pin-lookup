package com.flexsentlabs.myapplication.domain.images.models

data class PixabaySearchPage(
    val total: Int,
    val totalHits: Int,
    val images: List<PixabayImage>
)
