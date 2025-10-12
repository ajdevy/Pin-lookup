package com.example.myapplication.app.di

import com.arkivanov.decompose.ComponentContext
import com.example.myapplication.app.ui.navigation.DefaultNavigationComponent
import com.example.myapplication.imagedetails.di.imageDetailsModule
import com.example.myapplication.imagesearch.di.imageSearchModule
import com.example.myapplication.imagesearch.ui.ImageSearchViewModel
import com.example.myapplication.tasks.di.tasksModule
import org.koin.core.module.dsl.viewModel
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