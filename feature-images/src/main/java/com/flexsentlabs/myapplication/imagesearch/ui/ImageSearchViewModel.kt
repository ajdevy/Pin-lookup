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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData: Flow<PagingData<PixabayImage>> = _searchQuery
        .flatMapLatest { query ->
            Timber.d("Search query changed: '$query'")
            if (query.isBlank()) {
                Timber.d("Empty query, returning empty paging data")
                flowOf(PagingData.empty())
            } else {
                Timber.d("Getting pager for query: '$query'")
                try {
                    val pager = get<Pager<Int, PixabayImage>> { parametersOf(query) }
                    Timber.d("Pager created successfully for query: '$query'")
                    pager.flow.cachedIn(viewModelScope)
                } catch (e: Exception) {
                    Timber.e(e, "Error creating pager for query: '$query'")
                    flowOf(PagingData.empty())
                }
            }
        }

    fun searchImages(query: String) {
        Timber.d("searchImages called with query: '$query' (length: ${query.length})")
        _searchQuery.value = query
        Timber.d("Search query updated to: '${_searchQuery.value}' (length: ${_searchQuery.value.length})")
    }
}