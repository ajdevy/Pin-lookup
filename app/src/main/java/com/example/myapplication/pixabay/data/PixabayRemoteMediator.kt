package com.example.myapplication.pixabay.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.pixabay.domain.SearchImagesUseCase

//TODO: inject it with @ActivityContext
@OptIn(ExperimentalPagingApi::class)
class PixabayRemoteMediator(
    private val database: PixabayDatabase,
    private val searchUseCase: SearchImagesUseCase,
    private val query: String
) : RemoteMediator<Int, PixabayImageEntity>() {

    private val imageDao = database.pixabayImageDao()
    private val remoteKeysDao = database.pixabayRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PixabayImageEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    nextKey
                }
            }

            val result = searchUseCase(query = query, page = page, perPage = 20)
            val images = result.images.map { it.toEntity(query, page) }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    imageDao.clearByQuery(query)
                    remoteKeysDao.clearRemoteKeysByQuery(query)
                }

                imageDao.insertAll(images)
                remoteKeysDao.insertAll(
                    listOf(
                        PixabayRemoteKeys(
                            searchQuery = query,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (result.images.isEmpty()) null else page + 1
                        )
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = result.images.isEmpty())
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PixabayImageEntity>
    ): PixabayRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { image ->
                remoteKeysDao.remoteKeysByQuery(query)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PixabayImageEntity>
    ): PixabayRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysByQuery(query)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PixabayImageEntity>
    ): PixabayRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysByQuery(query)
            }
    }
}
