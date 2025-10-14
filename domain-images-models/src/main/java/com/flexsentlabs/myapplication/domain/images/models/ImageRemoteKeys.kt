package com.flexsentlabs.myapplication.domain.images.models

data class ImageRemoteKeys(
    val label: String,
    val prevKey: Int?,
    val nextKey: Int?
)