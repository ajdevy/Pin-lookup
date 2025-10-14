package com.flexsentlabs.myapplication.imagesearch.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.flexsentlabs.myapplication.imagesearch.R
import com.flexsentlabs.myapplication.database.pixabay.PixabayImageEntity
import timber.log.Timber

class PixabayImageViewHolder(itemView: View, val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

    private val displayImageView: ImageView = itemView.findViewById(R.id.displayImageView)
    private val displayImageIdTextView: TextView = itemView.findViewById(R.id.displayImageIdTextView)
    private val displayUserNameTextView: TextView = itemView.findViewById(R.id.displayUserNameTextView)
    private val displayTagsTextView: TextView = itemView.findViewById(R.id.displayTagsTextView)

    @SuppressLint("SetTextI18n")
    fun bind(image: PixabayImageEntity) {
        // Load image using Coil
        Timber.d("loading image url ${image.previewUrl}")
        displayImageView.load(image.previewUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
            crossfade(true)
        }
        displayImageView.setOnClickListener {
            itemClickListener.onItemClicked(image)
        }

        displayImageIdTextView.text = "ID: ${image.id}"
        displayUserNameTextView.text = "User: ${image.userName ?: "Unknown"}"
        displayTagsTextView.text = "Tags: ${image.tags}"
    }
}
