package com.example.myapplication.imagesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class LoadingFooterAdapter(
    private val onRetryClick: () -> Unit
) : RecyclerView.Adapter<LoadingFooterViewHolder>() {
    
    private var loadState: LoadState = LoadState.NotLoading(false)
    
    fun updateLoadState(newLoadState: LoadState) {
        val oldLoadState = loadState
        loadState = newLoadState
        
        if (oldLoadState != newLoadState) {
            notifyDataSetChanged()
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingFooterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading_footer, parent, false)
        return LoadingFooterViewHolder(view, onRetryClick)
    }
    
    override fun onBindViewHolder(holder: LoadingFooterViewHolder, position: Int) {
        holder.bind(loadState)
    }
    
    override fun getItemCount(): Int {
        return if (shouldShowFooter()) 1 else 0
    }
    
    private fun shouldShowFooter(): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error
    }
}
