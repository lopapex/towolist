package com.example.towolist.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.databinding.ItemListBinding
import com.example.towolist.data.MovieItem

class MovieViewHolder(private val binding: ItemListBinding, private val view: View)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieItem: MovieItem, onItemClick: (MovieItem) -> Unit) {
        binding.mediaName.text = movieItem.name
        Glide.with(view.context)
            .load(movieItem.imageSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        binding.cardContainer.setOnClickListener {
            onItemClick(movieItem)
        }
    }
}