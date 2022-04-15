package com.example.towolist.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.towolist.MainActivity
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.repository.MovieRepository


class ListFragment : Fragment() {

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

        val mainActivity :MainActivity = (activity as MainActivity)
        val getItems = { movieRepository.getMockedData(100) }

        mainActivity.updateLayout(this, binding.recyclerView, getItems)
        mainActivity.registerLayoutListener(this, binding.recyclerView, getItems)
    }
}