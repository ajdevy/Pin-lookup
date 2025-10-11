package com.example.myapplication.tasks.di

import com.example.myapplication.tasks.ui.TasksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val tasksModule = module {
    viewModel { TasksViewModel() }
}