package com.flexsentlabs.myapplication.imagesearch.domain

import com.flexsentlabs.myapplication.domain.images.models.PixabaySearchPage

class SearchImagesUseCase(
    private val repository: ImageSearchRepository
) {
    suspend operator fun invoke(query: String, page: Int, perPage: Int = 25): PixabaySearchPage {
        return repository.search(query = query, page = page, perPage = perPage)
    }
}


