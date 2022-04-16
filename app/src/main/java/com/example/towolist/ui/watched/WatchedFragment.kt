package com.example.towolist.ui.watched

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.towolist.MainActivity
import com.example.towolist.data.MovieItem
import java.text.SimpleDateFormat
import java.util.*
import com.example.towolist.databinding.FragmentWatchedBinding
import com.example.towolist.repository.MovieRepository

class WatchedFragment : Fragment() {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }

    private lateinit var binding: FragmentWatchedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWatchedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)
        val getItems = { movieRepository.getMockedData(1) }
        val onClick =  { it: MovieItem ->
            findNavController()
                .navigate(WatchedFragmentDirections.actionWatchedFragmentToDetailMovieFragment(it))
        }

        mainActivity.updateLayout(onClick, binding.recyclerView, getItems)
        mainActivity.registerLayoutListener(onClick, binding.recyclerView, getItems)
    }
}