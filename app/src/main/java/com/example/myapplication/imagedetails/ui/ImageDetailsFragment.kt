package com.example.myapplication.imagedetails.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import org.koin.androidx.compose.koinViewModel

class ImageDetailsFragment : Fragment() {

    private val imageId: String by lazy {
        arguments?.getString("imageId") ?: throw IllegalArgumentException("imageId is required")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val viewModel: ImageDetailsViewModel = koinViewModel()
                    PlantDetailDescription(viewModel)
                }
            }
        }
    }

    companion object {
        fun newInstance() = ImageDetailsFragment()
    }
}