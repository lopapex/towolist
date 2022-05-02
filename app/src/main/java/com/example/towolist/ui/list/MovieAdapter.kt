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

    private var moviesAll: MutableList<MovieItem> = mutableListOf()
    private var movies: MutableList<MovieItem> = mutableListOf()
    private var predicate: (MovieItem) -> Boolean = {
        true
    }


    override fun getItemCount(): Int = movies.size

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
            (holder as MovieListViewHolder).bind(movies[position], onItemClick)
        } else{
            (holder as MovieGridViewHolder).bind(movies[position], onItemClick)
        }
    }

    fun submitList(newMovieItems: List<MovieItem>) {
        moviesAll = newMovieItems.toMutableList()
        filterList()
    }

    fun filterList() {
        movies = moviesAll.filter(predicate).toMutableList()
        notifyDataSetChanged()
    }

    fun appendToList(movieItems: List<MovieItem>) {
        moviesAll.addAll(movieItems.toMutableList())
        filterList()
    }

    fun sortByPopularity() {
        movies.sortByDescending { movie -> movie.popularity }
        notifyDataSetChanged()
    }

    fun sortByVoteAverage() {
        movies.sortByDescending { movie -> movie.voteAverage }
        notifyDataSetChanged()
    }

    fun sortByOldest() {
        movies.sortBy { movie -> movie.savedAt }
        notifyDataSetChanged()
    }

    fun sortByYoungest() {
        movies.sortByDescending { movie -> movie.savedAt }
        notifyDataSetChanged()
    }

    fun updateMovie(position: Int, newItem: MovieItem) {
        movies[position] = newItem
        notifyItemChanged(position)
    }

    fun getMovies(): List<MovieItem> {
        return movies
    }

    fun getFilterFunction(): (MovieItem) -> Boolean {
        return predicate
    }

    fun removeItem(position: Int) {
        movies.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateFilterFunction(newPredicate: (MovieItem) -> Boolean) {
        predicate = newPredicate
    }
}