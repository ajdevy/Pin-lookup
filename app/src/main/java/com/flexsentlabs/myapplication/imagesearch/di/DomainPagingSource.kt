package com.flexsentlabs.myapplication.imagesearch.di

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flexsentlabs.myapplication.core.database.pixabay.PixabayImageEntity
import com.flexsentlabs.myapplication.imagesearch.data.mapper.toDomain
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

class DomainPagingSource(
    private val entityPagingSource: PagingSource<Int, PixabayImageEntity>
) : PagingSource<Int, PixabayImage>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayImage> {
        return when (val result = entityPagingSource.load(params)) {
            is LoadResult.Page -> {
                LoadResult.Page(
                    data = result.data.map { it.toDomain() },
                    prevKey = result.prevKey,
                    nextKey = result.nextKey
                )
            }
            is LoadResult.Error -> result as LoadResult<Int, PixabayImage>
            is LoadResult.Invalid -> result as LoadResult<Int, PixabayImage>
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, PixabayImage>): Int? {
        // For simplicity, we'll just return null for refresh key
        // This is acceptable for most use cases
        return null
    }
}
