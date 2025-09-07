package com.example.myapplication.app.ui.imagesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.app.ui.theme.MyApplicationTheme
import com.example.myapplication.pixabay.data.PixabayImageEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageSearchFragment : Fragment() {

    private val viewModel: ImageSearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme {
                    ImageSearchScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchScreen(viewModel: ImageSearchViewModel) {
    val pagingDataFlow by viewModel.pagingData.collectAsStateWithLifecycle()
    
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search images") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.searchImages(searchText) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        pagingDataFlow?.let { flow ->
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> 
                        val image = lazyPagingItems[index]
                        if (image != null) {
                            "${image.id}_${index}"
                        } else {
                            "placeholder_$index"
                        }
                    }
                ) { index ->
                    val image = lazyPagingItems[index]
                    image?.let { 
                        ImageItem(image = it)
                    }
                }
                
                when (lazyPagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            Text(
                                text = "Error loading more items",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
// TODO: remove entity from fragment, use domain class
fun ImageItem(image: PixabayImageEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "ID: ${image.id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "User: ${image.userName ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tags: ${image.tags}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}