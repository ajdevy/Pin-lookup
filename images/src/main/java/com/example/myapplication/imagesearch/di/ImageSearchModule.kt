package com.example.myapplication.imagesearch.di

import com.example.myapplication.imagesearch.BuildConfig
import com.example.myapplication.pixabay.data.PixabayApi
import com.example.myapplication.pixabay.data.PixabayDatabase
import com.example.myapplication.pixabay.data.PixabaySearchRepository
import com.example.myapplication.pixabay.data.PixabaySearchRepositoryImpl
import com.example.myapplication.pixabay.domain.SearchImagesUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val imageSearchModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.PIXABAY_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(PixabayApi::class.java)
    }

    single { PixabaySearchRepositoryImpl(get()) } bind PixabaySearchRepository::class

    single { SearchImagesUseCase(get()) }

    // Database
    single { PixabayDatabase.create(androidContext()) }
}