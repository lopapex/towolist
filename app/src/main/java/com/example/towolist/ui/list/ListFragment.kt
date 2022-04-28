package com.example.towolist.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
import com.example.towolist.repository.toServiceItem
import com.example.towolist.ui.IMainActivityFragment
import com.example.towolist.ui.detail.DetailMovieFragmentArgs
import com.example.towolist.webservice.response.WatchProviderByStateResponse


class ListFragment : Fragment(), IMainActivityFragment {

    private fun Context.toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

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
    private var fetchFreshData = true
    private var adapterData = listOf<MovieItem>()

    private var predicate: (MovieItem) -> Boolean = {
        true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("ListFragment", "onViewCreated called")
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
        Log.i("ListFragment", "loadItems called")
        val movies: MutableList<MovieItem> = mutableListOf()
        val loader = initLoader(isUpdate)
        
        if (isPopular) {
            movieRepository.getPopularMovies(
                pagePopular,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }
                    movies.addAll(movieItems.toMutableList())

                    movieRepository.getPopularTvShows(
                        pagePopular,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }
                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.popularity }
                            popular.addAll(movies)
                            adapter.submitList(popular.filter(predicate))
                            loader.visibility = View.GONE
                        },
                        onFailure = {
                            requestFailed(loader)
                        }
                    )
                },
                onFailure = {
                    requestFailed(loader)
                }
            )
        } else {
            movieRepository.getTopRatedMovies(
                pageTopRated,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }

                    movies.addAll(movieItems.toMutableList())
                    movieRepository.getTopRatedTvShows(
                        pageTopRated,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }

                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.voteAverage }
                            topRated.addAll(movies)
                            adapter.submitList(topRated.filter(predicate))
                            if (isUpdate) {
                                binding.progressUpdate.visibility = View.GONE
                            } else {
                                binding.progressLoad.visibility = View.GONE
                            }
                        },
                        onFailure = {
                            requestFailed(loader)
                        }
                    )
                },
                onFailure = {
                    requestFailed(loader)
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

        movieRepository.searchMovies(
            pageSearch,
            query = searchText,
            onSuccess = { movieItems ->
                movieItems.forEach {
                    getWatchProvidersMovies(it, loader)
                    setFlags(it)
                }

                movies = movieItems.toMutableList()

                movieRepository.searchTvShows(
                    pageSearch,
                    query = searchText,
                    onSuccess = { showItems ->
                        showItems.forEach {
                            getWatchProvidersShows(it, loader)
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
                        requestFailed(loader)
                    }
                )
            },
            onFailure = {
                requestFailed(loader)
            }
        )
    }

    private fun getWatchProvidersMovies(it: MovieItem, loader: ProgressBar) {
        movieRepository.getWatchProvidersByMovieId(
            it.id,
            onSuccess = { providerByState ->
                setProviders(providerByState, it)
            },
            onFailure = {
                requestFailed(loader)
            })
    }

    private fun getWatchProvidersShows(it: MovieItem, loader: ProgressBar) {
        movieRepository.getWatchProvidersByTvId(
            it.id,
            onSuccess = { providerByState ->
                setProviders(providerByState, it)
            },
            onFailure = {
                requestFailed(loader)
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

    private fun requestFailed(loader: ProgressBar) {
        context?.toast(requireContext().getString(R.string.general_error))
        loader.visibility = View.GONE
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
            Log.i("FilterFragmentListener", "handling fragment result")
            predicate = bundle.get("predicate") as (MovieItem) -> Boolean

            if (fetchFreshData) {
                Log.i("FilterFragmentListener", "fetchingFreshData...")
                adapterData = adapter.getMovies()
                Log.i("FilterFragmentListener", "fetched ${adapterData.size} items")
                fetchFreshData = false
            }

            Log.i("FilterFragmentListener", "adapterData ${adapterData.size} items")
            val filtered = adapterData.filter(predicate)
            Log.i("FilterFragmentListener", "filtered ${filtered.size} items")
            adapter.submitList(filtered)
        }
    }
}