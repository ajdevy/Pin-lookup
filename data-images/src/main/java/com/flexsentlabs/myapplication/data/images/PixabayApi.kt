package com.flexsentlabs.myapplication.data.images

import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PixabaySearchPage
}