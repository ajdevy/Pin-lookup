package com.flexsentlabs.myapplication.database.pixabay

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.flexsentlabs.myapplication.pixabay.domain.SearchImagesUseCase
import timber.log.Timber

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
        Timber.d("PixabayRemoteMediator initialized for query: $query")
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PixabayImageEntity>
    ): MediatorResult {
        Timber.d("Loading data for query: $query, loadType: $loadType")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    val pageNum = remoteKeys?.nextKey?.minus(1) ?: 1
                    Timber.d("REFRESH: Loading page $pageNum")
                    pageNum
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    if (prevKey == null) {
                        Timber.d("PREPEND: No more pages to load, end of pagination reached")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Timber.d("PREPEND: Loading page $prevKey")
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    if (nextKey == null) {
                        Timber.d("APPEND: No more pages to load, end of pagination reached")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Timber.d("APPEND: Loading page $nextKey")
                    nextKey
                }
            }

            Timber.d("Fetching images from API for query: $query, page: $page")
            val result = searchUseCase(query = query, page = page, perPage = 20)
            val images = result.images.map { it.toEntity(query, page) }
            Timber.d("Successfully fetched ${images.size} images from API")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Timber.d("Clearing existing data for query: $query")
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
                Timber.d("Successfully saved ${images.size} images to database")
            }

            val endOfPagination = result.images.isEmpty()
            Timber.d("Load completed successfully. End of pagination: $endOfPagination")
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            Timber.e(e, "Error loading data for query: $query, loadType: $loadType")
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
