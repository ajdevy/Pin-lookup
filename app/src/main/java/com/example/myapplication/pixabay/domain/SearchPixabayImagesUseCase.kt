package com.example.myapplication.pixabay.domain

import com.example.myapplication.pixabay.data.PixabayRepository

class SearchPixabayImagesUseCase(
    private val repository: PixabayRepository
) {
    suspend operator fun invoke(query: String, page: Int, perPage: Int = 25): PixabaySearchPage {
        return repository.search(query = query, page = page, perPage = perPage)
    }
}


