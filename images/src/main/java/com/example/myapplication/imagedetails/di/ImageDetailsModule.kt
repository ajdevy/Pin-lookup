package com.example.myapplication.imagedetails.di

import com.example.myapplication.imagedetails.data.PixabayImageRepository
import com.example.myapplication.imagedetails.data.PixabayImageRepositoryImpl
import com.example.myapplication.imagedetails.domain.GetPixabayImageUseCase
import com.example.myapplication.imagedetails.ui.ImageDetailsViewModel
import com.example.myapplication.pixabay.data.PixabayDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val imageDetailsModule = module {

    single { get<PixabayDatabase>().pixabayImageDao() }

    single { PixabayImageRepositoryImpl(get()) as PixabayImageRepository }

    factory { GetPixabayImageUseCase(get()) }

    viewModel { (imageId: Long) -> ImageDetailsViewModel(imageId, get()) }

}