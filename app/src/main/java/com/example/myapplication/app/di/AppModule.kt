package com.example.myapplication.app.di

import com.arkivanov.decompose.ComponentContext
import com.example.myapplication.BuildConfig
import com.example.myapplication.app.ui.navigation.DefaultNavigationComponent
import com.example.myapplication.imagedetails.di.imageDetailsModule
import com.example.myapplication.imagesearch.di.imageSearchModule
import com.example.myapplication.imagesearch.ui.ImageSearchViewModel
import com.example.myapplication.tasks.di.tasksModule
import org.koin.core.module.dsl.viewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val appModule = module {

    includes(
        imageDetailsModule,
        tasksModule,
        imageSearchModule
    )

    // navigation
    factory { (componentContext: ComponentContext) ->
        DefaultNavigationComponent(componentContext)
    }

    // ViewModels
    viewModel { ImageSearchViewModel(get(), get()) }
}