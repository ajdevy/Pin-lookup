package com.flexsentlabs.myapplication.pixabay.domain

import com.flexsentlabs.myapplication.pixabay.data.PixabaySearchRepository

class SearchImagesUseCase(
    private val repository: PixabaySearchRepository
) {
    suspend operator fun invoke(query: String, page: Int, perPage: Int = 25): PixabaySearchPage {
        return repository.search(query = query, page = page, perPage = perPage)
    }
}


