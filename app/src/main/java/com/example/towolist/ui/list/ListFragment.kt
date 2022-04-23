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
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.repository.toServiceItem
import com.example.towolist.ui.`interface`.IUpdateLayoutFragment
import com.mancj.materialsearchbar.MaterialSearchBar


class ListFragment : Fragment(), IUpdateLayoutFragment, MaterialSearchBar.OnSearchActionListener {

    fun Context.toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val movieRepository: MovieRepository by lazy {
        MovieRepository(requireContext())
    }

    private lateinit var binding: FragmentListBinding

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
                updateLayout(mainActivity.isListLayout())
            }
        }

        val searchBar = mainActivity.getSearchBar()
        searchBar.setOnSearchActionListener(this)

        updateLayout(mainActivity.isListLayout())
    }

    override fun updateLayout(isList: Boolean) {
        val adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }, isList)

        binding.recyclerView.adapter = adapter

        val mainActivity : MainActivity = (activity as MainActivity)

        if (mainActivity.isPopularSpinnerOption()) {
            movieRepository.getPopularMovies(
                onSuccess = { items ->
                    items.forEach {
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

                    adapter.submitList(items)
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )

            movieRepository.getPopularTvShows(
                onSuccess = { items ->
                    items.forEach {
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

                    adapter.appendToList(items)
                    adapter.sortByPopularity()
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )
        } else {
            movieRepository.getTopRatedMovies(
                onSuccess = { items ->
                    items.forEach {
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

                    adapter.submitList(items)
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )

            movieRepository.getTopRatedTvShows(
                onSuccess = { items ->
                    items.forEach {
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

                    adapter.appendToList(items)
                    adapter.sortByVoteAverage()
                },
                onFailure = {
                    context?.toast(R.string.general_error.toString())
                }
            )
        }

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
    }

    override fun onSearchConfirmed(text: CharSequence) {
        val mainActivity : MainActivity = (activity as MainActivity)

        val adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }, mainActivity.isListLayout())

        binding.recyclerView.adapter = adapter

        movieRepository.searchMovies(
            query = text.toString(),
            onSuccess = { items ->
                items.forEach {
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

                adapter.submitList(items)
            },
            onFailure = {
                context?.toast(R.string.general_error.toString())
            }
        )

        movieRepository.searchTvShows(
            query = text.toString(),
            onSuccess = { items ->
                items.forEach {
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

                adapter.appendToList(items)
            },
            onFailure = {
                context?.toast(R.string.general_error.toString())
            }
        )

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        mainActivity.getSearchBar().closeSearch()
    }

    override fun onButtonClicked(buttonCode: Int) {
    }
}