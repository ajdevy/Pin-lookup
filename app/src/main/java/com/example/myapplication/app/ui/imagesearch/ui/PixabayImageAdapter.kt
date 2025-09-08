package com.example.myapplication.app.ui.imagesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.R
import com.example.myapplication.pixabay.data.PixabayImageEntity

class PixabayImageAdapter : PagingDataAdapter<PixabayImageEntity, PixabayImageViewHolder>(
    DIFF_CALLBACK
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pixabay_image, parent, false)
        return PixabayImageViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PixabayImageViewHolder, position: Int) {
        val image = getItem(position)
        image?.let { holder.bind(it) }
    }
    
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PixabayImageEntity>() {
            override fun areItemsTheSame(
                oldItem: PixabayImageEntity,
                newItem: PixabayImageEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
            
            override fun areContentsTheSame(
                oldItem: PixabayImageEntity,
                newItem: PixabayImageEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
