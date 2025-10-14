package com.flexsentlabs.myapplication.data.images

import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage

interface PixabaySearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage
}