package com.example.myapplication.imagedetails.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.R

@Composable
fun PlantDetailDescription(viewModel: ImageDetailsViewModel) {

    val currentImage = viewModel.currentImage.collectAsState()

    Surface {
        Text(
            text = "Hello Compose",
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.margin_small))
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
//            style = MaterialTheme.typography.h5
        )
    }
}

//@Preview
//@Composable
//fun PlantDetailDescriptionPreview() {
//    PlantDetailDescriptio
//}