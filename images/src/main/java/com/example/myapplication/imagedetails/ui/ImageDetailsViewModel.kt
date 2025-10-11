package com.example.myapplication.imagedetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.UiState
import com.example.myapplication.imagedetails.domain.GetPixabayImageUseCase
import com.example.myapplication.pixabay.domain.PixabayImage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
