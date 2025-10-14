package com.flexsentlabs.myapplication.imagesearch.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchScreen(modifier: Modifier = Modifier) {
    Surface {
        Scaffold(
            topBar = {
                SearchBar(
                    query = "",
                    onQueryChange = { },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    modifier = Modifier
                ) {
                    // SearchBar content
                }
            },
            content = { paddingValues ->
                ImageList(
                    modifier = Modifier.padding(paddingValues),
                    items = emptyList()
                )
            }
        )
    }
}

@Composable
fun ImageList(modifier: Modifier = Modifier, items: List<PixabayImage>) {
    LazyColumn(modifier = modifier) {
        items(
            items = items,
            key = { item -> item.id }
        ) { item ->
            // TODO: Add item content
        }
    }
}
