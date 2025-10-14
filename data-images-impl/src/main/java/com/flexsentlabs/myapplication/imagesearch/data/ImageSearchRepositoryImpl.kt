package com.flexsentlabs.myapplication.imagesearch.data

import com.flexsentlabs.myapplication.data.images.PixabayApi
import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage
import com.flexsentlabs.myapplication.imagesearch.domain.ImageSearchRepository

class ImageSearchRepositoryImpl(
    private val pixabayApi: PixabayApi
) : ImageSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage {
        return pixabayApi.searchImages(query, page, perPage)
    }
}