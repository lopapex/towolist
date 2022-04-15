package com.example.towolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.databinding.ListItemMovieBinding
import com.example.towolist.data.MovieItem

class MovieListAdapter(
    private val onItemClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<MovieListViewHolder>() {

    private var movieItems: MutableList<MovieItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movieItems[position], onItemClick)
    }

    fun submitList(newMovieItems: List<MovieItem>) {
        movieItems = newMovieItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movieItems.size
}