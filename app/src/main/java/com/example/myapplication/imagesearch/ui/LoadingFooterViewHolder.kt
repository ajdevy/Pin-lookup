package com.example.myapplication.imagesearch.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.button.MaterialButton

class LoadingFooterViewHolder(
    itemView: View,
    private val onRetryClick: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    
    private val progressBar: ProgressBar = itemView.findViewById(R.id.displayLoadingProgressBar)
    private val errorText: TextView = itemView.findViewById(R.id.displayErrorMessageTextView)
    private val retryButton: MaterialButton = itemView.findViewById(R.id.performRetryButton)
    
    init {
        retryButton.setOnClickListener {
            onRetryClick()
        }
    }
    
    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.Loading -> {
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
            is LoadState.Error -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                retryButton.visibility = View.VISIBLE
                errorText.text = "Error loading more items"
            }
            else -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
        }
    }
}
