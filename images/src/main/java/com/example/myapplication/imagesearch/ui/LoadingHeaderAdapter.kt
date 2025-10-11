package com.example.myapplication.imagesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.imagesearch.R
import timber.log.Timber

class LoadingHeaderAdapter(
    private val onRetryClick: () -> Unit
) : RecyclerView.Adapter<LoadingHeaderViewHolder>() {
    
    private var loadState: LoadState = LoadState.NotLoading(false)
    
    fun updateLoadState(newLoadState: LoadState) {
        val oldLoadState = loadState
        loadState = newLoadState
        
        Timber.d("LoadingHeaderAdapter load state changed from $oldLoadState to $newLoadState")
        
        if (oldLoadState != newLoadState) {
            notifyDataSetChanged()
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingHeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading_header, parent, false)
        return LoadingHeaderViewHolder(view, onRetryClick)
    }
    
    override fun onBindViewHolder(holder: LoadingHeaderViewHolder, position: Int) {
        holder.bind(loadState)
    }
    
    override fun getItemCount(): Int {
        return if (shouldShowHeader()) 1 else 0
    }
    
    private fun shouldShowHeader(): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error
    }
}
