package com.example.myapplication.imagedetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.app.ui.UiState
import com.example.myapplication.imagedetails.domain.GetPixabayImageUseCase
import com.example.myapplication.pixabay.data.PixabayImageEntity
import com.example.myapplication.pixabay.domain.PixabayImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class ImageDetailsViewModel(
    private val imageId: Long,
    private val imageUseCase: GetPixabayImageUseCase,
) : ViewModel() {
    val currentImage: StateFlow<UiState<PixabayImage>> = flow {
        emit(
            imageUseCase.invoke(id = imageId)
                ?.let {
                    UiState.Success(it)
                }
                ?: UiState.Error("Image not found")
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
}
