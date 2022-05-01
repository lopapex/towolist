package com.example.towolist.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.MainActivity
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.IMainActivityFragment
import com.example.towolist.utils.toast

class ListFragment : Fragment(), IMainActivityFragment {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository(requireContext())
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: MovieAdapter

    private var pagePopular: Int = 1
    private var pageTopRated: Int = 1
    private var pageSearch: Int = 1
    private var popular: MutableList<MovieItem> = mutableListOf()
    private var topRated: MutableList<MovieItem> = mutableListOf()
    private var searchText: String = ""
    private var outsideCall: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)

        updateLayout(mainActivity.isListLayout())

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && adapter.getMovies().isNotEmpty()) {
                    if (mainActivity.getSearchBar().isSearchOpened) {
                        pageSearch++
                        search(searchText, true)
                    } else {
                        if (mainActivity.isPopularSpinnerOption()) pagePopular++ else pageTopRated++
                        loadItems(mainActivity.isPopularSpinnerOption(), true)
                    }
                }
            }
        })

        val text = ListFragmentArgs.fromBundle(requireArguments()).text
        outsideCall = text != null
        if (text != null) {
            search(text, false)
        } else {
            loadItems(mainActivity.isPopularSpinnerOption(), false)
        }

        setupFragmentListenerForFilter()
    }

    override fun updateSpinner() {
        val mainActivity : MainActivity = (activity as MainActivity)
        if (mainActivity.getSearchBar().isSearchOpened) {
            if (mainActivity.isPopularSpinnerOption()) adapter.sortByPopularity() else adapter.sortByVoteAverage()
        } else {
            loadIfEmpty(mainActivity.isPopularSpinnerOption())
        }
        binding.recyclerView.scrollToPosition(0)
    }

    override fun updateLayout(isList: Boolean) {
        var items = listOf<MovieItem>()
        if (::adapter.isInitialized) {
            items = adapter.getMovies()
        }
        adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }, isList)

        adapter.submitList(items)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }

    private fun loadItems(isPopular : Boolean, isUpdate: Boolean) {
        val movies: MutableList<MovieItem> = mutableListOf()
        val loader = initLoader(isUpdate)
        val page = if (isPopular) pagePopular else pageTopRated

        if (isPopular) {
            movieRepository.getPopularMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        movieRepository.getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }
                    movies.addAll(movieItems.toMutableList())

                    movieRepository.getPopularTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                movieRepository.getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }
                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.popularity }
                            popular.addAll(movies)
                            adapter.submitList(popular)
                            loader.visibility = View.GONE
                        },
                        onFailure = {
                            movieRepository.requestFailed(loader)
                        }
                    )
                },
                onFailure = {
                    movieRepository.requestFailed(loader)
                }
            )
        } else {
            movieRepository.getTopRatedMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        movieRepository.getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }

                    movies.addAll(movieItems.toMutableList())
                    movieRepository.getTopRatedTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                movieRepository.getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }

                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.voteAverage }
                            topRated.addAll(movies)
                            adapter.submitList(topRated)
                            if (isUpdate) {
                                binding.progressUpdate.visibility = View.GONE
                            } else {
                                binding.progressLoad.visibility = View.GONE
                            }
                        },
                        onFailure = {
                            movieRepository.requestFailed(loader)
                        }
                    )
                },
                onFailure = {
                    movieRepository.requestFailed(loader)
                }
            )
        }
    }

    override fun searchClose() {
        val mainActivity : MainActivity = (activity as MainActivity)
        loadIfEmpty(mainActivity.isPopularSpinnerOption())
        searchText = ""
        binding.recyclerView.scrollToPosition(0)
        binding.noItemsFoundView.visibility = View.GONE
        if (outsideCall) {
            findNavController().navigateUp()
        }
    }

    override fun search(text: CharSequence?, isUpdate: Boolean) {
        val mainActivity : MainActivity = (activity as MainActivity)
        var movies: MutableList<MovieItem>
        binding.noItemsFoundView.visibility = View.GONE
        val loader = initLoader(isUpdate)
        searchText = text.toString()
        if (!isUpdate) {
            pageSearch = 1
        }
        val page = pageSearch

        movieRepository.searchMovies(
            page,
            query = searchText,
            onSuccess = { movieItems ->
                movieItems.forEach {
                    movieRepository.getWatchProvidersMovies(it, loader)
                    setFlags(it)
                }

                movies = movieItems.toMutableList()

                movieRepository.searchTvShows(
                    page,
                    query = searchText,
                    onSuccess = { showItems ->
                        showItems.forEach {
                            movieRepository.getWatchProvidersShows(it, loader)
                            setFlags(it)
                        }

                        movies.addAll(showItems.toMutableList())
                        movies.sortByDescending { movie -> if (mainActivity.isPopularSpinnerOption()) movie.popularity else movie.voteAverage }
                        adapter.appendToList(movies)
                        loader.visibility = View.GONE
                        if (adapter.getMovies().isEmpty()) {
                            binding.noItemsFoundView.visibility = View.VISIBLE
                        }
                    },
                    onFailure = {
                        movieRepository.requestFailed(loader)
                    }
                )
            },
            onFailure = {
                movieRepository.requestFailed(loader)
            }
        )
    }

    private fun setFlags(it: MovieItem) {
        it.isToWatch = movieRepository.getToWatchByMovieId(it.id)
        it.isWatched = movieRepository.getWatchedByMovieId(it.id)
    }

    private fun initLoader(isUpdate: Boolean): ProgressBar {
        if (!isUpdate) {
            adapter.submitList(mutableListOf())
        }
        val loader = if (isUpdate) binding.progressUpdate else binding.progressLoad
        loader.visibility = View.VISIBLE
        return loader
    }

    private fun loadIfEmpty(isPopular: Boolean) {
        if (isPopular) {
            if (popular.isEmpty()) {
                loadItems(isPopular, false)
            } else {
                adapter.submitList(popular)
            }
        } else {
            if (topRated.isEmpty()) {
                loadItems(isPopular, false)
            } else {
                adapter.submitList(topRated)
            }
        }
    }

    private fun setupFragmentListenerForFilter() {
        setFragmentResultListener("filterFragment") { _, bundle ->
            adapter.updateFilterFunction(bundle.get("predicate") as (MovieItem) -> Boolean)
            adapter.filterList()
            binding.noItemsFoundView.visibility = if (adapter.getMovies().isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.scrollToPosition(0)
        }
    }
}