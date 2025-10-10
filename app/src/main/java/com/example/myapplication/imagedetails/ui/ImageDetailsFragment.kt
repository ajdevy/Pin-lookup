package com.example.myapplication.imagedetails.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.example.myapplication.R
import com.example.myapplication.app.ui.navigation.NavigationComponent

class ImageDetailsFragment : Fragment() {

    private val imageId: String by lazy {
        arguments?.getString("imageId") ?: throw IllegalArgumentException("imageId is required")
    }

    private val viewModel: ImageDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    PlantDetailDescription(viewModel)
                }
            }
        }
    }

    companion object {
        fun newInstance() = ImageDetailsFragment()
    }
}