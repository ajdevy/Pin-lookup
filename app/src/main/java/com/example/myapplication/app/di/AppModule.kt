package com.example.myapplication.app.di

import com.example.myapplication.BuildConfig
import com.example.myapplication.app.data.PinterestApi
import com.example.myapplication.app.data.PinterestRepository
import com.example.myapplication.app.data.PinterestRepositoryImpl
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        Moshi.Builder().build()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val authInterceptor = Interceptor { chain ->
            val token = BuildConfig.PINTEREST_ACCESS_TOKEN
            val request = chain.request().newBuilder().apply {
                if (token.isNotEmpty()) {
                    header("Authorization", "Bearer $token")
                }
            }.build()
            chain.proceed(request)
        }
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.PINTEREST_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(PinterestApi::class.java)
    }

    single { PinterestRepositoryImpl(get()) } bind PinterestRepository::class
}