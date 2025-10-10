package com.example.myapplication.imagedetails.data

import com.example.myapplication.pixabay.data.PixabayImageDao
import com.example.myapplication.pixabay.domain.PixabayImage

class PixabayImageRepositoryImpl(private val pixabayImageDao: PixabayImageDao) : PixabayImageRepository {

    override suspend fun findImageById(id: Long): PixabayImage? {
        return pixabayImageDao.findById(id)?.toDomain()
    }
}