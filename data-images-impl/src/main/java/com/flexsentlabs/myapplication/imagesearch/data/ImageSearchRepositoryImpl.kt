package com.flexsentlabs.myapplication.imagesearch.data

import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage
import com.flexsentlabs.myapplication.imagesearch.data.api.PixabayApi
import com.flexsentlabs.myapplication.imagesearch.domain.ImageSearchRepository

class ImageSearchRepositoryImpl(
    private val pixabayApi: PixabayApi
) : ImageSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage {
        val response = pixabayApi.searchImages(query, page, perPage)
        return PixabaySearchPage(
            total = response.total,
            totalHits = response.totalHits,
            images = response.hits.map { hit ->
                PixabayImage(
                    id = hit.id,
                    tags = (hit.tags ?: "").split(',').map { it.trim() }.filter { it.isNotEmpty() },
                    previewUrl = hit.previewURL,
                    webformatUrl = hit.webformatURL,
                    largeImageUrl = hit.largeImageURL,
                    userName = hit.user
                )
            }
        )
    }
}