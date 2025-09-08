package com.example.myapplication.app.ui.imagesearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentImageSearchBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageSearchFragment : Fragment() {

    private val viewModel: ImageSearchViewModel by viewModel()
    private var _binding: FragmentImageSearchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var imageAdapter: PixabayImageAdapter
    private lateinit var footerAdapter: LoadingFooterAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        imageAdapter = PixabayImageAdapter()
        footerAdapter = LoadingFooterAdapter {
            // Retry loading more items
            imageAdapter.retry()
        }
        concatAdapter = ConcatAdapter(imageAdapter, footerAdapter)
        
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvImages.adapter = concatAdapter
        
        // Add load state listener to update footer
        imageAdapter.addLoadStateListener { loadState ->
            footerAdapter.updateLoadState(loadState.append)
        }
    }

    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text?.toString() ?: ""
            if (query.isNotBlank()) {
                viewModel.searchImages(query)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.pagingData.collectLatest { pagingData ->
                imageAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}