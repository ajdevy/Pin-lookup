package com.example.myapplication.app.ui.imagesearch.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.pixabay.data.PixabayImageEntity

class PixabayImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    private val ivImage: ImageView = itemView.findViewById(com.example.myapplication.R.id.displayImageView)
    private val tvImageId: TextView = itemView.findViewById(com.example.myapplication.R.id.displayImageIdTextView)
    private val tvUserName: TextView = itemView.findViewById(com.example.myapplication.R.id.displayUserNameTextView)
    private val tvTags: TextView = itemView.findViewById(com.example.myapplication.R.id.displayTagsTextView)
    
    fun bind(image: PixabayImageEntity) {
        // Load image using Coil
        ivImage.load(image.previewUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
            crossfade(true)
        }
        
        tvImageId.text = "ID: ${image.id}"
        tvUserName.text = "User: ${image.userName ?: "Unknown"}"
        tvTags.text = "Tags: ${image.tags}"
    }
}
