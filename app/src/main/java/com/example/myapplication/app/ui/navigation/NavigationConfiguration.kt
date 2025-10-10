package com.example.myapplication.app.ui.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

// Define your screen configurations
@Serializable
sealed class NavigationConfiguration {
    @Serializable
    data object ImageSearch : NavigationConfiguration()

    @Serializable
    data class ImageDetails(val id: String) : NavigationConfiguration()
}

val NavigationConfigurationSerializer = Json.serializersModule.serializer<NavigationConfiguration>()
