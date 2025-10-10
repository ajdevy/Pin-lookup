package com.example.myapplication.app.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value

sealed interface Child

@OptIn(DelicateDecomposeApi::class)
class DefaultNavigationComponent(
    componentContext: ComponentContext,
) : NavigationComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<NavigationConfiguration>()

    override val stack: Value<ChildStack<NavigationConfiguration, Child>> = childStack(
        source = navigation,
        initialConfiguration = NavigationConfiguration.ImageSearch,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = NavigationConfigurationSerializer,
    )

    private fun createChild(config: NavigationConfiguration, context: ComponentContext): Child =
        when (config) {
            NavigationConfiguration.ImageSearch -> HomeChild(context)
            is NavigationConfiguration.ImageDetails -> DetailsChild(context, config.id)
        }

    override fun onNavigateToImageDetails(id: String) {
        navigation.push(NavigationConfiguration.ImageDetails(id))
    }

    override fun onNavigateBack() {
        navigation.pop()
    }
}

// Child components
class HomeChild(componentContext: ComponentContext) : Child, ComponentContext by componentContext
class DetailsChild(componentContext: ComponentContext, val id: String) : Child, ComponentContext by componentContext
