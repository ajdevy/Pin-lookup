package com.flexsentlabs.myapplication.imagedetails.domain

import com.flexsentlabs.myapplication.data.images.ImageRepository
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

class GetImageUseCase(private val imageRepository: ImageRepository) {

    suspend fun invoke(id: Long): PixabayImage? {
        return imageRepository.findImageById(id)
    }
}