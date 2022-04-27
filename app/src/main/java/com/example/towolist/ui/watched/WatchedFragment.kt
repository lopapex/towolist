package com.example.towolist.ui.watched

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.towolist.MainActivity
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentWatchedBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.IMainActivityFragment
import com.example.towolist.ui.list.MovieAdapter
import com.example.towolist.ui.to_watch.ToWatchFragmentDirections

class WatchedFragment : Fragment(), IMainActivityFragment {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository(requireContext())
    }

    private lateinit var binding: FragmentWatchedBinding
    private lateinit var adapter: MovieAdapter

    private lateinit var movies: List<MovieItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWatchedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies = movieRepository.getWatchedMovies()
        if (movies.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        }

        val mainActivity : MainActivity = (activity as MainActivity)
        updateLayout(mainActivity.isListLayout())
        updateSpinner()
    }

    override fun updateSpinner() {
        val mainActivity : MainActivity = (activity as MainActivity)
        if (mainActivity.isPopularSpinnerOption()) adapter.sortByPopularity() else adapter.sortByVoteAverage()
    }

    override fun search(text: CharSequence?, isUpdate: Boolean) {
        findNavController()
            .navigate(WatchedFragmentDirections.actionWatchedFragmentToListFragment(text.toString()))
    }

    override fun searchClose() {
    }

    override fun updateLayout(isList: Boolean) {
        adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(WatchedFragmentDirections.actionWatchedFragmentToDetailMovieFragment(it))
        }, isList)
        binding.recyclerView.adapter = adapter
        adapter.submitList(movies)

        setFragmentResultListener("updateWatched") { _, bundle ->
            val item = bundle.get("item") as MovieItem
            val index = adapter.getMovies().indexOf(item)

            if (item.isWatched && index >= 0) {
                adapter.removeItem(index)
            }
            binding.emptyView.visibility = if (adapter.getMovies().isEmpty()) View.VISIBLE else View.GONE
        }

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }
}