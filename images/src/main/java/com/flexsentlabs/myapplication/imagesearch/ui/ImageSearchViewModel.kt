package com.flexsentlabs.myapplication.imagesearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flexsentlabs.myapplication.database.pixabay.PixabayDatabase
import com.flexsentlabs.myapplication.database.pixabay.PixabayImageEntity
import com.flexsentlabs.myapplication.database.pixabay.PixabayRemoteMediator
import com.flexsentlabs.myapplication.pixabay.domain.SearchImagesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ImageSearchViewModel(
    private val searchUseCase: SearchImagesUseCase,
    private val database: PixabayDatabase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData: Flow<PagingData<PixabayImageEntity>> = _searchQuery
        .flatMapLatest { query ->
            Timber.d("Search query changed: '$query'")
            if (query.isBlank()) {
                Timber.d("Empty query, returning empty paging data")
                flowOf(PagingData.empty())
            } else {
                Timber.d("Creating pager for query: '$query'")
                val pager = Pager(
                    config = PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false
                    ),
                    remoteMediator = PixabayRemoteMediator(
                        database = database,
                        searchUseCase = searchUseCase,
                        query = query
                    ),
                    pagingSourceFactory = { 
                        Timber.d("Creating paging source for query: '$query'")
                        database.pixabayImageDao().pagingSource(query)
                    }
                )
                pager.flow.cachedIn(viewModelScope)
            }
        }

    fun searchImages(query: String) {
        Timber.d("searchImages called with query: '$query'")
        _searchQuery.value = query
    }
}