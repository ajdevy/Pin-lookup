package com.example.myapplication.app.ui.imagesearch

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
import com.example.myapplication.pixabay.domain.PixabayImage
import com.example.myapplication.pixabay.domain.SearchImagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalPagingApi::class)
class ImageSearchViewModel(
    private val searchUseCase: SearchImagesUseCase,
    private val database: PixabayDatabase
) : ViewModel() {

    private val _pagingData = MutableStateFlow<Flow<PagingData<PixabayImageEntity>>?>(null)
    // TODO: remove ? nullability
    val pagingData: StateFlow<Flow<PagingData<PixabayImageEntity>>?> = _pagingData.asStateFlow()

    fun searchImages(query: String) {
        if (query.isBlank()) return
        
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
        
        _pagingData.value = pager.flow.cachedIn(viewModelScope)
    }
}