package com.example.towolist.ui.to_watch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.towolist.MainActivity
import com.example.towolist.databinding.FragmentToWatchBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.`interface`.IUpdateLayoutFragment
import com.example.towolist.ui.list.MovieAdapter

class ToWatchFragment : Fragment(), IUpdateLayoutFragment {

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
        updateLayout(mainActivity.isListLayout())
    }

    override fun updateLayout(isList: Boolean) {
        val adapter = MovieAdapter(onItemClick = {
            findNavController()
                .navigate(ToWatchFragmentDirections.actionToWatchFragmentToDetailMovieFragment(it))
        }, isList)
        binding.recyclerView.adapter = adapter
        adapter.submitList(movieRepository.getMockedData(3))

        binding.recyclerView.apply {
            layoutManager = if (isList) LinearLayoutManager(context) else GridLayoutManager(context, 3)
        }
    }
}