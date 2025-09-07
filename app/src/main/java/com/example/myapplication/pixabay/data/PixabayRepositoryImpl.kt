package com.example.myapplication.pixabay.data

import com.example.myapplication.pixabay.domain.PixabayImage
import com.example.myapplication.pixabay.domain.PixabaySearchPage

class PixabayRepositoryImpl(
    private val api: PixabayApi
) : PixabayRepository {

    override suspend fun search(query: String, page: Int, perPage: Int): PixabaySearchPage {
        val dto = api.searchImages(query = query, page = page, perPage = perPage)
        return PixabaySearchPage(
            total = dto.total,
            totalHits = dto.totalHits,
            images = dto.hits.map { hit ->
                PixabayImage(
                    id = hit.id,
                    tags = (hit.tags ?: "").split(',').map { it.trim() }.filter { it.isNotEmpty() },
                    previewUrl = hit.previewURL,
                    webformatUrl = hit.webformatURL,
                    largeImageUrl = hit.largeImageURL,
                    userName = hit.user
                )
            },
            page = page,
            perPage = perPage
        )
    }
}