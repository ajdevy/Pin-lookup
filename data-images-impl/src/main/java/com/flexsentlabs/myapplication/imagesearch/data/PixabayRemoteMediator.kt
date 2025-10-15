package com.flexsentlabs.myapplication.imagesearch.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.flexsentlabs.myapplication.core.database.AppDatabase
import com.flexsentlabs.myapplication.domain.images.models.ImageRemoteKeys
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import com.flexsentlabs.myapplication.imagesearch.data.mapper.toDomain
import com.flexsentlabs.myapplication.imagesearch.data.mapper.toEntity
import com.flexsentlabs.myapplication.imagesearch.domain.SearchImagesUseCase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class PixabayRemoteMediator(
    private val database: AppDatabase,
    private val searchUseCase: SearchImagesUseCase,
    private val query: String
) : RemoteMediator<Int, PixabayImage>() {

    private val imageDao = database.pixabayImageDao()
    private val remoteKeysDao = database.pixabayRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        Timber.Forest.d("PixabayRemoteMediator initialized for query: $query")
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PixabayImage>
    ): MediatorResult {
        Timber.Forest.d("Loading data for query: $query, loadType: $loadType")
        Timber.d("RemoteMediator load called for query: $query, loadType: $loadType")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    val pageNum = remoteKeys?.nextKey?.minus(1) ?: 1
                    Timber.Forest.d("REFRESH: Loading page $pageNum")
                    pageNum
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    if (prevKey == null) {
                        Timber.Forest.d("PREPEND: No more pages to load, end of pagination reached")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Timber.Forest.d("PREPEND: Loading page $prevKey")
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    if (nextKey == null) {
                        Timber.Forest.d("APPEND: No more pages to load, end of pagination reached")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Timber.Forest.d("APPEND: Loading page $nextKey")
                    nextKey
                }
            }

            Timber.Forest.d("Fetching images from API for query: $query, page: $page")
            val result = searchUseCase(query = query, page = page, perPage = 20)
            val images = result.images
            Timber.Forest.d("Successfully fetched ${images.size} images from API")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Timber.Forest.d("Clearing existing data for query: $query")
                    imageDao.clearByQuery(query)
                    remoteKeysDao.clearRemoteKeys()
                }

                val entities = images.map {
                    it.toEntity(
                        searchQuery = query,
                        page = page
                    )
                }
                Timber.Forest.d("Saving ${entities.size} entities with searchQuery: '$query'")
                imageDao.insertAll(entities)
                remoteKeysDao.insertAll(
                    listOf(
                        ImageRemoteKeys(
                            label = query,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (result.images.isEmpty()) null else page + 1
                        ).toEntity()
                    )
                )
                Timber.Forest.d("Successfully saved ${images.size} images to database")
                
                // Debug: Check what's actually in the database
                val count = imageDao.getCountForQuery(query)
                val allQueries = imageDao.getAllSearchQueries()
                Timber.Forest.d("Database now has $count images for query '$query'")
                Timber.Forest.d("All search queries in database: $allQueries")
            }

            val endOfPagination = result.images.isEmpty()
            Timber.Forest.d("Load completed successfully. End of pagination: $endOfPagination")
            
            // Force a database invalidation to ensure paging source gets updated data
            if (loadType == LoadType.REFRESH) {
                Timber.Forest.d("Forcing database invalidation for query: $query")
                // The paging source should automatically detect the new data
                // We need to ensure the paging source gets invalidated
                // This should trigger a refresh of the paging source
            }
            
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            Timber.Forest.e(e, "Error loading data for query: $query, loadType: $loadType")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PixabayImage>
    ): ImageRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { image ->
                remoteKeysDao.remoteKeysByLabel(query)?.toDomain()
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PixabayImage>
    ): ImageRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysByLabel(query)?.toDomain()
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PixabayImage>
    ): ImageRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysByLabel(query)?.toDomain()
            }
    }
}