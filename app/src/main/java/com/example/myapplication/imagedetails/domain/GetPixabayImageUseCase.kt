package com.example.myapplication.imagedetails.domain

import com.example.myapplication.imagedetails.data.PixabayImageRepository
import com.example.myapplication.pixabay.domain.PixabayImage

class GetPixabayImageUseCase(private val pixabayImageRepository: PixabayImageRepository) {

    suspend fun invoke(id: Long): PixabayImage? {
        return pixabayImageRepository.findImageById(id)
    }
}