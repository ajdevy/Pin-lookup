package com.flexsentlabs.myapplication.imagesearch.domain

import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage

interface ImageSearchRepository {
    suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage
}