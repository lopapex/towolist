package com.example.towolist.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.towolist.MainActivity
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.repository.toServiceItem
import com.example.towolist.ui.IUpdateLayoutFragment
import com.example.towolist.webservice.response.WatchProviderByStateResponse
import com.mancj.materialsearchbar.MaterialSearchBar


class ListFragment : Fragment(), IUpdateLayoutFragment, MaterialSearchBar.OnSearchActionListener {

    private fun Context.toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val movieRepository: MovieRepository by lazy {
        MovieRepository(requireContext())
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: MovieAdapter
    private var page: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)

        val spinner = mainActivity.setupSpinner()
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (mainActivity.getSearchBar().isSearchOpened) {
                    if (mainActivity.isPopularSpinnerOption()) adapter.sortByPopularity() else adapter.sortByVoteAverage()
                } else {
                    page++
                    loadItems(mainActivity.isPopularSpinnerOption())
                }
            }
        }

        val searchBar = mainActivity.getSearchBar()
        searchBar.setOnSearchActionListener(this)

        updateLayout(mainActivity.isListLayout())
        loadItems(mainActivity.isPopularSpinnerOption())
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

    private fun loadItems(isPopular : Boolean) {
        var movies: MutableList<MovieItem> = mutableListOf()
        binding.progressBar.visibility = View.VISIBLE
        adapter.submitList(mutableListOf())

        if (isPopular) {
            movieRepository.getPopularMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it)
                    }
                    movies = movieItems.toMutableList()

                    movieRepository.getPopularTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it)
                            }

                            movies.addAll(showItems.toMutableList())
                            adapter.appendToList(movies)
                            adapter.sortByPopularity()
                            binding.progressBar.visibility = View.GONE
                        },
                        onFailure = {
                            context?.toast(R.string.general_error.toString())
                        }
                    )
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )
        } else {
            movieRepository.getTopRatedMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it)
                    }

                    movies = movieItems.toMutableList()
                    movieRepository.getTopRatedTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it)
                            }

                            movies.addAll(showItems.toMutableList())
                            adapter.appendToList(movies)
                            adapter.sortByVoteAverage()
                            binding.progressBar.visibility = View.GONE
                        },
                        onFailure = {
                            context?.toast(R.string.general_error.toString())
                        }
                    )
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (!enabled) {
            val mainActivity : MainActivity = (activity as MainActivity)
            loadItems(mainActivity.isPopularSpinnerOption())
        }
    }

    override fun onSearchConfirmed(text: CharSequence) {
        val mainActivity : MainActivity = (activity as MainActivity)
        var movies: MutableList<MovieItem> = mutableListOf()
        binding.progressBar.visibility = View.VISIBLE
        adapter.submitList(movies)

        movieRepository.searchMovies(
            page,
            query = text.toString(),
            onSuccess = { movieItems ->
                movieItems.forEach {
                    getWatchProvidersMovies(it)
                }

                movies = movieItems.toMutableList()

                movieRepository.searchTvShows(
                    page,
                    query = text.toString(),
                    onSuccess = { showItems ->
                        showItems.forEach {
                            getWatchProvidersShows(it)
                        }

                        movies.addAll(showItems.toMutableList())
                        adapter.appendToList(movies)
                        if (mainActivity.isPopularSpinnerOption()) adapter.sortByPopularity() else adapter.sortByVoteAverage()
                        binding.progressBar.visibility = View.GONE
                    },
                    onFailure = {
                        context?.toast(R.string.general_error.toString())
                    }
                )
            },
            onFailure = {
                context?.toast(R.string.general_error.toString())
            }
        )

        closeKeyboard(mainActivity)
    }

    override fun onButtonClicked(buttonCode: Int) {
    }

    private fun getWatchProvidersMovies(it: MovieItem) {
        movieRepository.getWatchProvidersByMovieId(
            it.id,
            onSuccess = { providerByState ->
                setProviders(providerByState, it)
            },
            onFailure = {
                context?.toast(R.string.general_error.toString())
            })
    }

    private fun getWatchProvidersShows(it: MovieItem) {
        movieRepository.getWatchProvidersByTvId(
            it.id,
            onSuccess = { providerByState ->
                setProviders(providerByState, it)
            },
            onFailure = {
                context?.toast(R.string.general_error.toString())
            })
    }

    private fun setProviders(
        providerByState: WatchProviderByStateResponse?,
        it: MovieItem,
    ) {
        if (providerByState != null) {
            if (providerByState.flatrate != null)
                it.watchNow = providerByState.flatrate
                    .map { provider -> provider.toServiceItem() }
                    .toMutableList()
            if (providerByState.buy != null)
                it.buyRent = providerByState.buy
                    .map { provider -> provider.toServiceItem() }
                    .toMutableList()
        }
    }

    private fun closeKeyboard(mainActivity: MainActivity) {
        val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}