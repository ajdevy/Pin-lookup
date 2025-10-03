package com.example.myapplication.pixabay.domain

data class PixabaySearchPage(
    val total: Int,
    val totalHits: Int,
    val images: List<PixabayImage>
)
