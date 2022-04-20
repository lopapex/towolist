package com.example.towolist.ui.list

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.ListItemMovieBinding

class MovieListViewHolder(private val binding: ListItemMovieBinding, private val view: View)
    : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(movieItem: MovieItem, onItemClick: (MovieItem) -> Unit) {
        Glide.with(view.context)
            .load(movieItem.imageSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        binding.mediaName.text = movieItem.name + " list"

        binding.info.text = "${movieItem.release_date.substringBefore("-")} ${view.context.getString(movieItem.rating)}"


        Glide.with(view.context)
            .load(movieItem.watchNow.first().iconSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.icon)

        binding.cardContainer.setOnClickListener {
            onItemClick(movieItem)
        }
    }
}