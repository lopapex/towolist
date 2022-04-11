package com.example.towolist.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.databinding.FragmentDetailMovieBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMovieFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailMovieBinding

    val language = arrayOf<String>("C","C++","Java")
    val description = arrayOf<String>(
        "C programming is considered as the base for other programming languages",
        "C++ is an object-oriented programming language.",
        "Java is a programming language and a platform."
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mediaName.text = "The Batman"
        binding.options.text = "Watch Options"

        Glide.with(view.context)
            .load("https://image.tmdb.org/t/p/w300_and_h450_bestv2/74xTEgt7R36Fpooo50r9T25onhq.jpg")
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        val myListAdapter = ServiceAdapter(context as Activity,language,description)

        binding.watchNowList.adapter = myListAdapter
    }
}