package com.example.towolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.databinding.ItemListBinding
import com.example.towolist.ui.data.MovieItem

class MovieAdapter(
    private val onItemClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    private var movieItems: MutableList<MovieItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieItems[position], onItemClick)
    }

    fun submitList(newMovieItems: List<MovieItem>) {
        movieItems = newMovieItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movieItems.size
}