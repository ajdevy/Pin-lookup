package com.flexsentlabs.myapplication.data.images

import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

interface ImageRepository {
    suspend fun findImageById(id: Long): PixabayImage?
}
