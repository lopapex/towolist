package com.example.towolist.ui.list

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.ListItemMovieBinding
import com.example.towolist.utils.getFormattedDateString

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

        binding.mediaName.text = movieItem.name

        binding.info.text =
            if (movieItem.releaseDate != null) "${movieItem.releaseDate.substringBefore("-")}" else view.context?.getString(R.string.unknown)

        val services = movieItem.watchNow + movieItem.buyRent

        if (services.isNotEmpty()) {
            Glide.with(view.context)
                .load(services.first().iconSource)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .fallback(R.drawable.empty_image)
                .into(binding.icon)
            binding.theater.text = ""
        } else {
            binding.icon.setImageResource(R.drawable.ic_calendar)
            binding.theater.text =
                "${view.context.getString(R.string.available)} " +
                    "${if (movieItem.releaseDate != null) movieItem.releaseDate.getFormattedDateString() else view.context?.getString(R.string.unknown)}"
        }

        binding.cardContainer.setOnClickListener {
            onItemClick(movieItem)
        }
    }
}