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
import com.example.towolist.repository.toServiceItem
import com.example.towolist.ui.IMainActivityFragment
import com.example.towolist.utils.toast
import com.example.towolist.webservice.response.WatchProviderByStateResponse

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
    private var searchNotFound: Boolean = false
    private var outsideCall: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)
        mainActivity.setSpinnerOptions(R.array.online_options)
        mainActivity.initFilterBottomFragment {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToFilterFragment())
        }

        updateLayout(mainActivity.isListLayout())

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !searchNotFound) {
                    if (mainActivity.getSearchBar().isSearchOpened) {
                        pageSearch++
                        search(searchText, true)
                    } else {
                        if (mainActivity.isFirstSpinnerOption()) pagePopular++ else pageTopRated++
                        loadItems(mainActivity.isFirstSpinnerOption(), true)
                    }
                }
            }
        })

        adapter.submitList(mutableListOf())

        val text = ListFragmentArgs.fromBundle(requireArguments()).text
        outsideCall = text != null
        if (outsideCall) {
            search(text, false)
        }

        setupFragmentListenerForFilter()
    }

    override fun updateSpinner() {
        val mainActivity : MainActivity = (activity as MainActivity)
        if (mainActivity.getSearchBar().isSearchOpened) {
            if (mainActivity.isFirstSpinnerOption()) adapter.sortByPopularity() else adapter.sortByVoteAverage()
        } else {
            loadIfEmpty(mainActivity.isFirstSpinnerOption())
        }
        binding.recyclerView.scrollToPosition(0)
    }

    override fun updateLayout(isList: Boolean) {
        var items = listOf<MovieItem>()
        var predicate: (MovieItem) -> Boolean =  { true }
        if (::adapter.isInitialized) {
            items = adapter.getMovies()
            predicate = adapter.getFilterFunction()
        }
        adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }, isList)

        setFragmentResultListener("updateState") { _, bundle ->
            val item = bundle.get("item") as MovieItem
            val index = (adapter.getMovies().indices).firstOrNull { i: Int -> item.id == adapter.getMovies()[i].id }
            if (index != null) {
                adapter.updateMovie(index, item)
            }
        }

        adapter.updateFilterFunction(predicate)
        adapter.submitList(items)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }

    private fun loadItems(isPopular : Boolean, isUpdate: Boolean) {
        if (outsideCall) return
        val movies: MutableList<MovieItem> = mutableListOf()
        val loader = initLoader(isUpdate)
        val page = if (isPopular) pagePopular else pageTopRated

        if (isPopular) {
            movieRepository.getPopularMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }
                    movies.addAll(movieItems.toMutableList())

                    movieRepository.getPopularTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }
                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.popularity }
                            popular.addAll(movies)
                            adapter.submitList(popular)
                            loader.visibility = View.GONE

                            if (movies.isEmpty() || adapter.getMovies().size < 10) {
                                pagePopular++
                                loadItems(isPopular, true)
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
        } else {
            movieRepository.getTopRatedMovies(
                page,
                onSuccess = { movieItems ->
                    movieItems.forEach {
                        getWatchProvidersMovies(it, loader)
                        setFlags(it)
                    }

                    movies.addAll(movieItems.toMutableList())
                    movieRepository.getTopRatedTvShows(
                        page,
                        onSuccess = { showItems ->
                            showItems.forEach {
                                getWatchProvidersShows(it, loader)
                                setFlags(it)
                            }

                            movies.addAll(showItems.toMutableList())
                            movies.sortByDescending { movie -> movie.voteAverage }
                            topRated.addAll(movies)
                            adapter.submitList(topRated)

                            loader.visibility = View.GONE
                            if (movies.isEmpty() || adapter.getMovies().size < 10) {
                                pageTopRated++
                                loadItems(isPopular, true)
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
        loadIfEmpty(mainActivity.isFirstSpinnerOption())
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
                    getWatchProvidersMovies(it, loader)
                    setFlags(it)
                }

                movies = movieItems.toMutableList()

                movieRepository.searchTvShows(
                    page,
                    query = searchText,
                    onSuccess = { showItems ->
                        showItems.forEach {
                            getWatchProvidersShows(it, loader)
                            setFlags(it)
                        }

                        movies.addAll(showItems.toMutableList())
                        movies.sortByDescending { movie -> if (mainActivity.isFirstSpinnerOption()) movie.popularity else movie.voteAverage }
                        adapter.appendToList(movies)

                        searchNotFound = movies.isEmpty()
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

    private fun requestFailed(loader: ProgressBar) {
        context?.toast(requireContext().getString(R.string.general_error))
        loader.visibility = View.GONE
    }

    private fun setFlags(it: MovieItem) {
        it.isToWatch = movieRepository.getToWatchByMovieId(it.id)
        it.isWatched = movieRepository.getWatchedByMovieId(it.id)
    }

    private fun initLoader(isUpdate: Boolean): ProgressBar {
        if (!isUpdate) {
            adapter.submitList(mutableListOf())
        }
        val loader = if (!isUpdate || adapter.getMovies().isEmpty()) binding.progressLoad else binding.progressUpdate
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
            binding.recyclerView.scrollToPosition(0)
        }
    }
}