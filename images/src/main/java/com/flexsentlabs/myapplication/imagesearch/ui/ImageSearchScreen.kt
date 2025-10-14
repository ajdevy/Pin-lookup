package com.flexsentlabs.myapplication.imagesearch.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flexsentlabs.myapplication.imagedetails.data.PixabayImageRepositoryImpl
import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage

@Composable
fun ImageSearchScreen(modifier: Modifier = Modifier) {

    Surface {
        Scaffold {
            topContent = { SearchBar() },
            content = { paddingValues ->
                ImageList()
            }
        }
    }
}

@Composable
fun ImageList(modifier: Modifier = Modifier, items:List<PixabayImage>) {
    LazyColumn {
        items(items) {

        }
    }
}
