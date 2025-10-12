package com.flexsentlabs.myapplication.pixabay.data

import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage
import com.flexsentlabs.myapplication.pixabay.domain.PixabaySearchPage

class PixabaySearchRepositoryImpl(
    private val api: PixabayApi
) : PixabaySearchRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage {
        val response = api.searchImages(query = query, page = page, perPage = perPage)
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