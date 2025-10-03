package com.example.myapplication.imagesearch.ui

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.pixabay.data.PixabayImageEntity
import timber.log.Timber

class PixabayImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val displayImageView: ImageView = itemView.findViewById(R.id.displayImageView)
    private val displayImageIdTextView: TextView = itemView.findViewById(R.id.displayImageIdTextView)
    private val displayUserNameTextView: TextView = itemView.findViewById(R.id.displayUserNameTextView)
    private val displayTagsTextView: TextView = itemView.findViewById(R.id.displayTagsTextView)

    fun bind(image: PixabayImageEntity) {
        // Load image using Coil
        Timber.d("loading image url ${image.previewUrl}")
        displayImageView.load(image.previewUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
            crossfade(true)
        }

        displayImageIdTextView.text = "ID: ${image.id}"
        displayUserNameTextView.text = "User: ${image.userName ?: "Unknown"}"
        displayTagsTextView.text = "Tags: ${image.tags}"
    }
}
