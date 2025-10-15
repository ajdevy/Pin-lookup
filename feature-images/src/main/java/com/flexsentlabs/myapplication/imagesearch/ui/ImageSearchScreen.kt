package com.flexsentlabs.myapplication.imagesearch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
// import coil.compose.AsyncImage
import com.flexsentlabs.myapplication.domain.images.models.PixabayImage
import com.flexsentlabs.myapplication.imagesearch.BuildConfig
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageSearchViewModel
) {
    var searchQuery by remember { mutableStateOf("") } // Start with empty query
    var isSearchBarExpanded by remember { mutableStateOf(false) }
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    // Debounced search effect
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchImagesDebounced(searchQuery)
        }
    }

    // Trigger initial search
    LaunchedEffect(Unit) {
        val query = if (BuildConfig.DEBUG) "nature" else ""
        Timber.d( "Triggering initial search with query: '$query'")
        searchQuery = query // Update the UI state first
    }

    Surface {
        Scaffold(
            modifier = modifier.padding(16.dp),
            topBar = {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { query ->
                                searchQuery = query
                            },
                            onSearch = { _ ->
                                isSearchBarExpanded = false
                            },
                            expanded = isSearchBarExpanded,
                            onExpandedChange = { isSearchBarExpanded = it },
                            placeholder = { Text("Search images...") },
                            leadingIcon = {
                                // Search icon is handled by SearchBarDefaults
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    // Clear button could be added here
                                }
                            }
                        )
                    },
                    expanded = isSearchBarExpanded,
                    onExpandedChange = { isSearchBarExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Search suggestions or recent searches could go here
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Start typing to search for images",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            },
            content = { paddingValues ->
                ImageList(
                    modifier = Modifier.padding(paddingValues),
                    pagingItems = pagingItems
                )
            }
        )
    }
}

@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<PixabayImage>
) {
    // Debug information
    val loadState = pagingItems.loadState
    val itemCount = pagingItems.itemCount

    // Debug logging
    Timber.d("Item count: $itemCount, Load state: $loadState")

    // Show initial loading state
    if (loadState.refresh is LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Show error state for initial load
    if (loadState.refresh is LoadState.Error) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error loading images: ${(loadState.refresh as LoadState.Error).error.message}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No images found. Try searching for something else.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->
            val item = pagingItems[index]
            Timber.d("Item at index $index: $item")
            item?.let { image ->
                ImageItem(
                    image = image,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Handle loading states
        when (pagingItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Text(
                        text = "Error loading images",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun ImageItem(
    image: PixabayImage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Image placeholder - replace with AsyncImage when Coil is properly configured
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Image",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User info and tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "By ${image.userName ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Tags
            if (image.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = image.tags.take(3).joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun ImageItemPreview() {
    ImageItem(
        image = PixabayImage(
            id = 1L,
            previewUrl = "https://picsum.photos/300/200?random=1",
            largeImageUrl = "https://picsum.photos/800/600?random=1",
            tags = listOf("nature", "landscape", "mountain"),
            webformatUrl = "https://picsum.photos/400/300?random=1",
            userName = "Photographer Name"
        )
    )
}