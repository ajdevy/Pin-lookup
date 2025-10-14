package com.flexsentlabs.myapplication.app.di

import com.arkivanov.decompose.ComponentContext
import com.flexsentlabs.myapplication.app.ui.navigation.DefaultNavigationComponent
import com.flexsentlabs.myapplication.core.database.di.databaseModule
import com.flexsentlabs.myapplication.imagedetails.di.imageDetailsModule
import com.flexsentlabs.myapplication.imagesearch.di.imageSearchModule
import com.flexsentlabs.myapplication.imagesearch.ui.ImageSearchViewModel
import com.flexsentlabs.myapplication.tasks.di.tasksModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    includes(
        databaseModule,
        imageDetailsModule,
        tasksModule,
        imageSearchModule
    )

    // navigation
    factory { (componentContext: ComponentContext) ->
        DefaultNavigationComponent(componentContext)
    }

    // ViewModels
    viewModel { ImageSearchViewModel(get()) }
}