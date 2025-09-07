package com.example.myapplication.app.di

import com.example.myapplication.BuildConfig
import com.example.myapplication.pixabay.data.PixabayApi
import com.example.myapplication.pixabay.data.PixabayRepository
import com.example.myapplication.pixabay.data.PixabayRepositoryImpl
import com.example.myapplication.pixabay.domain.SearchImagesUseCase
import com.example.myapplication.app.ui.imagesearch.ImageSearchViewModel
import com.example.myapplication.pixabay.data.PixabayDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // Pixabay
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
                .build()
            val request = original.newBuilder().url(newUrl).build()
            chain.proceed(request)
        }
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.PIXABAY_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(PixabayApi::class.java)
    }

    single { PixabayRepositoryImpl(get()) } bind PixabayRepository::class
    
    single { SearchImagesUseCase(get()) }
    
    // Database
    single { PixabayDatabase.create(androidContext()) }
    
    // ViewModels
    viewModel { ImageSearchViewModel(get(), get()) }
}