package com.example.towolist.ui.to_watch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.towolist.MainActivity
import com.example.towolist.databinding.FragmentToWatchBinding
import com.example.towolist.repository.MovieRepository

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

        mainActivity.updateLayout(this, binding.recyclerView, getItems)
        mainActivity.registerLayoutListener(this, binding.recyclerView, getItems)
    }
}