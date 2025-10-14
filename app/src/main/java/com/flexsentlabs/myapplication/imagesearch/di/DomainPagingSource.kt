package com.flexsentlabs.myapplication.imagesearch.di

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageEntity
import com.flexsentlabs.myapplication.imagesearch.data.mapper.toDomain
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import timber.log.Timber

class DomainPagingSource(
    private val entityPagingSource: PagingSource<Int, PixabayImageEntity>
) : PagingSource<Int, PixabayImage>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayImage> {
        Timber.d("DomainPagingSource.load called with params: $params")
        return when (val result = entityPagingSource.load(params)) {
            is LoadResult.Page -> {
                Timber.d("DomainPagingSource received ${result.data.size} entities from database")
                val domainData = result.data.map { it.toDomain() }
                Timber.d("DomainPagingSource converted to ${domainData.size} domain objects")
                LoadResult.Page(
                    data = domainData,
                    prevKey = result.prevKey,
                    nextKey = result.nextKey
                )
            }
            is LoadResult.Error -> {
                Timber.e("DomainPagingSource error: ${result.throwable}")
                result as LoadResult<Int, PixabayImage>
            }
            is LoadResult.Invalid -> {
                Timber.w("DomainPagingSource invalid result")
                result as LoadResult<Int, PixabayImage>
            }
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, PixabayImage>): Int? {
        // For simplicity, we'll just return null for refresh key
        // This is acceptable for most use cases
        return null
    }
}
