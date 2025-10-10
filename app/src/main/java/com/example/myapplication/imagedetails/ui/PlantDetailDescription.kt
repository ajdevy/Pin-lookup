package com.example.myapplication.imagedetails.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.R
import com.example.myapplication.app.ui.UiState
import com.example.myapplication.pixabay.domain.PixabayImage
import timber.log.Timber

@Composable
fun PlantDetailDescription(viewModel: ImageDetailsViewModel) {

    val currentState by viewModel.currentImage.collectAsState()

    when (currentState) {
        is UiState.Error -> Timber.d("Got error ${(currentState as UiState.Error).message}")
        is UiState.Loading -> Timber.d("Loading")
        is UiState.Success -> {
            val image = (currentState as UiState.Success).data
            Timber.d("Got data $image")
            PlantDetailDescription(image)
        }
    }
}

@Composable
private fun PlantDetailDescription(image: PixabayImage) {
    Surface {
        image.largeImageUrl?.let {
        }

        PixableImageTags(image.tags)
    }
}

@Composable
fun PixableImageTags(tags: List<String>) {
    Text(
        text = tags.joinToString(", "),
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview
@Composable
fun PlantDetailDescriptionPreview() {
    MaterialTheme {
        PixableImageTags(listOf("tag1", "tag2", "tag3") )
    }
}
