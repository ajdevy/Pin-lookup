package com.flexsentlabs.myapplication.imagedetails.di

import com.flexsentlabs.myapplication.imagedetails.data.PixabayImageRepository
import com.flexsentlabs.myapplication.imagedetails.data.PixabayImageRepositoryImpl
import com.flexsentlabs.myapplication.imagedetails.domain.GetPixabayImageUseCase
import com.flexsentlabs.myapplication.imagedetails.ui.ImageDetailsViewModel
import com.flexsentlabs.myapplication.pixabay.data.PixabayDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val imageDetailsModule = module {

    single { get<PixabayDatabase>().pixabayImageDao() }

    single { PixabayImageRepositoryImpl(get()) as PixabayImageRepository }

    factory { GetPixabayImageUseCase(get()) }

    viewModel { (imageId: Long) -> ImageDetailsViewModel(imageId, get()) }

}