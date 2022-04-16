package com.example.towolist.ui.to_watch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.towolist.MainActivity
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentToWatchBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.list.ListFragmentDirections
import com.example.towolist.ui.watched.WatchedFragment
import com.example.towolist.ui.watched.WatchedFragmentDirections

class ToWatchFragment : Fragment() {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }

    private lateinit var binding: FragmentToWatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentToWatchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)
        val getItems = { movieRepository.getMockedData(10) }
        val onClick =  { it: MovieItem ->
            findNavController()
                .navigate(ToWatchFragmentDirections.actionToWatchFragmentToDetailMovieFragment(it))
        }

        mainActivity.updateLayout(onClick, binding.recyclerView, getItems)
        mainActivity.registerLayoutListener(onClick, binding.recyclerView, getItems)
    }
}