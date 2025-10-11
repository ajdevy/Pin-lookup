package com.example.myapplication.imagesearch.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.imagesearch.R
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class LoadingFooterViewHolder(
    itemView: View,
    private val onRetryClick: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    
    private val progressBar: ProgressBar = itemView.findViewById(R.id.displayLoadingProgressBar)
    private val errorText: TextView = itemView.findViewById(R.id.displayErrorMessageTextView)
    private val retryButton: MaterialButton = itemView.findViewById(R.id.performRetryButton)
    
    init {
        retryButton.setOnClickListener {
            Timber.d("LoadingFooterViewHolder retry button clicked")
            onRetryClick()
        }
    }
    
    fun bind(loadState: LoadState) {
        Timber.d("LoadingFooterViewHolder binding load state: $loadState")
        when (loadState) {
            is LoadState.Loading -> {
                Timber.d("Showing loading state in footer")
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
            is LoadState.Error -> {
                Timber.e("Showing error state in footer: ${loadState.error}")
                progressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                retryButton.visibility = View.VISIBLE
                errorText.text = "Error loading more items"
            }
            else -> {
                Timber.d("Hiding footer (not loading or error)")
                progressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
        }
    }
}
