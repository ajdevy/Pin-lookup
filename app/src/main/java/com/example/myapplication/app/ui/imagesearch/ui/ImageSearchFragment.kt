package com.example.myapplication.app.ui.imagesearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentImageSearchBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageSearchFragment : Fragment() {

    private val viewModel: ImageSearchViewModel by viewModel()
    private var _binding: FragmentImageSearchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adapter: PixabayImageAdapter

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
        adapter = PixabayImageAdapter()
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvImages.adapter = adapter
        
        // Add load state listener
        adapter.addLoadStateListener { loadState ->
            when (loadState.append) {
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                }
            }
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
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}