package com.flexsentlabs.myapplication.imagesearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexsentlabs.myapplication.imagesearch.databinding.FragmentImageSearchBinding
import com.flexsentlabs.myapplication.database.pixabay.PixabayImageEntity
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ImageSearchFragment : Fragment() {

    private val viewModel: ImageSearchViewModel by viewModel()
    private var _binding: FragmentImageSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: PixabayImageAdapter
    private lateinit var headerAdapter: LoadingHeaderAdapter
    private lateinit var footerAdapter: LoadingFooterAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("ImageSearchFragment onCreateView")
        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("ImageSearchFragment onViewCreated")

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Timber.d("Setting up RecyclerView")
        imageAdapter = PixabayImageAdapter(object : ItemClickListener {
            override fun onItemClicked(item: PixabayImageEntity) {
                Timber.d("Image clicked: ${item.id}")
                // Handle image click, navigate to details screen
                // TODO: Implement navigation callback or interface for cross-module navigation
                // ImageSearchFragmentDirections
                //     .actionImageSearchToImageDetails(item.id)
                //     .let { findNavController().navigate(it) }
            }
        })
        headerAdapter = LoadingHeaderAdapter {
            // Retry initial load
            Timber.d("Retrying initial load")
            imageAdapter.retry()
        }
        footerAdapter = LoadingFooterAdapter {
            // Retry loading more items
            Timber.d("Retrying load more items")
            imageAdapter.retry()
        }
        concatAdapter = ConcatAdapter(headerAdapter, imageAdapter, footerAdapter)

        binding.displayImagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.displayImagesRecyclerView.adapter = concatAdapter

        // Add load state listener to update header and footer loading states
        imageAdapter.addLoadStateListener { loadState ->
            Timber.d("Load state changed: refresh=${loadState.refresh}, append=${loadState.append}")

            // Update header for refresh states (initial load)
            headerAdapter.updateLoadState(loadState.refresh)

            // Update footer for append states (loading more)
            footerAdapter.updateLoadState(loadState.append)

            // Show error toast for refresh errors (initial load failures)
            if (loadState.refresh is LoadState.Error) {
                val errorMessage = "Failed to load images. Please check your internet connection and try again."
                Timber.e("Refresh error: ${(loadState.refresh as LoadState.Error).error}")
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }

            // Show error toast for append errors (loading more items failures)
            if (loadState.append is LoadState.Error) {
                val errorMessage = "Failed to load more images. Please try again."
                Timber.e("Append error: ${(loadState.append as LoadState.Error).error}")
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.performSearchButton.setOnClickListener {
            val query = binding.enterSearchQueryEditText.text?.toString() ?: ""
            if (query.isNotBlank()) {
                Timber.d("Search button clicked with query: $query")
                viewModel.searchImages(query)
            } else {
                Timber.w("Search button clicked with empty query")
            }
        }
    }

    private fun observeViewModel() {
        Timber.d("Starting to observe ViewModel paging data")
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.pagingData.collectLatest { pagingData ->
                Timber.d("Received new paging data, submitting to adapter")
                imageAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("ImageSearchFragment onDestroyView")
        _binding = null
    }
}