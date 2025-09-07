package com.example.myapplication.pixabay.data

import com.example.myapplication.pixabay.domain.PixabaySearchPage

interface PixabayRepository {
    suspend fun search(query: String, page: Int, perPage: Int = 25): PixabaySearchPage
}