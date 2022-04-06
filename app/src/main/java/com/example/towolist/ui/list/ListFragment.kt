package com.example.towolist.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.ui.repository.MovieRepository

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val movieRepository: MovieRepository by lazy {
        MovieRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAdapter(onItemClick = {

        })

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(view.context, 3)
        }
        binding.recyclerView.adapter = adapter

        adapter.submitList(movieRepository.getMockedData(100))
    }
}