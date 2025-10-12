package com.flexsentlabs.myapplication.tasks.di

import com.flexsentlabs.myapplication.tasks.ui.TasksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val tasksModule = module {
    viewModel { TasksViewModel() }
}