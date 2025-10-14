package com.flexsentlabs.myapplication.imagedetails.data

import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageDao
import com.flexsentlabs.myapplication.data.images.ImageRepository
import com.flexsentlabs.myapplication.imagesearch.data.mapper.toDomain
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

class ImageRepositoryImpl(private val pixabayImageDao: PixabayImageDao) : ImageRepository {

    override suspend fun findImageById(id: Long): PixabayImage? {
        return pixabayImageDao.findById(id)?.toDomain()
    }
}