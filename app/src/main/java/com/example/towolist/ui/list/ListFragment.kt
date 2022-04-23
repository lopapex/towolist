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
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.`interface`.IUpdateLayoutFragment


class ListFragment : Fragment(), IUpdateLayoutFragment {

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
                    adapter.submitList(items)
                },
                onFailure = {
                    context?.toast("Something happened!")
                }
            )

            movieRepository.getPopularTvShows(
                onSuccess = { items ->
                    adapter.appendToList(items)
                    adapter.sortByPopularity()
                },
                onFailure = {
                    context?.toast("Something happened!")
                }
            )
        } else {
            movieRepository.getTopRatedMovies(
                onSuccess = { items ->
                    adapter.submitList(items)
                },
                onFailure = {
                    context?.toast("Something happened!")
                }
            )

            movieRepository.getTopRatedTvShows(
                onSuccess = { items ->
                    adapter.appendToList(items)
                    adapter.sortByVoteAverage()
                },
                onFailure = {
                    context?.toast("Something happened!")
                }
            )
        }

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }
}