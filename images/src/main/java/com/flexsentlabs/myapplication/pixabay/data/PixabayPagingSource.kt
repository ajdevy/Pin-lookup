package com.flexsentlabs.myapplication.pixabay.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage
import com.flexsentlabs.myapplication.pixabay.domain.SearchImagesUseCase

class PixabayPagingSource(
    private val searchUseCase: SearchImagesUseCase,
    private val query: String
) : PagingSource<Int, PixabayImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayImage> {
        return try {
            val page = params.key ?: 1
            val result = searchUseCase(query = query, page = page, perPage = 20)
            
            LoadResult.Page(
                data = result.images,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.images.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PixabayImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
