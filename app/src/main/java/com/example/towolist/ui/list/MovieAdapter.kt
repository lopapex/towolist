package com.example.towolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.databinding.GridItemMovieBinding
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.ListItemMovieBinding

class MovieAdapter(
    private val onItemClick: (MovieItem) -> Unit,
    private val isListLayout: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movieItems: MutableList<MovieItem> = mutableListOf()

    override fun getItemCount(): Int = movieItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isListLayout) {
            val binding = ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieListViewHolder(binding, parent)
        } else {
            val binding = GridItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieGridViewHolder(binding, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isListLayout){
            (holder as MovieListViewHolder).bind(movieItems[position], onItemClick)
        } else{
            (holder as MovieGridViewHolder).bind(movieItems[position], onItemClick)
        }
    }

    fun submitList(newMovieItems: List<MovieItem>) {
        movieItems = newMovieItems.toMutableList()
        notifyDataSetChanged()
    }
}