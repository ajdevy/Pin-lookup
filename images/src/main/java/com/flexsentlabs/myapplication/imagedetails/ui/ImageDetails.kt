package com.flexsentlabs.myapplication.imagedetails.ui

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.flexsentlabs.myapplication.common.R
import com.flexsentlabs.myapplication.common.UiState
import com.flexsentlabs.myapplication.pixabay.domain.PixabayImage
import timber.log.Timber

@Composable
fun ImageDetails(viewModel: ImageDetailsViewModel) {

    val currentState by viewModel.currentImage.collectAsState()

    when (currentState) {
        is UiState.Error -> Timber.d("Got error ${(currentState as UiState.Error).message}")
        is UiState.Loading -> Timber.d("Loading")
        is UiState.Success -> {
            val image = (currentState as UiState.Success).data
            Timber.d("Got data $image")
            ImageDetails(image)
        }
    }
}

@Composable
private fun ImageDetails(image: PixabayImage) {
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.margin_medium))
        ) {
            image.userName?.let { PixabayUsername(it) }
            PixabayImageTags(image.tags)
        }
    }
}

@Composable
fun PixabayUsername(username: String) {
    Text(
        text = stringResource(R.string.image_details_by, username),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun PixabayImageTags(tags: List<String>) {
    Text(
        text = tags.joinToString(", "),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Preview
@Composable
fun ImageDetailsPreview() {
    MaterialTheme {
        val fakeImageDetails = PixabayImage(
            id = 1,
            userName = "username",
            tags = listOf("flower", "nature", "yellow", "spring", "garden", "blossom", "floral", "botany"),
            previewUrl = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_150.jpg",
            webformatUrl = "https://pixabay.com/get/g6f0d7c3f3e2b4f6a5e8c3d1a9)",
            largeImageUrl = "https://pixabay.com/get/g6f0d7c3f3e2b4f6a5e8c3d1a9)"
        )
        ImageDetails(fakeImageDetails)
    }
}

@Preview
@Composable
fun PixabayUsernamePreview() {
    MaterialTheme {
        PixabayUsername("username")
    }
}

@Preview
@Composable
fun PixableImageTagsPreview() {
    MaterialTheme {
        PixabayImageTags(listOf("flower", "nature", "yellow"))
    }
}