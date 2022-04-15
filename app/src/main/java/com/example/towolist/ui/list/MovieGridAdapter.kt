package com.example.towolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.databinding.GridItemMovieBinding
import com.example.towolist.data.MovieItem

class MovieGridAdapter(
    private val onItemClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<MovieGridViewHolder>() {

    private var movieItems: MutableList<MovieItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridViewHolder {
        val binding = GridItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieGridViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: MovieGridViewHolder, position: Int) {
        holder.bind(movieItems[position], onItemClick)
    }

    fun submitList(newMovieItems: List<MovieItem>) {
        movieItems = newMovieItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movieItems.size
}