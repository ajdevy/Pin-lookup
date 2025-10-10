package com.example.myapplication.app.ui.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

// Create a component that manages navigation
interface NavigationComponent {
    val stack: Value<ChildStack<NavigationConfiguration, Child>>

    fun onNavigateToImageDetails(id: String)
    fun onNavigateBack()
}