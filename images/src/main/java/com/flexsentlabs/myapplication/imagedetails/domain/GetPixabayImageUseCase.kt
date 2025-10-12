package com.flexsentlabs.myapplication.imagedetails.domain

import com.flexsentlabs.myapplication.imagedetails.data.PixabayImageRepository
import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage

class GetPixabayImageUseCase(private val pixabayImageRepository: PixabayImageRepository) {

    suspend fun invoke(id: Long): PixabayImage? {
        return pixabayImageRepository.findImageById(id)
    }
}