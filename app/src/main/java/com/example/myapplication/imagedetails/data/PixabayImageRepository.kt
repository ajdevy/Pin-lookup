package com.example.myapplication.imagedetails.data

import com.example.myapplication.pixabay.domain.PixabayImage

interface PixabayImageRepository {
    suspend fun findImageById(id: Long): PixabayImage?
}
