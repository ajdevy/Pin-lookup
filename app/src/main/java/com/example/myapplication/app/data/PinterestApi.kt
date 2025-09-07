package com.example.myapplication.app.data

import retrofit2.http.GET
import retrofit2.http.Query

interface PinterestApi {
    // Example endpoint for boards or pins; Pinterest API v5 uses cursor pagination
    // Here we model a generic items fetch by page/size via query params for simplicity
    @GET("pins")
    suspend fun getPins(
        @Query("page_size") pageSize: Int = 25,
        @Query("bookmark") bookmark: String? = null
    ): PinsResponse
}


