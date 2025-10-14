package com.flexsentlabs.myapplication.imagesearch.data.mapper

import com.flexsentlabs.myapplication.core.database.pixabay.ImageRemoteKeysEntity
import com.flexsentlabs.myapplication.domain.images.models.ImageRemoteKeys

fun ImageRemoteKeysEntity.toDomain(): ImageRemoteKeys {
    return ImageRemoteKeys(
        label = label,
        prevKey = prevKey,
        nextKey = nextKey
    )
}

fun ImageRemoteKeys.toEntity(): ImageRemoteKeysEntity {
    return ImageRemoteKeysEntity(
        label = label,
        prevKey = prevKey,
        nextKey = nextKey
    )
}