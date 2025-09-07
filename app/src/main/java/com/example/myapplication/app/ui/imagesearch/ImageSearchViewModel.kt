package com.example.myapplication.app.ui.imagesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplication.pixabay.data.PixabayDatabase
import com.example.myapplication.pixabay.data.PixabayRemoteMediator
import com.example.myapplication.pixabay.data.toDomain
import com.example.myapplication.pixabay.domain.PixabayImage
import com.example.myapplication.pixabay.domain.SearchImagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageSearchViewModel(
    private val searchUseCase: SearchImagesUseCase,
    private val database: PixabayDatabase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _pagingData = MutableStateFlow<Flow<PagingData<PixabayImage>>?>(null)
    val pagingData: StateFlow<Flow<PagingData<PixabayImage>>?> = _pagingData.asStateFlow()

    fun searchImages(query: String) {
        if (query.isBlank()) return
        
        _searchQuery.value = query
        
        val pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PixabayRemoteMediator(
                searchUseCase = searchUseCase,
                query = query,
                database = database
            ),
            pagingSourceFactory = { database.pixabayImageDao().pagingSource() }
        )
        
        _pagingData.value = pager.flow
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { entity -> entity.toDomain() }
            }
    }
}