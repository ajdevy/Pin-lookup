package com.flexsentlabs.myapplication.imagesearch.data

import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage
import com.flexsentlabs.myapplication.imagesearch.data.api.PixabayApi
import com.flexsentlabs.myapplication.imagesearch.domain.ImageSearchRepository

class ImageSearchRepositoryImpl(
    private val pixabayApi: PixabayApi
) : ImageSearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage {
        timber.log.Timber.d("ImageSearchRepositoryImpl.search called with query: $query, page: $page, perPage: $perPage")
        val response = pixabayApi.searchImages(query, page, perPage)
        timber.log.Timber.d("API response received: total=${response.total}, totalHits=${response.totalHits}, hits=${response.hits.size}")
        val result = PixabaySearchPage(
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
        timber.log.Timber.d("Repository returning ${result.images.size} images")
        return result
    }
}