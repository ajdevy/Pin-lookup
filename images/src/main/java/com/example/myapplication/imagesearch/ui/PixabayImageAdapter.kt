package com.example.myapplication.imagesearch.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.imagesearch.R
import com.example.myapplication.pixabay.data.PixabayImageEntity

class PixabayImageAdapter(val itemClickListener: ItemClickListener) :
    PagingDataAdapter<PixabayImageEntity, PixabayImageViewHolder>(
        DIFF_CALLBACK
    ) {

    private var loadState: LoadState = LoadState.NotLoading(false)

    @SuppressLint("NotifyDataSetChanged")
    fun updateLoadState(newLoadState: LoadState) {
        val oldLoadState = loadState
        loadState = newLoadState

        // Notify if the load state changed
        if (oldLoadState != newLoadState) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pixabay_image, parent, false)
        return PixabayImageViewHolder(view, itemClickListener)
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

public interface ItemClickListener {
    fun onItemClicked(item: PixabayImageEntity)
}