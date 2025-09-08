package com.example.myapplication.app.ui.imagesearch.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.pixabay.data.PixabayImageEntity

class PixabayImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    private val tvImageId: TextView = itemView.findViewById(com.example.myapplication.R.id.tvImageId)
    private val tvUserName: TextView = itemView.findViewById(com.example.myapplication.R.id.tvUserName)
    private val tvTags: TextView = itemView.findViewById(com.example.myapplication.R.id.tvTags)
    
    fun bind(image: PixabayImageEntity) {
        tvImageId.text = "ID: ${image.id}"
        tvUserName.text = "User: ${image.userName ?: "Unknown"}"
        tvTags.text = "Tags: ${image.tags}"
    }
}
