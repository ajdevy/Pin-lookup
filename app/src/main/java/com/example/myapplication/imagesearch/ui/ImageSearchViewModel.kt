package com.example.myapplication.imagesearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.pixabay.data.PixabayDatabase
import com.example.myapplication.pixabay.data.PixabayImageEntity
import com.example.myapplication.pixabay.data.PixabayRemoteMediator
import com.example.myapplication.pixabay.domain.SearchImagesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

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
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
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
                        database.pixabayImageDao().pagingSource(query)
                    }
                )
                pager.flow.cachedIn(viewModelScope)
            }
        }

    fun searchImages(query: String) {
        _searchQuery.value = query
    }
}