package com.flexsentlabs.myapplication.imagedetails.di

import com.flexsentlabs.myapplication.core.database.AppDatabase
import com.flexsentlabs.myapplication.data.images.ImageRepository
import com.flexsentlabs.myapplication.imagedetails.data.ImageRepositoryImpl
import com.flexsentlabs.myapplication.imagedetails.domain.GetImageUseCase
import com.flexsentlabs.myapplication.imagedetails.ui.ImageDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val imageDetailsModule = module {

    single { get<AppDatabase>().pixabayImageDao() }

    single<ImageRepository> { ImageRepositoryImpl(get()) }

    factory { GetImageUseCase(get()) }

    viewModel { (imageId: Long) -> ImageDetailsViewModel(imageId, get()) }

}