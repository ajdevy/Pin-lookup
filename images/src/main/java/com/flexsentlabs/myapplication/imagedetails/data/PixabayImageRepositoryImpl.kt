package com.flexsentlabs.myapplication.imagedetails.data

import com.flexsentlabs.myapplication.database.pixabay.PixabayImageDao
import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage

class PixabayImageRepositoryImpl(private val pixabayImageDao: PixabayImageDao) : PixabayImageRepository {

    override suspend fun findImageById(id: Long): PixabayImage? {
        return pixabayImageDao.findById(id)?.toDomain()
    }
}