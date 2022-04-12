package com.example.towolist.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentDetailMovieBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMovieFragment(private val item: MovieItem) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailMovieBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaName.text = item.name

        Glide.with(view.context)
            .load(item.imageSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        val watchNowListAdapter = ServiceAdapter(context as Activity, item.watchNow)
        binding.watchNowList.adapter = watchNowListAdapter

        val rentBuyListAdapter = ServiceAdapter(context as Activity, item.buyRent)
        binding.rentBuyList.adapter = rentBuyListAdapter
    }
}