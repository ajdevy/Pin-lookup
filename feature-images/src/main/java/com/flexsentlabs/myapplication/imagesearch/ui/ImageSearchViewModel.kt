package com.flexsentlabs.myapplication.imagesearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flexsentlabs.myapplication.imagesearch.domain.SearchImagesUseCase
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ImageSearchViewModel(
    private val searchUseCase: SearchImagesUseCase
) : ViewModel(), KoinComponent {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private var debounceJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData: Flow<PagingData<PixabayImage>> = _searchQuery
        .flatMapLatest { query ->
            Timber.d("Search query changed in pagingData: '$query' (length: ${query.length})")
            if (query.isBlank()) {
                Timber.d("Empty query, returning empty paging data")
                // Force a new empty PagingData to ensure UI updates
                flowOf(PagingData.empty<PixabayImage>())
            } else {
                Timber.d("Getting pager for query: '$query'")
                try {
                    val pager = get<Pager<Int, PixabayImage>> { parametersOf(query) }
                    Timber.d("Pager created successfully for query: '$query'")
                    pager.flow
                } catch (e: Exception) {
                    Timber.e(e, "Error creating pager for query: '$query'")
                    flowOf(PagingData.empty<PixabayImage>())
                }
            }
        }

    fun searchImages(query: String) {
        Timber.d("searchImages called with query: '$query' (length: ${query.length})")
        _searchQuery.value = query
        Timber.d("Search query updated to: '${_searchQuery.value}' (length: ${_searchQuery.value.length})")
    }
    
    fun clearSearch() {
        Timber.d("Clearing search results")
        _searchQuery.value = ""
    }
    
    fun searchImagesDebounced(query: String, delayMs: Long = 500L) {
        Timber.d("searchImagesDebounced called with query: '$query' (length: ${query.length})")
        
        // Cancel previous debounce job
        debounceJob?.cancel()
        
        // Start new debounce job
        debounceJob = viewModelScope.launch {
            delay(delayMs)
            if (query.isNotBlank()) {
                Timber.d("Executing debounced search for query: '$query'")
                searchImages(query)
            } else {
                Timber.d("Query is blank, clearing search results")
                clearSearch() // Use explicit clear method
            }
        }
    }
}