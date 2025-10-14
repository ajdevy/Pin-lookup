package com.flexsentlabs.myapplication.imagesearch.di

import com.flexsentlabs.myapplication.imagesearch.BuildConfig
import com.flexsentlabs.myapplication.database.pixabay.PixabayApi
import com.flexsentlabs.myapplication.database.pixabay.PixabayDatabase
import com.flexsentlabs.myapplication.database.pixabay.PixabaySearchRepository
import com.flexsentlabs.myapplication.database.pixabay.PixabaySearchRepositoryImpl
import com.flexsentlabs.myapplication.pixabay.domain.SearchImagesUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

}