package com.flexsentlabs.myapplication.imagesearch.domain

import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage
import timber.log.Timber

class SearchImagesUseCase(
    private val repository: ImageSearchRepository
) {
    suspend operator fun invoke(query: String, page: Int, perPage: Int = 25): PixabaySearchPage {
        Timber.d("SearchImagesUseCase called with query: $query, page: $page, perPage: $perPage")
        val result = repository.search(query = query, page = page, perPage = perPage)
        Timber.d("SearchImagesUseCase result: ${result.images.size} images found")
        return result
    }
}


