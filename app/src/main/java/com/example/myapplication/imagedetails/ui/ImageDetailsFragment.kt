package com.example.myapplication.imagedetails.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ImageDetailsFragment : Fragment() {

    private val imageId: Long by lazy {
        arguments?.getLong("imageId") ?: throw IllegalArgumentException("imageId is required")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val viewModel: ImageDetailsViewModel = koinViewModel { parametersOf(imageId) }
                    ImageDetails(viewModel)
                }
            }
        }
    }

    companion object {
        fun newInstance() = ImageDetailsFragment()
    }
}