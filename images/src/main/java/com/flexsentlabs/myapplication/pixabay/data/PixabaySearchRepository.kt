package com.flexsentlabs.myapplication.pixabay.data

import com.flexsentlabs.myapplication.pixabay.domain.PixabaySearchPage

interface PixabaySearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int = 25): PixabaySearchPage
}