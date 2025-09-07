package com.example.myapplication.app.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pin(
    val id: String,
    val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "link") val linkUrl: String?,
    @Json(name = "media") val media: Media?
)

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "images") val images: Map<String, MediaImage>?
)

@JsonClass(generateAdapter = true)
data class MediaImage(
    val url: String?,
    val width: Int?,
    val height: Int?
)

@JsonClass(generateAdapter = true)
data class PinsResponse(
    val items: List<Pin> = emptyList(),
    val bookmark: String? = null
)


