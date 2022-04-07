package com.example.towolist.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.towolist.ui.detail.DetailMovieFragment
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository


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

        val context = view.context

        val adapter = MovieAdapter(onItemClick = {
            val bottomSheetFragment = DetailMovieFragment()
            bottomSheetFragment.show((context as AppCompatActivity).supportFragmentManager, bottomSheetFragment.getTag())
        })

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(view.context, 3)
        }
        binding.recyclerView.adapter = adapter

        adapter.submitList(movieRepository.getMockedData(100))
    }
}