package com.example.myapplication.app.ui.imagesearch.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.button.MaterialButton

class LoadingFooterViewHolder(
    itemView: View,
    private val onRetryClick: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    private val errorText: TextView = itemView.findViewById(R.id.tvError)
    private val retryButton: MaterialButton = itemView.findViewById(R.id.btnRetry)
    
    init {
        retryButton.setOnClickListener {
            onRetryClick()
        }
    }
    
    fun bind(loadState: androidx.paging.LoadState) {
        when (loadState) {
            is androidx.paging.LoadState.Loading -> {
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
            is androidx.paging.LoadState.Error -> {
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
