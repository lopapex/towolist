package com.example.towolist.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.towolist.MainActivity
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.`interface`.IUpdateLayoutFragment


class ListFragment : Fragment(), IUpdateLayoutFragment {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity : MainActivity = (activity as MainActivity)
        updateLayout(mainActivity.isGridLayout())
    }

    override fun updateLayout(isList: Boolean) {
        val getItems = { movieRepository.getMockedData(100) }
        val onClick =  { it: MovieItem ->
            findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
        }
        if (isList) {
            val adapter = MovieListAdapter(onItemClick = onClick)

            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
            }
            binding.recyclerView.adapter = adapter
            adapter.submitList(getItems())
        } else {
            val adapter = MovieGridAdapter(onItemClick = onClick)

            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 3)
            }
            binding.recyclerView.adapter = adapter
            adapter.submitList(getItems())
        }
    }
}