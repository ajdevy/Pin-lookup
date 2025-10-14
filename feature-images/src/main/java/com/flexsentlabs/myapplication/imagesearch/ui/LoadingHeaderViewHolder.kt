package com.flexsentlabs.myapplication.imagesearch.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.flexsentlabs.myapplication.imagesearch.R
import timber.log.Timber

class LoadingHeaderViewHolder(
    itemView: View,
    private val onRetryClick: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    
    private val progressBar: ProgressBar = itemView.findViewById(R.id.displayLoadingProgressBar)
    private val errorText: TextView = itemView.findViewById(R.id.displayErrorMessageTextView)
    private val retryButton: com.google.android.material.button.MaterialButton = itemView.findViewById(R.id.performRetryButton)
    
    init {
        retryButton.setOnClickListener {
            Timber.d("LoadingHeaderViewHolder retry button clicked")
            onRetryClick()
        }
    }
    
    fun bind(loadState: LoadState) {
        Timber.d("LoadingHeaderViewHolder binding load state: $loadState")
        when (loadState) {
            is LoadState.Loading -> {
                Timber.d("Showing loading state in header")
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
            is LoadState.Error -> {
                Timber.e("Showing error state in header: ${loadState.error}")
                progressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                retryButton.visibility = View.VISIBLE
                errorText.text = "Error loading images. Please try again."
            }
            else -> {
                Timber.d("Hiding header (not loading or error)")
                progressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
        }
    }
}
