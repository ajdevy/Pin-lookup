package com.flexsentlabs.myapplication.imagedetails.data

import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage

interface PixabayImageRepository {
    suspend fun findImageById(id: Long): PixabayImage?
}
