package com.flexsentlabs.myapplication.pixabay.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonQualifier
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20,
        @Query("image_type") imageType: String = "photo",
        @Query("safesearch") safeSearch: Boolean = true
    ): PixabaySearchResponse
}

data class PixabaySearchResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixabayHitDto>
)

data class PixabayHitDto(
    val id: Long,
    val pageURL: String?,
    val type: String?,
    val tags: String?,
    val previewURL: String?,
    val previewWidth: Int?,
    val previewHeight: Int?,
    val webformatURL: String?,
    val webformatWidth: Int?,
    val webformatHeight: Int?,
    val largeImageURL: String?,
    val imageWidth: Int?,
    val imageHeight: Int?,
    val imageSize: Int?,
    val views: Int?,
    val downloads: Int?,
    val collections: Int?,
    val likes: Int?,
    val comments: Int?,
    @Json(name = "user_id")
    val userId: Int?,
    val user: String?,
    val userImageURL: String?
)


