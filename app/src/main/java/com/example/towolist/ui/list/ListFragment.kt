package com.example.towolist.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.towolist.ui.`interface`.IUpdateLayoutFragment
import com.example.towolist.webservice.response.WatchProviderInfoResponse
import com.mancj.materialsearchbar.MaterialSearchBar


class ListFragment : Fragment(), IUpdateLayoutFragment, MaterialSearchBar.OnSearchActionListener {

    fun Context.toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }

    private lateinit var binding: FragmentListBinding
    private var adapter: MovieAdapter? = null

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
                loadItems(mainActivity.isPopularSpinnerOption())
            }
        }

        val searchBar = mainActivity.getSearchBar()
        searchBar.setOnSearchActionListener(this)

        updateLayout(mainActivity.isListLayout())
        loadItems(mainActivity.isPopularSpinnerOption())
    }

    override fun updateLayout(isList: Boolean) {
        var items = listOf<MovieItem>()
        if (adapter != null) {
            items = adapter?.getMovies()!!
        }
        adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }, isList)

        adapter?.submitList(items)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }

    private fun loadItems(isPopular : Boolean) {
        var movies: MutableList<MovieItem>
        if (isPopular) {
            movieRepository.getPopularMovies(
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        movieRepository.getWatchProvidersByMovieId(it.id,
                            onSuccess = { providerByState ->
                                if (providerByState != null) {
                                    if (providerByState.flatrate != null)
                                        it.watchNow = providerByState.flatrate.take(3)
                                            .map { provider -> provider.toServiceItem() }
                                            .toMutableList()
                                    if (providerByState.buy != null)
                                        it.buyRent =
                                            providerByState.buy.take(2).map { provider -> provider.toServiceItem() }
                                                .toMutableList()
                                }
                            },
                            onFailure = {
                                context?.toast(R.string.general_error.toString())
                            })
                    }
                    movies = movieItems.toMutableList()

                    movieRepository.getPopularTvShows(
                        onSuccess = { showItems ->
                            showItems.forEach {
                                movieRepository.getWatchProvidersByTvId(it.id,
                                    onSuccess = { providerByState ->
                                        if (providerByState != null) {
                                            if (providerByState.flatrate != null)
                                                it.watchNow = providerByState.flatrate.take(3)
                                                    .map { provider -> provider.toServiceItem() }
                                                    .toMutableList()
                                            if (providerByState.buy != null)
                                                it.buyRent =
                                                    providerByState.buy.take(2).map { provider -> provider.toServiceItem() }
                                                        .toMutableList()
                                        }
                                    },
                                    onFailure = {
                                        context?.toast(R.string.general_error.toString())
                                    })
                            }

                            movies.addAll(showItems.toMutableList())
                            adapter?.submitList(movies)
                            adapter?.sortByPopularity()
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
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        movieRepository.getWatchProvidersByMovieId(it.id,
                            onSuccess = { providerByState ->
                                if (providerByState != null) {
                                    if (providerByState.flatrate != null)
                                        it.watchNow = providerByState.flatrate.take(3)
                                            .map { provider -> provider.toServiceItem() }
                                            .toMutableList()
                                    if (providerByState.buy != null)
                                        it.buyRent =
                                            providerByState.buy.take(2).map { provider -> provider.toServiceItem() }
                                                .toMutableList()
                                }
                            },
                            onFailure = {
                                context?.toast(R.string.general_error.toString())
                            })
                    }

                    movies = movieItems.toMutableList()
                    movieRepository.getTopRatedTvShows(
                        onSuccess = { showItems ->
                            showItems.forEach {
                                movieRepository.getWatchProvidersByTvId(it.id,
                                    onSuccess = { providerByState ->
                                        if (providerByState != null) {
                                            if (providerByState.flatrate != null)
                                                it.watchNow = providerByState.flatrate.take(3)
                                                    .map { provider -> provider.toServiceItem() }
                                                    .toMutableList()
                                            if (providerByState.buy != null)
                                                it.buyRent =
                                                    providerByState.buy.take(2).map { provider -> provider.toServiceItem() }
                                                        .toMutableList()
                                        }
                                    },
                                    onFailure = {
                                        context?.toast(R.string.general_error.toString())
                                    })
                            }

                            movies.addAll(showItems.toMutableList())
                            adapter?.submitList(movies)
                            adapter?.sortByVoteAverage()
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
    }

    override fun onSearchConfirmed(text: CharSequence) {
        val mainActivity : MainActivity = (activity as MainActivity)

        var movies: MutableList<MovieItem>
        movieRepository.searchMovies(
            query = text.toString(),
            onSuccess = { movieItems ->
                movieItems.forEach {
                    movieRepository.getWatchProvidersByMovieId(it.id,
                        onSuccess = { providerByState ->
                            if (providerByState != null) {
                                if (providerByState.flatrate != null)
                                    it.watchNow = providerByState.flatrate.take(3).map {provider -> provider.toServiceItem()}.toMutableList()
                                if (providerByState.buy != null)
                                    it.buyRent = providerByState.buy.take(2).map {provider -> provider.toServiceItem() }.toMutableList()
                            }
                        },
                        onFailure = {
                            context?.toast(R.string.general_error.toString())
                        })
                }

                movies = movieItems.toMutableList()

                movieRepository.searchTvShows(
                    query = text.toString(),
                    onSuccess = { showItems ->
                        showItems.forEach {
                            movieRepository.getWatchProvidersByTvId(it.id,
                                onSuccess = { providerByState ->
                                    if (providerByState != null) {
                                        if (providerByState.flatrate != null)
                                            it.watchNow = providerByState.flatrate.take(3).map {provider -> provider.toServiceItem()}.toMutableList()
                                        if (providerByState.buy != null)
                                            it.buyRent = providerByState.buy.take(2).map {provider -> provider.toServiceItem() }.toMutableList()
                                    }
                                },
                                onFailure = {
                                    context?.toast(R.string.general_error.toString())
                                })
                        }

                        movies.addAll(showItems.toMutableList())
                        adapter?.submitList(movies)
                        if (mainActivity.isPopularSpinnerOption()) adapter?.sortByPopularity() else adapter?.sortByVoteAverage()
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

        mainActivity.getSearchBar().closeSearch()
    }

    override fun onButtonClicked(buttonCode: Int) {
    }
}